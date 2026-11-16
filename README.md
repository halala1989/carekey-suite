# CareKey Suite · 医键套件

[![Android](https://img.shields.io/badge/Android-9%2B-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Bluetooth HID](https://img.shields.io/badge/Bluetooth-HID-0082FC?logo=bluetooth&logoColor=white)](#how-it-works--工作方式)
[![License](https://img.shields.io/github/license/mechanismlab/carekey-suite)](LICENSE)

> **Clinical text input and inpatient documentation for Android, without screen capture.**<br>
> **面向 Android 的临床文字输入与住院文书套件，不读取电脑屏幕。**

> Former repository name: `bluetooth-hid-keyboard`; existing GitHub links redirect automatically.<br>
> 原仓库名：`bluetooth-hid-keyboard`；旧 GitHub 链接会自动跳转。

---

## Overview · 项目简介

| English | 简体中文 |
|---|---|
| CareKey Suite turns text typed on an Android phone into standard keyboard input on a connected computer through Bluetooth HID or a supported HID bridge board. | 医键套件把 Android 手机上输入的文字，通过蓝牙 HID 或受支持的 HID 桥接板，转换为目标电脑上的标准键盘输入。 |
| The target computer does not need a screen-capture service, a desktop helper, or a special driver for normal HID use. | 正常使用 HID 时，目标电脑不需要屏幕捕获服务、桌面辅助程序或专用驱动。 |
| The repository uses branches as product boundaries: one branch for the full clinical workflow and another for the lightweight keyboard workflow. | 仓库使用分支划分产品边界：一个分支负责完整临床工作流，另一个分支负责轻量键盘工作流。 |

## Product editions · 产品版本

| Edition | Branch | English | 简体中文 |
|---|---|---|---|
| **CareKey Lite** | [`lite-input`](https://github.com/mechanismlab/carekey-suite/tree/lite-input) | Direct text-to-keyboard output, common phrases, selectable speed, and several Chinese output modes. No model account is required for core text delivery. | 直接输出文字、常用语、速度选择和多档中文输出方式。核心文字输入不要求模型账号。 |
| **CareKey Clinical** | [`medical-record-app-v4`](https://github.com/mechanismlab/carekey-suite/tree/medical-record-app-v4) | Patient management, clinical documentation workspace, Markdown writing skills, optional DeepSeek/MiMo support, and multi-terminal output. | 患者管理、临床文书工作台、Markdown 书写 Skill、可选 DeepSeek/MiMo 模型支持，以及多终端输出。 |
| **Engineering history** | [`codex/lite-unified`](https://github.com/mechanismlab/carekey-suite/tree/codex/lite-unified) | Unified Lite implementation with send, pause, continue, stop, and clear controls. Preserved as the source history for `lite-input`. | 统一 Lite 实现，包含发送、暂停、继续、停止和清空控制，作为 `lite-input` 的来源历史保留。 |

The complete branch map and maintenance rules are documented in [`PROJECTS.md`](PROJECTS.md).<br>
完整分支职责和维护规则见 [`PROJECTS.md`](PROJECTS.md)。

## Core capabilities · 核心能力

| Capability | English | 简体中文 |
|---|---|---|
| **Direct output** | Type on the phone and send text to the current cursor position on the target computer. | 在手机上输入文字，并输出到目标电脑当前光标位置。 |
| **Unified connection** | Discover and select supported Bluetooth computers or bridge terminals from one connection workflow. | 在一个连接流程中扫描并选择受支持的蓝牙电脑或桥接终端。 |
| **Output control** | Send, pause, resume, stop, clear, and adjust the output speed while monitoring progress. | 支持发送、暂停、继续、停止、清空、速度调节和输出进度显示。 |
| **Clinical workflow** | Maintain a patient list and generate structured inpatient documents from a chronological natural-language record. | 维护患者列表，并依据按时间记录的自然语言病情资料生成结构化住院文书。 |
| **Writing skills** | Import Markdown skills to guide document structure and writing style. | 导入 Markdown Skill，指导文书结构和书写方式。 |
| **Chinese output** | Use Alt+X, hexadecimal, decimal, or GBK modes depending on the target application and Windows environment. | 根据目标软件和 Windows 环境，使用 Alt+X、十六进制、十进制或 GBK 模式。 |

## How it works · 工作方式

```text
Android app
    |
    +-- Bluetooth HID --------------------------> Target computer
    |
    +-- Bluetooth LE --> HID bridge board ------> USB HID input
```

| English | 简体中文 |
|---|---|
| The phone can register as a Bluetooth HID keyboard or communicate with a supported bridge board that presents itself to the computer as a USB HID keyboard. | 手机可以注册为蓝牙 HID 键盘，也可以连接受支持的桥接板，由桥接板以 USB HID 键盘身份连接电脑。 |
| Patient records and settings remain in the Android application. Generated clinical text must be reviewed before use. | 患者记录和设置保存在 Android 应用内。生成的临床文字在使用前必须由医生审核。 |

## Preview · 界面预览

| Patient list · 患者列表 | Clinical documentation · 病历工作台 |
|---|---|
| ![Patient list](https://raw.githubusercontent.com/mechanismlab/carekey-suite/medical-record-app-v4/design-prototype/main-screen-redesign-preview.png) | ![Clinical documentation workspace](https://raw.githubusercontent.com/mechanismlab/carekey-suite/medical-record-app-v4/design-prototype/medical-record-workspace-preview.png) |

## Releases · 发布版本

| Edition | Current build | Download |
|---|---|---|
| **CareKey Clinical / 医键临床版** | `4.0-test33` · versionCode `40032` | [APK](https://github.com/mechanismlab/carekey-suite/releases/download/v4.0-test33/CareKey-Clinical-4.0-test33.apk) · [Release notes](https://github.com/mechanismlab/carekey-suite/releases/tag/v4.0-test33) |
| **CareKey Lite / 医键 Lite** | `4.0-test30-lite8` · versionCode `40030` | [Branch build](https://github.com/mechanismlab/carekey-suite/blob/lite-input/releases/v4.0-test30-lite8/RELEASE_NOTES.md) |

Clinical APK SHA-256: `FEB14A98987A43130E03C1E02B6AE84E72434B495A2D5E0FA5958EBED0C7A049`<br>
Lite APK SHA-256: `D6472DB2F6D983FBC3645FF9037E881FCD35E9A86F7FB1147B7F35D93EBE33F4`

These are development test builds, not certified medical-device releases.<br>
以上版本为开发测试包，不是经过认证的医疗器械版本。

## Quick start · 快速开始

### Requirements · 环境要求

| English | 简体中文 |
|---|---|
| Android 9 or later on a phone with Bluetooth HID support | Android 9 或更高版本，手机支持蓝牙 HID |
| A target computer with Bluetooth; optional supported HID bridge board | 带蓝牙的目标电脑；可选受支持的 HID 桥接板 |
| JDK 17 and Android SDK 34 for source builds | 从源码构建时需要 JDK 17 和 Android SDK 34 |

### Build the Clinical edition · 构建临床版

```powershell
git clone https://github.com/mechanismlab/carekey-suite.git
cd carekey-suite
git switch medical-record-app-v4
cd android

$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:ANDROID_HOME = "C:\Android\Sdk"
.\gradlew.bat assembleDebug --no-daemon
```

APK output / APK 输出位置:

```text
android/app/build/outputs/apk/debug/app-debug.apk
```

### Build the Lite edition · 构建 Lite 版

```powershell
git switch lite-input
cd android
.\gradlew.bat assembleLite --no-daemon
```

Lite APK output / Lite APK 输出位置:

```text
android/app/build/outputs/apk/lite/app-lite.apk
```

Adjust `JAVA_HOME` and `ANDROID_HOME` to match your computer.<br>
请根据自己电脑的实际安装位置修改 `JAVA_HOME` 和 `ANDROID_HOME`。

### Connect and send · 连接并输出

| English | 简体中文 |
|---|---|
| 1. Open the connection page and scan for a computer or supported bridge board. | 1. 打开连接页面，扫描电脑或受支持的桥接板。 |
| 2. Select a device and wait for the status indicator to confirm the connection. | 2. 选择设备，等待状态灯确认连接成功。 |
| 3. Place the computer cursor in the intended text field. | 3. 把电脑光标放到目标输入框。 |
| 4. Start output from the phone and adjust speed if characters are missing. | 4. 从手机开始输出；如果出现丢字，适当降低速度。 |

## Chinese output modes · 中文输出方式

| Mode | English | 简体中文 |
|---|---|---|
| **Alt+X** | Works with many RichEdit applications such as Notepad and Word. | 适用于记事本、Word 等许多 RichEdit 软件。 |
| **Hexadecimal** | May require the Windows `EnableHexNumpad` setting and NumLock. | 可能需要 Windows `EnableHexNumpad` 设置和 NumLock。 |
| **Decimal** | Works in some legacy RichEdit editors. | 适用于部分旧式 RichEdit 编辑器。 |
| **GBK** | Intended primarily for Chinese Windows environments. | 主要用于中文版 Windows 环境。 |

The exact supported mode depends on the target software. Test with a short sentence before sending a long document.<br>
实际可用方式取决于目标软件。输出长文前，请先用短句测试。

## Privacy and safety · 隐私与安全

| English | 简体中文 |
|---|---|
| CareKey does not capture or monitor the desktop screen. | 医键不捕获或监控电脑屏幕。 |
| Clinical records and API keys are stored by the Android application. | 临床病例和 API Key 由 Android 应用保存在本机。 |
| When an AI feature is used, selected clinical text may be sent to the provider configured by the user. | 使用 AI 功能时，所选临床文字可能发送到用户配置的模型服务商。 |
| This repository is a development project, not a certified medical device. | 本项目是开发项目，不是经过认证的医疗器械。 |
| Clinical use requires institutional authorization, privacy assessment, workflow validation, and clinician review. | 用于真实临床前，必须完成机构授权、隐私评估、流程验证和医生审核。 |
| Never post patient-identifiable information, credentials, or API keys in issues, pull requests, or public logs. | 禁止在 Issue、Pull Request 或公开日志中发布可识别患者身份的信息、凭据或 API Key。 |

Read [`docs/PRIVACY-AND-SAFETY.md`](docs/PRIVACY-AND-SAFETY.md) before handling real patient data.<br>
处理真实患者数据前，请阅读 [`docs/PRIVACY-AND-SAFETY.md`](docs/PRIVACY-AND-SAFETY.md)。

## Documentation · 文档

| Topic | English | 简体中文 |
|---|---|---|
| Installation | [Getting started](docs/GETTING-STARTED.md) | [安装与构建](docs/GETTING-STARTED.md) |
| Architecture | [Architecture](docs/ARCHITECTURE.md) | [架构说明](docs/ARCHITECTURE.md) |
| Safety | [Privacy and safety](docs/PRIVACY-AND-SAFETY.md) | [隐私与安全](docs/PRIVACY-AND-SAFETY.md) |
| Development | [Contributing](docs/CONTRIBUTING.md) | [参与开发](docs/CONTRIBUTING.md) |
| Releases | [Release process](docs/RELEASES.md) | [发布流程](docs/RELEASES.md) |
| Branches | [Branch catalog](PROJECTS.md) | [分支目录](PROJECTS.md) |

## License · 许可证

[MIT](LICENSE)
