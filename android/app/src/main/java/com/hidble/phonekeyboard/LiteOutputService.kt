package com.hidble.phonekeyboard

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.CopyOnWriteArraySet
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class LiteOutputStatus {
    IDLE,
    STARTING,
    RUNNING,
    PAUSED,
    COMPLETED,
    FAILED,
    STOPPED
}

data class LiteOutputRequest(
    val text: String,
    val deviceLabel: String,
    val isBoard: Boolean,
    val speedLevel: Int,
    val unicodeMode: Int
)

data class LiteOutputSnapshot(
    val status: LiteOutputStatus = LiteOutputStatus.IDLE,
    val sentCharacters: Int = 0,
    val totalCharacters: Int = 0,
    val deviceLabel: String = "",
    val message: String = "等待发送"
) {
    val isActive: Boolean
        get() = status == LiteOutputStatus.STARTING ||
            status == LiteOutputStatus.RUNNING ||
            status == LiteOutputStatus.PAUSED

    val isPaused: Boolean
        get() = status == LiteOutputStatus.PAUSED
}

/**
 * Lite 输出的前台服务。
 *
 * 输出任务不再运行在 Activity 的 lifecycleScope 中。服务持有 PARTIAL_WAKE_LOCK，
 * 因此切后台、Activity 被回收或屏幕自动关闭后，手机 HID 与开发板 BLE 输出仍会继续。
 */
class LiteOutputService : Service() {

