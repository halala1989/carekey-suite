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

> 下列链接直接下载 APK。Android 9 或更高版本；这些是开发测试包，不是经过认证的医疗器械版本。<br>
> The links below download APK files directly. Android 9+; these are development test builds, not certified medical-device releases.

### Multi-target v35 · 多终端 v35

按目标硬件选择安装包；每个目标都有完整版和 Lite 简明版。Lite 版核心文字输出不需要模型账号。<br>
Choose the APK for your target hardware. Each target has a full and a Lite edition; Lite core text output does not require a model account.

| Target · 目标 | Full · 完整版 | Lite · 简明版 | Package · 包名 |
|---|---|---|---|
| **Generic Bluetooth · 通用蓝牙** | [PhoneBluetoothKeyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/PhoneBluetoothKeyboard-v35.apk) | [PhoneBluetoothKeyboard-Lite-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/PhoneBluetoothKeyboard-Lite-v35.apk) | `com.hidble.phonekeyboard` |
| **Pico W terminal · Pico W 终端** | [PicoBluetoothKeyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/PicoBluetoothKeyboard-v35.apk) | [PicoBluetoothKeyboard-Lite-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/PicoBluetoothKeyboard-Lite-v35.apk) | `com.hidble.picokeyboard` |
| **ESP32-S3 Super Mini · ESP32-S3 终端** | [Esp32BluetoothKeyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/Esp32BluetoothKeyboard-v35.apk) | [Esp32BluetoothKeyboard-Lite-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/Esp32BluetoothKeyboard-Lite-v35.apk) | `com.hidble.esp32keyboard` |
| **YD-ESP32-S3 N16R8 · YD 终端** | [YD-ESP32-S3-Keyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/YD-ESP32-S3-Keyboard-v35.apk) | [YD-ESP32-S3-Keyboard-Lite-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/YD-ESP32-S3-Keyboard-Lite-v35.apk) | `com.hidble.ydesp32s3keyboard` |

