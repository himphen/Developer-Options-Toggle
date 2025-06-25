Developer Options Toggle - Product Requirements Document (PRD)
Version: 1.3

Date: June 25, 2025

Objective: To develop a robust, lightweight Android utility application designed to provide a fast and simple interface for toggling the system's "Developer Options," "USB Debugging," and "Wi-Fi Debugging," and provide a direct shortcut to the main developer screen, maximizing operational efficiency and ensuring a crash-free user experience.

1. Overview
For Android developers and power users, "Developer Options" is a frequently accessed feature. Its most common uses include enabling "USB Debugging" and "Wi-Fi Debugging" for development. However, leaving these settings enabled poses a security risk and can be blocked by high-security applications, like banking apps. This forces users into a tedious cycle of manually enabling and disabling these settings through the system menus.

This project aims to solve this pain point by creating a minimalist Android application. After granting a one-time permission via ADB, users can directly control the state of Developer Options, USB Debugging, and Wi-Fi Debugging with dedicated switches in the app's main interface. The app will also provide home screen widgets and a direct in-app shortcut for even faster access, completely transforming the cumbersome manual workflow.

2. Target User
Our target users are Android application developers or power users of the Android system. The specific challenges they face include:

Need for Frequent Toggling: Constantly needing to enable or disable key developer settings for development, debugging, and daily use.

Demand for Efficiency: Aversion to the repetitive and time-consuming process of navigating through system settings.

Technical Capability: Familiarity with and ability to use development tools like ADB to grant the application necessary system permissions.

3. Core User Flow
One-time Setup: The user installs the app and grants the WRITE_SECURE_SETTINGS permission via a single ADB command.

In-App Interaction:

The user opens the app. The app immediately checks if the required permission has been granted.

If permission is granted: The main interface displays the current status of "Developer Options," "USB Debugging," and "Wi-Fi Debugging," each with its own enabled toggle switch.

If permission is NOT granted: The app displays a clear, persistent warning message. All toggle switches are disabled to prevent failed actions. The shortcut button to open the system settings page remains enabled.

Using the Toggle Widget: The user adds the "Dev Opts Toggle" widget to their home screen. This widget controls the main "Developer Options" setting. Tapping it toggles the state, and the widget's appearance updates to reflect the new state.

Using the Shortcut Widget: The user adds the "Dev Opts Shortcut" widget to their home screen, which opens the main "Developer Options" screen.

4. Functional Requirements
4.1. Permission Model & Setup
Required Permission (WRITE_SECURE_SETTINGS): This single permission is required to modify all developer-related settings.

Runtime Permission Check: The application must check for the WRITE_SECURE_SETTINGS permission every time the main activity resumes (onResume).

Failure State: If the permission check fails, the application must not crash. Instead, it will enter a "read-only" state where functionality requiring the permission is gracefully disabled.

4.2. Main Application UI
Layout: A single-page, centered, vertical layout with three rows for toggles and a section for a shortcut button.

Theme: Supports dark mode only.

Developer Options Toggle: A Material 3 Switch to control the master "Developer Options" setting.

USB Debugging Toggle: A second Material 3 Switch to control the "USB Debugging" (adb_enabled) setting.

Wi-Fi Debugging Toggle: A third Material 3 Switch to control the "Wi-Fi Debugging" (adb_wifi_enabled) setting.

State Logic: Both the USB Debugging and Wi-Fi Debugging toggles should be automatically disabled if the main Developer Options are turned off, as they are sub-settings.

Permission Handling: All three toggle switches must be disabled if the permission check fails.

Permission Warning UI: A prominent UI element must be displayed on the main screen if the permission check fails, instructing the user on how to grant it.

Developer Options Shortcut: A Button that opens the main system "Developer Options" page. This button remains enabled regardless of permission status.

4.3. Home Screen Widgets
Implementation Technology: Built using the androidx.glance:glance-appwidget API.

Toggle Widget: Controls the main "Developer Options" setting only. Its visual state must update to reflect the on/off status after being tapped.

Shortcut Widget: A static icon that opens the main "Developer Options" page.

5. Technical Specifications
5.1. Tech Stack
Language: Kotlin

UI Framework: Jetpack Compose with Material 3

Minimum Android Version (minSdk): 29 (Android 10)

5.2. Core Component Logic
DeveloperSettingManager.kt: This utility class will be expanded to manage all three settings.

isDeveloperOptionsEnabled() / setDeveloperOptions()

isUsbDebuggingEnabled(): Reads Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) == 1.

setUsbDebugging(): Writes to Settings.Global.ADB_ENABLED.

isWifiDebuggingEnabled(): Reads Settings.Global.getInt(context.contentResolver, "adb_wifi_enabled", 0) == 1.

setWifiDebugging(): Writes to the "adb_wifi_enabled" setting. All write functions must return a Boolean indicating success or failure.

MainScreen.kt: The main Composable function will be updated.

It will hold state variables for each of the developer settings and the permission status.

The enabled property of the debugging switches will be dependent on both the permission check and the state of the master Developer Options Switch.

It will contain the logic for a single shortcut button launching the ACTION_APPLICATION_DEVELOPMENT_SETTINGS Intent.

6. Non-Functional Requirements
UI Language: All UI elements and informational messages will be in English.

Performance: The application and its widgets should be extremely lightweight.

Robustness: The application must be resilient to the absence of its required permission and never crash as a result.

7. Prerequisites
An Android device with USB debugging enabled (Android 10 or newer).

A computer with the Android Debug Bridge (ADB) installed.

Execution of the following command after installation:

adb shell pm grant app.hibernatev2.developeroptionstoggle android.permission.WRITE_SECURE_SETTINGS

8. References
Settings.Global | Android Developers: https://developer.android.com/reference/android/provider/Settings.Global

WRITE_SECURE_SETTINGS Permission: https://developer.android.com/reference/android/Manifest.permission#WRITE_SECURE_SETTINGS

Glance API | Android Developers: https://developer.android.com/jetpack/compose/glance