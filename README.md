# Soccer Trainer

A simple Android reaction-training app. Choose which colors to include in the rotation, set the change interval, and optionally display a random number over each color.

## Features

- Select one or more colors: red, blue, green, yellow, and orange.
- Choose a rotation interval of 7, 5, 3, 2, or 1 second.
- Optionally show a large random number from 1 to 9 over the color display.
- Start and stop the rotation with the main button.
- The displayed color may repeat on consecutive changes.

The app starts with red, blue, green, and yellow selected, the interval set to 7 seconds, and Number turned off. At least one color must remain selected.

## Requirements

- JDK 17
- Android SDK Platform 35
- Android SDK Build-Tools 35.0.0
- Android SDK Platform-Tools

Android Studio is not required. The Gradle wrapper downloads the configured Gradle version when first run. The Android SDK license must be accepted before the SDK packages can be installed or used.

## Build the debug APK

In PowerShell, from the project root, set Java and Android SDK paths if they are not already configured:

```powershell
$env:JAVA_HOME = "$env:LOCALAPPDATA\Programs\Microsoft\jdk-17.0.10.7-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
$env:ANDROID_SDK_ROOT = $env:ANDROID_HOME
.\gradlew.bat :app:assembleDebug
```

The build produces the standard APK at `app/build/outputs/apk/debug/app-debug.apk` and a versioned copy at:

```text
app/build/outputs/versioned-apk/SoccerTrainer-v1.0-debug.apk
```

The versioned filename is generated from the app's `versionName` in `app/build.gradle.kts`.