- [Release page · 发布页](https://github.com/mechanismlab/carekey-suite/releases/tag/v35)
- [SHA256SUMS.txt](https://github.com/mechanismlab/carekey-suite/releases/download/v35/SHA256SUMS.txt)

### Suite editions · 套件版本

| Edition · 版本 | Current build · 当前构建 | Download · 下载 |
|---|---|---|
| **CareKey Clinical · 医键临床版** | `4.0-test33` · versionCode `40032` | [APK](https://github.com/mechanismlab/carekey-suite/releases/download/v4.0-test33/CareKey-Clinical-4.0-test33.apk) · [Release notes](https://github.com/mechanismlab/carekey-suite/releases/tag/v4.0-test33) |
| **CareKey Lite · 医键 Lite** | `4.0-test30-lite9` | [APK](https://raw.githubusercontent.com/mechanismlab/carekey-suite/codex/lite-unified/releases/v4.0-test30-lite9/%E5%85%A8%E7%BB%88%E7%AB%AFLite%E9%94%AE%E7%9B%98-v4.0-test30-lite9.apk) · [Release notes](https://github.com/mechanismlab/carekey-suite/blob/codex/lite-unified/releases/v4.0-test30-lite9/RELEASE_NOTES.md) |

Clinical APK SHA-256: `FEB14A98987A43130E03C1E02B6AE84E72434B495A2D5E0FA5958EBED0C7A049`<br>
多终端 v35 APK 的 SHA-256 见 [`SHA256SUMS.txt`](https://github.com/mechanismlab/carekey-suite/releases/download/v35/SHA256SUMS.txt)。

## Quick start · 快速开始

### Requirements · 环境要求

| English | 简体中文 |
|---|---|
| Android 9 or later | Android 9 或更高版本 |
| For generic Bluetooth: a phone with Bluetooth HID support and a Bluetooth-enabled target computer | 通用蓝牙方案：手机支持蓝牙 HID，目标电脑带蓝牙 |
| For a terminal board: a supported bridge board and a USB port on the target computer | 终端板方案：受支持的桥接板，目标电脑有 USB 口 |
| JDK 17 and Android SDK 34 for source builds | 从源码构建时需要 JDK 17 和 Android SDK 34 |

### 1. Install the APK · 安装 APK

1. 在上方表格选择与硬件匹配的 APK，下载后在 Android 手机上打开安装。
2. 如果系统提示“禁止安装未知应用”，请允许浏览器或文件管理器安装未知应用。
3. 完整版包含更多连接/模型相关功能；只要直接打字，安装 Lite 简明版即可。

### 2. Generic Bluetooth · 通用蓝牙方案

1. 安装 `PhoneBluetoothKeyboard-v35.apk`（或 Lite 版）。
2. 打开 App，开启“模拟蓝牙键盘”开关，允许蓝牙和“对附近设备可见”权限。
3. 在电脑的“设置 → 蓝牙 → 添加设备”中搜索并配对 **手机蓝牙键盘**。
4. 把电脑光标放进目标输入框，在手机输入文字并发送。
5. 中文先用默认 Alt+X 模式测试；浏览器等不支持 Alt+X 的地方切换“十六进制”或“GBK”。
6. 如果丢字，降低输出速度；关闭开关即可停止模拟键盘。

### 3. Terminal board · 终端开发板方案

1. 按所选板卡烧录桥接固件；YD-ESP32-S3 参考 [FLASH_GUIDE.md](https://github.com/mechanismlab/carekey-suite/blob/multi-target-6/releases/yds3-v35/FLASH_GUIDE.md)。
2. 将板子的 **USB HID 口**接到目标电脑，电脑应识别为 USB HID 键盘。
3. 安装与板卡匹配的 APK，例如 `YD-ESP32-S3-Keyboard-v35.apk`。
4. 打开 App 的连接页，扫描并选择板子广播名：
   - YD：`YD-ESP32-S3 Bridge`
   - ESP32-S3：`ESP32-S3 Bridge`
   - Pico：按 Pico 固件广播名选择
5. 连接成功后，把电脑光标放进目标输入框，从手机发送文字。
6. 手机与电脑之间不需要配对成蓝牙键盘；蓝牙只负责手机到板子的数据，板子通过 USB HID 输出到电脑。

### 4. Chinese output · 中文输出

蓝牙/桥接板只能发送标准按键，不能直接发送汉字，因此 App 会模拟 Windows 的 Alt 码输入。默认使用 **Alt+X**；浏览器、旧式编辑器或中文 Windows 可切换十六进制、十进制或 GBK 模式。输出长文前先用短句测试。

## Hardware selection · 硬件选型

### 方案对比 · Which setup should I choose?

| Scenario · 场景 | Recommended · 推荐 | APK · 安装包 | Notes · 说明 |
|---|---|---|---|
| 不想买硬件、目标电脑自带蓝牙 | **通用蓝牙** | [PhoneBluetoothKeyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/PhoneBluetoothKeyboard-v35.apk) | 手机直接注册为蓝牙 HID 键盘；最简单，但手机需保持开机和蓝牙连接 |
| 需要长期稳定、目标电脑有 USB 口 | **YD-ESP32-S3 N16R8**（首选） | [YD-ESP32-S3-Keyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/YD-ESP32-S3-Keyboard-v35.apk) | 双 Type-C、16 MB Flash + 8 MB PSRAM、FT232RQ 烧录口 + 原生 USB HID；已有真机烧录/枚举验证 |
| 低成本、能接受手动进入下载模式 | **ESP32-S3 Super Mini** | [Esp32BluetoothKeyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/Esp32BluetoothKeyboard-v35.apk) | 4 MB Flash + 2 MB PSRAM；USB 调试与 HID 共用，烧录后可能需要拔插 USB |
| 已经有 Pico W | **Pico W** | [PicoBluetoothKeyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/PicoBluetoothKeyboard-v35.apk) | 手机 BLE → Pico W → USB HID；保留旧硬件即可，需烧录 Pico 固件 |

### 通用蓝牙应该选什么？ · Generic Bluetooth

选择 **Android 9+、支持 `BluetoothHidDevice` 的手机**，以及带蓝牙的 Windows/其他目标电脑即可，**不需要购买开发板**。安装：

- 完整版：[PhoneBluetoothKeyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/PhoneBluetoothKeyboard-v35.apk)
- 简明版：[PhoneBluetoothKeyboard-Lite-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/PhoneBluetoothKeyboard-Lite-v35.apk)

适合：临时使用、移动办公、不想接线。代价是手机必须靠近电脑并保持蓝牙连接，电脑蓝牙驱动/休眠状态可能影响稳定性。

### 终端开发板应该选什么？ · Terminal board

**首选 YD-ESP32-S3 N16R8**，具体识别特征：

- 丝印：`YD-ESP32-23-2022-V1.3 v2356`
- 模组：`ESP32-S3-WROOM-1 N16R8`
- 16 MB Flash、8 MB OPI PSRAM
- 两个 Type-C：一个 FT232RQ 烧录/日志口，一个 ESP32-S3 原生 USB HID 口
- 手机安装 [YD-ESP32-S3-Keyboard-v35.apk](https://github.com/mechanismlab/carekey-suite/releases/download/v35/YD-ESP32-S3-Keyboard-v35.apk)，连接广播 `YD-ESP32-S3 Bridge`

如果买不到 YD 板，可选 **ESP32-S3 Super Mini**：成本更低，但要处理 BOOT/下载模式和 USB 口复用问题。已有 Pico W 则继续使用 Pico 方案，不需要重复购买。

**不要选**没有原生 USB HID 能力、或只有经典蓝牙而没有 BLE 的通用开发板；当前桥接固件依赖 ESP32-S3/Pico 的 BLE 数据通道和 USB HID 输出。

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
