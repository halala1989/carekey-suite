# 姜哥写病历（Lite9 独立版）

本分支 `codex/jiangge-medical-lite9` 从 Lite9 派生，作为“姜哥写病历”独立 Android 软件维护。

## 应用身份

- 应用名称：`姜哥写病历`
- applicationId：`com.hidble.carekeyboard.jiangge`
- 版本：`4.0-test30-jiangge1`
- 独立分支：`codex/jiangge-medical-lite9`
- 可与医疗住院管理正式测试版同时安装。

## 聚合的终端

一个 App 内可手动切换：

1. 手机蓝牙 HID：手机直接作为标准蓝牙键盘连接电脑。
2. Pico W：手机通过 BLE 数据通道发送，Pico W 输出 USB HID。
3. ESP32-S3 Super Mini：ESP32蓝牙键盘终端-0001，BLE 接收后输出 USB HID。
4. YD-ESP32-S3 N16R8：N16R8 桥接板，使用同一 BLE 数据协议。
5. ESP32-S3 旧 BLE HID：兼容早期归档固件。

## 精简后的功能

- 主界面直接显示当前连接设备名称和 MAC，不显示具体开发板类型。
- “扫描、配对或主动连接”进入统一设备页，电脑和所有开发板使用同一个扫描列表。
- 点击设备后，程序根据真实蓝牙类型自动选择电脑 HID 或开发板 BLE 连接方式。
- 首次连接成功后永久保存设备名称、MAC 和连接类别。
- 下次进入连接页会先主动重连上次设备；失败时再扫描并选择新设备。
- 保留文字输入、发送、暂停、停止、清空、1–10 档速度、四种中文输入模式和常用语。
- 暂停后点击发送从暂停位置继续；停止后再次发送从头开始。
- 输出任务由独立前台服务执行，切后台、页面被回收或屏幕自动关闭后仍会继续。
- 输出期间持有 CPU 局部唤醒锁；通知栏提供暂停、继续和停止操作。
- 长文本每 24 个字符执行一次安全排空，显式释放所有按键并等待 HID 缓冲消化，避免高速长文遗留修饰键后误触清空快捷键。
- 输入框内容自动保存为本地草稿；页面被系统重建或异常关闭后会恢复上次文字。
- 不显示大模型、API Token、住院患者和病历文书入口。
- 本版本使用独立包名和应用名称，可与原 Lite9、完整医疗版同时安装。

## 构建

在仓库根目录执行：

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
$env:ANDROID_HOME="C:\Android\Sdk"
cd android
.\gradlew.bat assembleLite --no-daemon
```

输出：

```text
android/app/build/outputs/apk/lite/app-lite.apk
```

完整医疗应用仍使用普通构建：

```powershell
.\gradlew.bat assembleDebug --no-daemon
```

## 关键源码

```text
android/app/src/main/java/com/hidble/phonekeyboard/LiteKeyboardActivity.kt
android/app/src/main/java/com/hidble/phonekeyboard/LiteOutputService.kt
android/app/src/main/java/com/hidble/phonekeyboard/LiteConnectionActivity.kt
android/app/src/main/java/com/hidble/phonekeyboard/LiteConnectionMemory.kt
android/app/src/main/res/layout/activity_lite_keyboard.xml
android/app/src/lite/AndroidManifest.xml
android/app/src/lite/res/drawable/ic_lite_launcher.xml
android/app/src/main/java/com/hidble/phonekeyboard/TerminalRegistry.kt
android/app/src/main/java/com/hidble/phonekeyboard/OutputRouter.kt
android/app/src/main/java/com/hidble/phonekeyboard/TerminalSettingsActivity.kt
```

Lite 使用资源布尔值 `lite_edition` 切换：

- Lite：隐藏大模型设置卡片，修改连接页标题。
- 普通完整版：保持全部医疗功能和大模型设置。

## 真机回归

正式发布前应分别验证：

- 手机蓝牙 HID 的首次配对和主动重连。
- Pico W、ESP32-S3 Super Mini、YD-ESP32-S3 的扫描、保存地址和重连。
- 四种中文输入模式与第 8–10 档速度。
- 外接板暂停、继续和完全停止。
- 屏幕自动关闭后，手机蓝牙 HID 和开发板输出均继续到完成。
- 超过 1000 字的普通文本与中文文本不出现全选、删除或清屏等修饰键误触。
- 常用语新增、修改、删除与插入。
