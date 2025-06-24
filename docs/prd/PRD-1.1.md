Developer Options Toggle - Product Requirements Document (PRD)
Version: 1.1

Date: June 24, 2025

Objective: To develop a robust, lightweight Android utility application designed to provide a fast and simple interface for toggling the system's "Developer Options," maximizing operational efficiency through home screen widgets and ensuring a crash-free user experience.

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

In-App Interaction:

The user opens the app. The app immediately checks if the WRITE_SECURE_SETTINGS permission has been granted.

If permission is granted: The main interface displays the current status of "Developer Options" (On or Off) and enables the toggle switch. The user can interact with all controls freely.

If permission is NOT granted: The app displays a clear, persistent warning message. The toggle switch is disabled to prevent failed actions and user confusion. The button to open the system settings page remains enabled.

Using the Toggle Widget:

The user adds the "Dev Opts Toggle" widget to their home screen. The widget's appearance reflects the current on/off state.

Tapping the widget directly toggles the state. If the permission has been revoked, the action will fail silently and the widget's UI will not update.

Using the Shortcut Widget:

The user adds the "Dev Opts Shortcut" widget to their home screen. Tapping it opens the system's "Developer Options" screen.

4. Functional Requirements
4.1. Permission Model & Setup
Required Permission (WRITE_SECURE_SETTINGS): The application must declare the android.permission.WRITE_SECURE_SETTINGS permission in its AndroidManifest.xml.

Granting Method: This application will not request the permission at runtime. It operates under the assumption that the user has manually granted it via ADB.

Runtime Permission Check: The application must check for the WRITE_SECURE_SETTINGS permission every time the main activity resumes (onResume).

Failure State: If the permission check fails, the application must not crash. Instead, it will enter a "read-only" state where functionality requiring the permission is gracefully disabled, and a clear warning is displayed to the user.

4.2. Main Application UI
Layout: A single-page, centered, vertical layout.

Theme: Supports dark mode only, using a professional and clean color palette.

Status Display: A Text component must clearly indicate the current status of "Developer Options" as "On" or "Off."

Toggle Switch: A Material 3 Switch component. Its enabled state must be bound to the result of the permission check. It is enabled only if the permission is granted.

System Page Shortcut: A Button component that, when clicked, triggers an android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS Intent. This button remains enabled regardless of permission status.

Permission Warning UI: A prominent, non-intrusive UI element (e.g., a colored Card or Box with text and an icon) must be displayed on the main screen if the permission check fails. It should inform the user that the core functionality is disabled and that permission must be granted via ADB.

4.3. Home Screen Widgets
Implementation Technology: Built using the androidx.glance:glance-appwidget API.

Toggle Widget:

Visual State: The widget's background color will change based on the status of "Developer Options."

Action: On-click, it triggers an ActionCallback. The callback will attempt the write operation within a try-catch block to prevent crashes if the permission has been revoked since the app was last opened.

Shortcut Widget: A static icon that opens the system's "Developer Options" page.

5. Technical Specifications
5.1. Tech Stack
Language: Kotlin

UI Framework: Jetpack Compose with Material 3

Build System: Gradle with TOML version catalog

Widgets: AndroidX Glance API

Minimum Android Version (minSdk): 29 (Android 10)

5.2. Core Component Logic
DeveloperSettingManager.kt: A singleton or object class encapsulating all interaction logic with Settings.Global. The setDeveloperOptions function must return a Boolean indicating success or failure, allowing the calling UI to react accordingly.

MainScreen.kt: The main Composable function.

It must contain a state variable (e.g., hasPermission: State<Boolean>) that is updated onResume.

This check should be performed using context.checkCallingOrSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED.

The visibility of the warning UI and the enabled property of the Switch will be determined by this state variable.

6. Non-Functional Requirements
UI Language: All UI elements and informational messages will be in English.

Performance: The application and its widgets should be extremely lightweight.

Robustness: The application must be resilient to the absence of its required permission and never crash as a result.

7. Prerequisites
An Android device with USB debugging enabled (Android 10 or newer).

A computer with the Android Debug Bridge (ADB) installed.

After installing this application, you must execute the following command in your computer's terminal:

adb shell pm grant app.hibernatev2.developeroptionstoggle android.permission.WRITE_SECURE_SETTINGS

8. References
Settings.Global | Android Developers: https://developer.android.com/reference/android/provider/Settings.Global

WRITE_SECURE_SETTINGS Permission: https://developer.android.com/reference/android/Manifest.permission#WRITE_SECURE_SETTINGS

Glance API | Android Developers: https://developer.android.com/jetpack/compose/glance