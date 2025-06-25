package app.hibernatev2.developeroptionstoggle.data

import android.content.Context
import android.provider.Settings
import android.util.Log

object DeveloperSettingManager {

    private const val TAG = "DeveloperSettingManager"

    fun isDeveloperOptionsEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
            ) == 1
        } catch (e: SecurityException) {
            Log.e(TAG, "Missing WRITE_SECURE_SETTINGS permission.", e)
            // As per PRD, fail silently on read, but we can't determine state.
            // Assuming it's off is a safe default for the UI.
            false
        }
    }

    fun isUsbDebuggingEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.ADB_ENABLED, 0
            ) == 1
        } catch (e: SecurityException) {
            Log.e(TAG, "Missing WRITE_SECURE_SETTINGS permission.", e)
            false
        }
    }

    fun isWifiDebuggingEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                "adb_wifi_enabled", 0
            ) == 1
        } catch (e: SecurityException) {
            Log.e(TAG, "Missing WRITE_SECURE_SETTINGS permission.", e)
            false
        }
    }

    fun setDeveloperOptions(enabled: Boolean, context: Context): Boolean {
        return try {
            Settings.Global.putString(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, if (enabled) "1" else "0"
            )
            true
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to set Developer Options.", e)
            false
        }
    }

    fun setUsbDebugging(enabled: Boolean, context: Context): Boolean {
        return try {
            Settings.Global.putString(
                context.contentResolver,
                Settings.Global.ADB_ENABLED,
                if (enabled) "1" else "0"
            )
            true
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to set USB Debugging.", e)
            false
        }
    }

    fun setWifiDebugging(enabled: Boolean, context: Context): Boolean {
        return try {
            Settings.Global.putString(
                context.contentResolver,
                "adb_wifi_enabled",
                if (enabled) "1" else "0"
            )
            true
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to set Wi-Fi Debugging.", e)
            false
        }
    }
} 