    companion object {
        private const val CHANNEL_ID = "lite_output"
        private const val NOTIFICATION_ID = 1003
        private const val ACTION_START = "com.hidble.phonekeyboard.lite.action.START"
        private const val ACTION_PAUSE = "com.hidble.phonekeyboard.lite.action.PAUSE"
        private const val ACTION_RESUME = "com.hidble.phonekeyboard.lite.action.RESUME"
        private const val ACTION_STOP = "com.hidble.phonekeyboard.lite.action.STOP"
        private const val BOARD_SAFETY_CHUNK_SIZE = 24
        private const val BOARD_SAFETY_FLUSH_MS = 160L

        private val listeners = CopyOnWriteArraySet<(LiteOutputSnapshot) -> Unit>()
        private val stateLock = Any()

        @Volatile
        private var currentSnapshot = LiteOutputSnapshot()
        private var pendingRequest: LiteOutputRequest? = null
        private var activeControl: TextOutputControl? = null

        fun snapshot(): LiteOutputSnapshot = currentSnapshot

        fun addListener(listener: (LiteOutputSnapshot) -> Unit) {
            listeners.add(listener)
            listener(currentSnapshot)
        }

        fun removeListener(listener: (LiteOutputSnapshot) -> Unit) {
            listeners.remove(listener)
        }

        fun startOutput(context: Context, request: LiteOutputRequest): Boolean {
            synchronized(stateLock) {
                if (currentSnapshot.isActive) return false
                pendingRequest = request
                activeControl = TextOutputControl()
                currentSnapshot = LiteOutputSnapshot(
                    status = LiteOutputStatus.STARTING,
                    totalCharacters = request.text.codePointCount(0, request.text.length),
                    deviceLabel = request.deviceLabel,
                    message = "正在启动后台输出…"
                )
            }
            publish(currentSnapshot)
            return try {
                ContextCompat.startForegroundService(
                    context,
                    Intent(context, LiteOutputService::class.java).setAction(ACTION_START)
                )
                true
            } catch (error: Exception) {
                synchronized(stateLock) {
                    activeControl = null
                    pendingRequest = null
                    currentSnapshot = currentSnapshot.copy(
                        status = LiteOutputStatus.FAILED,
                        message = "无法启动后台输出：${error.message ?: "系统限制前台服务"}"
                    )
                }
                publish(currentSnapshot)
                false
            }
        }

        fun pauseOutput(context: Context) = sendAction(context, ACTION_PAUSE)

        fun resumeOutput(context: Context) = sendAction(context, ACTION_RESUME)

        fun stopOutput(context: Context) = sendAction(context, ACTION_STOP)

        internal fun control(): TextOutputControl? = synchronized(stateLock) { activeControl }

        internal fun takePendingRequest(): LiteOutputRequest? = synchronized(stateLock) {
            val request = pendingRequest
            pendingRequest = null
            request
        }

        internal fun markRunning(request: LiteOutputRequest) {
            synchronized(stateLock) {
                currentSnapshot = currentSnapshot.copy(
                    status = LiteOutputStatus.RUNNING,
                    deviceLabel = request.deviceLabel,
                    message = "正在后台发送，屏幕关闭后仍会继续"
                )
            }
            publish(currentSnapshot)
        }

        internal fun reportProgress(sent: Int, total: Int) {
            synchronized(stateLock) {
                currentSnapshot = currentSnapshot.copy(
                    sentCharacters = sent,
                    totalCharacters = total
                )
            }
            publish(currentSnapshot)
        }

        internal fun markPaused(total: Int) {
            synchronized(stateLock) {
                currentSnapshot = currentSnapshot.copy(
                    status = LiteOutputStatus.PAUSED,
                    totalCharacters = total,
                    message = "输出已暂停，点击继续后会从当前位置恢复"
                )
            }
            publish(currentSnapshot)
        }

        internal fun markResumed() {
            synchronized(stateLock) {
                currentSnapshot = currentSnapshot.copy(
                    status = LiteOutputStatus.RUNNING,
                    message = "正在后台发送，屏幕关闭后仍会继续"
                )
            }
            publish(currentSnapshot)
        }

        internal fun finish(status: LiteOutputStatus, message: String, sent: Int? = null) {
            synchronized(stateLock) {
                activeControl = null
                pendingRequest = null
                currentSnapshot = currentSnapshot.copy(
                    status = status,
                    sentCharacters = sent ?: currentSnapshot.sentCharacters,
                    message = message
                )
            }
            publish(currentSnapshot)
        }

        private fun sendAction(context: Context, action: String) {
            runCatching {
                context.startService(Intent(context, LiteOutputService::class.java).setAction(action))
            }
        }

        private fun publish(snapshot: LiteOutputSnapshot) {
            listeners.forEach { listener ->
                runCatching { listener(snapshot) }
            }
        }
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var outputJob: Job? = null
    private var activeRequest: LiteOutputRequest? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var foregroundStarted = false
    private var lastNotificationUpdate = 0L

    private val outputStateListener: (LiteOutputSnapshot) -> Unit = { snapshot ->
        if (foregroundStarted) {
            val now = System.currentTimeMillis()
            if (snapshot.status != LiteOutputStatus.RUNNING || now - lastNotificationUpdate >= 500L) {
                lastNotificationUpdate = now
                getSystemService(NotificationManager::class.java)
                    .notify(NOTIFICATION_ID, buildNotification(snapshot))
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        addListener(outputStateListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ensureForeground(currentSnapshot)
        when (intent?.action) {
            ACTION_START -> beginPendingOutput()
            ACTION_PAUSE -> pauseOutput()
            ACTION_RESUME -> resumeOutput()
            ACTION_STOP -> stopCurrentOutput()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        val previous = snapshot()
        outputJob?.cancel()
        control()?.cancel()
        outputJob = null
        activeRequest = null
        releaseWakeLock()
        removeListener(outputStateListener)
        if (previous.isActive) {
            finish(
                LiteOutputStatus.STOPPED,
                if (previous.isPaused) {
                    "后台输出服务已结束，任务未继续"
                } else {
                    "后台输出服务被系统停止，请重新发送"
                }
            )
        }
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun beginPendingOutput() {
        if (outputJob?.isActive == true) return
        val request = takePendingRequest()
        if (request == null) {
            finishAndStop(LiteOutputStatus.FAILED, "输出任务启动失败，请重新发送")
            return
        }
        val control = control()
        if (control == null) {
            finishAndStop(LiteOutputStatus.FAILED, "输出控制状态已失效，请重新发送")
            return
        }
        activeRequest = request
        control.onProgress = { sent, total -> reportProgress(sent, total) }
        acquireWakeLock()
        markRunning(request)
        outputJob = serviceScope.launch {
            try {
                if (request.isBoard) {
                    sendToBoard(request, control)
                } else {
                    sendToPhoneHid(request, control)
                }
                finishAndStop(
                    LiteOutputStatus.COMPLETED,
                    "发送完成：${request.text.codePointCount(0, request.text.length)} / " +
                        request.text.codePointCount(0, request.text.length)
                )
            } catch (error: CancellationException) {
                val current = snapshot()
                if (current.status != LiteOutputStatus.STOPPED) {
                    finishAndStop(LiteOutputStatus.STOPPED, "输出已停止")
                }
            } catch (error: Exception) {
                finishAndStop(
                    LiteOutputStatus.FAILED,
                    error.message ?: "后台输出失败"
                )
            }
        }
    }

    private suspend fun sendToPhoneHid(request: LiteOutputRequest, control: TextOutputControl) {
        if (!KeyboardRuntime.snapshot().connected) error("电脑蓝牙连接已断开")
        val protocol = KeyboardRuntime.hidProtocol() ?: error("手机 HID 尚未初始化")
        protocol.setSpeed(request.speedLevel)
        protocol.setUnicodeMode(request.unicodeMode)
        protocol.typeText(request.text, control)
        if (control.interrupted) error("电脑蓝牙连接已断开，后台输出已中断")
    }

    private suspend fun sendToBoard(request: LiteOutputRequest, control: TextOutputControl) {
        val board = BoardLink.get(this)
        if (!board.isConnected()) error("当前开发板连接已断开")

        val speedResponse = board.sendCommandAwait("SPEED:${request.speedLevel}")
        if (speedResponse.startsWith("ERR")) error(speedResponse)
        val modeResponse = board.sendCommandAwait("UMOD:${request.unicodeMode}")
        if (modeResponse.startsWith("ERR")) error(modeResponse)

        val codePoints = request.text.codePoints().toArray()
        for ((index, codePoint) in codePoints.withIndex()) {
            control.checkpoint()
            val character = String(Character.toChars(codePoint))
            val response = board.sendTextAwait(character)
            if (response.startsWith("ERR")) error(response)
            reportProgress(index + 1, codePoints.size)
            if ((index + 1) % BOARD_SAFETY_CHUNK_SIZE == 0) {
                KeyboardRuntime.releaseAll()
                delay(BOARD_SAFETY_FLUSH_MS)
                control.checkpoint()
            }
        }
    }

    private fun pauseOutput() {
        val control = control() ?: return
        if (control.cancelled || control.paused) return
        control.paused = true
        releaseWakeLock()
        markPaused(snapshot().totalCharacters)
    }

    private fun resumeOutput() {
        val control = control() ?: return
        if (control.cancelled || !control.paused) return
        acquireWakeLock()
        control.paused = false
        markResumed()
    }

    private fun stopCurrentOutput() {
        val control = control()
        control?.cancel()
        KeyboardRuntime.releaseAll()
        if (activeRequest?.isBoard == true) {
            BoardLink.stopOutput()
        }
        outputJob?.cancel()
        outputJob = null
        activeRequest = null
        finishAndStop(LiteOutputStatus.STOPPED, "已停止，再次发送将从头开始")
    }

    private fun finishAndStop(status: LiteOutputStatus, message: String) {
        finish(status, message)
        outputJob = null
        activeRequest = null
        releaseWakeLock()
        stopForegroundCompat()
        stopSelf()
    }

    private fun ensureForeground(snapshot: LiteOutputSnapshot) {
        val notification = buildNotification(snapshot)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        foregroundStarted = true
    }

    private fun stopForegroundCompat() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        foregroundStarted = false
    }

    private fun buildNotification(snapshot: LiteOutputSnapshot): Notification {
        val openIntent = Intent(this, LiteKeyboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val title = when (snapshot.status) {
            LiteOutputStatus.PAUSED -> "Lite 键盘输出已暂停"
            LiteOutputStatus.STARTING -> "Lite 键盘正在启动后台输出"
            else -> "Lite 键盘正在后台输出"
        }
        val progressText = when {
            snapshot.totalCharacters > 0 ->
                "${snapshot.sentCharacters} / ${snapshot.totalCharacters} 字 · ${snapshot.deviceLabel}"
            else -> snapshot.message
        }
        val percent = if (snapshot.totalCharacters > 0) {
            (snapshot.sentCharacters * 100 / snapshot.totalCharacters).coerceIn(0, 100)
        } else {
            0
        }
        val toggleAction = if (snapshot.isPaused) ACTION_RESUME else ACTION_PAUSE
        val toggleTitle = if (snapshot.isPaused) "继续" else "暂停"
        val toggleIcon = if (snapshot.isPaused) {
            android.R.drawable.ic_media_play
        } else {
            android.R.drawable.ic_media_pause
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(progressText)
            .setStyle(NotificationCompat.BigTextStyle().bigText("${snapshot.message}\n$progressText"))
            .setProgress(100, percent, snapshot.totalCharacters == 0)
            .setOngoing(snapshot.isActive)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentIntent)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .addAction(toggleIcon, toggleTitle, servicePendingIntent(toggleAction, 1))
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "停止",
                servicePendingIntent(ACTION_STOP, 2)
            )
            .build()
    }

    private fun servicePendingIntent(action: String, requestCode: Int): PendingIntent {
        val intent = Intent(this, LiteOutputService::class.java).setAction(action)
        return PendingIntent.getService(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Lite 键盘后台输出",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "输出期间保持运行，息屏后仍会继续"
            setShowBadge(false)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    @SuppressLint("WakelockTimeout")
    private fun acquireWakeLock() {
        if (wakeLock?.isHeld == true) return
        runCatching {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            val lock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "carekey-lite:output"
            ).apply {
                setReferenceCounted(false)
                acquire()
            }
            wakeLock = lock
        }
    }

    private fun releaseWakeLock() {
        runCatching {
            wakeLock?.let { if (it.isHeld) it.release() }
        }
        wakeLock = null
    }
}
