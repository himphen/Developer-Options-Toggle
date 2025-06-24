Developer Options Toggle - Product Requirements Document (PRD)
Version: 1.0

Date: June 24, 2025

Objective: To develop a lightweight Android utility application designed to provide a fast and simple interface for toggling the system's "Developer Options," maximizing the operational efficiency of this core function through home screen widgets.

1. Overview
For Android developers and power users, "Developer Options" is a frequently accessed feature for tasks like USB debugging, adjusting animation speeds, or using other advanced tools. However, enabling or disabling this option requires navigating deep into the Settings app through multiple steps, a tedious process. This inefficiency is especially pronounced when users must frequently switch between a debugging mode and a daily-use mode. Furthermore, many high-security applications, particularly banking apps, strictly check if Developer Options are enabled and will block access if they are. This forces users to manually disable the setting before using these apps and re-enable it afterward.

This project aims to solve this pain point by creating a minimalist Android application. After granting a one-time permission via ADB (Android Debug Bridge), users can directly control the state of "Developer Options" with a single switch in the app's main interface. More importantly, this project will provide two home screen widgets: one for instantly toggling the state, and another as a shortcut to the system's "Developer Options" screen. This will completely transform the current cumbersome manual workflow, saving valuable time for its target users.

2. Target User
Our target users are Android application developers or power users of the Android system. The specific challenges they face include:

Need for Frequent Toggling: Constantly needing to enable or disable developer options when developing, debugging, and testing daily use-case scenarios.

Demand for Efficiency: Aversion to the repetitive and time-consuming process of navigating through system settings, seeking a more direct and faster solution.

Technical Capability: Familiarity with and ability to use development tools like ADB to grant the application necessary system permissions.

Open-Source Community Participants: As this project will be open-sourced on GitHub, users may also be developers interested in learning or contributing to related technologies.

3. Core User Flow
One-time Setup: The user downloads or builds the application from GitHub. After installation, they must connect their device to a computer and run an ADB command in the terminal to grant the app permission to modify secure settings. This is a mandatory prerequisite for using the tool.

In-App Toggle and Status Check:

The user opens the app, and the main interface clearly displays the current status of "Developer Options" (On or Off).

The user can directly toggle the state using an on-screen Switch.

The interface also provides a button for one-click access to the native system "Developer Options" screen.

Using the Toggle Widget:

The user adds the "Dev Opts Toggle" widget to their home screen.

The widget's icon or background color intuitively displays the current state of "Developer Options."

Tapping the widget directly toggles the state without opening the main app. The widget's appearance updates accordingly.

Using the Shortcut Widget:

The user adds the "Dev Opts Shortcut" widget to their home screen.

This widget has a fixed appearance and, when tapped, immediately opens the system's "Developer Options" screen.

4. Functional Requirements
4.1. Permission Model & Setup
Required Permission (WRITE_SECURE_SETTINGS): The application must declare the android.permission.WRITE_SECURE_SETTINGS permission in its AndroidManifest.xml.

Granting Method: This application will not request the permission at runtime. It operates under the assumption that the user has manually granted it via ADB.

Error Handling: If the permission has not been granted, any write operation (toggling via the switch or widget) will fail silently. The main application interface should provide a brief notification (e.g., a Toast Message) upon a failed operation to inform the user that the permission is likely missing.

4.2. Main Application UI
Layout: A single-page, centered, vertical layout.

Theme: Supports dark mode only, using a professional and clean color palette.

Status Display: A Text component must clearly indicate the current status of "Developer Options" as "On" or "Off."

Toggle Switch: A Material 3 Switch component, with its state bound to the system setting.

System Page Shortcut: A Button component that, when clicked, triggers an android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS Intent to open the corresponding system settings page.

4.3. Home Screen Widgets
Implementation Technology: Built using the androidx.glance:glance-appwidget API.

Toggle Widget:

Visual State: The widget's background color will change based on the status of "Developer Options." It will show an accent color (e.g., blue) when on and a neutral color (e.g., gray) when off.

Action: On-click, it triggers an ActionCallback to execute the toggle logic and immediately requests an update to its own UI.

Shortcut Widget:

Visual State: A static icon with an appearance that does not change.

Action: On-click, it triggers actionStartActivity to directly open the system's "Developer Options" page.

5. Technical Specifications
5.1. Tech Stack
Language: Kotlin

UI Framework: Jetpack Compose with Material 3

Build System: Gradle with TOML version catalog

Widgets: AndroidX Glance API

Minimum Android Version (minSdk): 29 (Android 10)

5.2. Core Component Logic
DeveloperSettingManager.kt: A singleton or object class encapsulating all interaction logic with Settings.Global, including reading (getInt) and writing (putString) the developer options state. All write operations should be wrapped in a try-catch block to handle SecurityException.

MainScreen.kt: The main Composable function responsible for the UI layout. It will hold an observable state (State<Boolean>) to reflect the developer options status and refresh this state during the onResume lifecycle event.

ToggleWidget.kt / ShortcutWidget.kt: The two GlanceAppWidget implementations, each defining its respective UI (Content()) and click behavior.

6. Non-Functional Requirements
UI Language: All UI elements and informational messages will be in English, in line with the internationalization practices of open-source projects.

Performance: The application and its widgets should be extremely lightweight, minimizing their footprint on system resources.

7. Prerequisites
An Android device with USB debugging enabled (Android 10 or newer).

A computer with the Android Debug Bridge (ADB) installed.

After installing this application, you must execute the following command in your computer's terminal:

adb shell pm grant app.hibernatev2.developeroptionstoggle android.permission.WRITE_SECURE_SETTINGS

8. References
Settings.Global | Android Developers: https://developer.android.com/reference/android/provider/Settings.Global

WRITE_SECURE_SETTINGS Permission: https://developer.android.com/reference/android/Manifest.permission#WRITE_SECURE_SETTINGS

Glance API | Android Developers: https://developer.android.com/jetpack/compose/glance