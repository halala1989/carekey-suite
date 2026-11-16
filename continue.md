# CareKey Suite：跨电脑继续开发指南

最后同步：2026-11-16

## 仓库与分支

- GitHub：<https://github.com/mechanismlab/carekey-suite>
- 默认分支：`main`，用于套件总览和版本导航
- 完整医疗版：`medical-record-app-v4`，当前为 `4.0-test33`
- Lite 长期分支：`lite-input`
- Lite 工程历史：`codex/lite-unified`，当前开发到 `4.0-test30-lite9`
- 芯片和终端工程分支：`multi-target-6`、`esp32-s3-app`

本次工作目录当前位于 `codex/lite-unified`。如果继续开发 Lite9 的后台输出、发送、暂停和停止逻辑，请保留在这个分支；如果要继续长期维护 Lite，请从 `origin/lite-input` 新建本地跟踪分支。

## 新电脑首次获取

```powershell
git clone https://github.com/mechanismlab/carekey-suite.git
cd carekey-suite
git fetch --all --prune
git switch --track origin/codex/lite-unified
git status --short --branch
```

完整医疗版：

```powershell
git fetch origin
git switch --track origin/medical-record-app-v4
```

长期 Lite 分支：

```powershell
git fetch origin
git switch --track origin/lite-input
```

开始工作前先拉取当前分支：

```powershell
git pull --ff-only
```

## 当前 Lite9 构建

- 应用名：`全终端 Lite 键盘`
- applicationId：`com.hidble.carekeyboard.lite`
- 版本：`4.0-test30-lite9`
- versionCode：`40031`
- APK：`releases/v4.0-test30-lite9/全终端Lite键盘-v4.0-test30-lite9.apk`

Lite9 把输出任务移入独立前台服务并持有 CPU 局部唤醒锁，屏幕关闭后仍继续输出；通知栏可以暂停、继续和停止。长文本每 24 字安全排空并显式释放按键，降低高速发送后误触清屏快捷键的风险。

## Android 构建

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
$env:ANDROID_HOME="C:\Android\Sdk"
cd android
.\gradlew.bat assembleLite --no-daemon
```

Lite APK 输出：

```text
android/app/build/outputs/apk/lite/app-lite.apk
```

完整医疗版使用：

```powershell
.\gradlew.bat assembleDebug --no-daemon
```

## 关键源码

```text
android/app/src/main/java/com/hidble/phonekeyboard/
  LiteKeyboardActivity.kt
  LiteOutputService.kt
  LiteConnectionActivity.kt
  LiteConnectionMemory.kt
  TerminalRegistry.kt
  OutputRouter.kt
  BoardBleManager.kt
  TypingEngine.kt
```

固件工程位于：

```text
terminals/sources/multi-target-6/
terminals/sources/esp32-s3-app/
```

## 当前验证边界

- Lite9 APK 已完成构建和版本校验。
- 文档不表示 Lite9 的后台输出和长文防清屏机制已经在所有手机、电脑和板子上完成真机回归。
- GBK、自动 NumLock 和最新速度固件应继续在目标电脑的中文软件中实测。
- 完整医疗版和 Lite 版使用不同 applicationId，可以同时安装。

## 提交前检查

```powershell
git status --short --branch
git diff --check
git diff --stat
```

确认 APK、版本号、发布说明与源码一致后再提交：

```powershell
git add -A
git commit -m "描述本次修改"
git push origin HEAD
```

不要在 GitHub Issue、提交示例或公开文档中写入真实患者信息、API Key 或其他敏感数据。
