package app.hibernatev2.developeroptionstoggle.ui.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.hibernatev2.developeroptionstoggle.data.DeveloperSettingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _developerOptionsEnabled = MutableStateFlow(false)
    val developerOptionsEnabled: StateFlow<Boolean> = _developerOptionsEnabled.asStateFlow()

    private val _usbDebuggingEnabled = MutableStateFlow(false)
    val usbDebuggingEnabled: StateFlow<Boolean> = _usbDebuggingEnabled.asStateFlow()

    private val _wifiDebuggingEnabled = MutableStateFlow(false)
    val wifiDebuggingEnabled: StateFlow<Boolean> = _wifiDebuggingEnabled.asStateFlow()

    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission.asStateFlow()

    init {
        refreshStatus()
    }

    fun refreshStatus() {
        viewModelScope.launch {
            _hasPermission.value = hasWriteSecureSettingsPermission()
            _developerOptionsEnabled.value =
                DeveloperSettingManager.isDeveloperOptionsEnabled(application)
            _usbDebuggingEnabled.value =
                DeveloperSettingManager.isUsbDebuggingEnabled(application)
            _wifiDebuggingEnabled.value =
                DeveloperSettingManager.isWifiDebuggingEnabled(application)
        }
    }

    private fun hasWriteSecureSettingsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.WRITE_SECURE_SETTINGS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun setDeveloperOptions(enabled: Boolean) {
        viewModelScope.launch {
            val success = DeveloperSettingManager.setDeveloperOptions(enabled, application)
            if (success) {
                _developerOptionsEnabled.value = enabled
                if (!enabled) {
                    _usbDebuggingEnabled.value = false
                    _wifiDebuggingEnabled.value = false
                }
            } else {
                Toast.makeText(
                    application,
                    "Permission required. Grant via ADB.",
                    Toast.LENGTH_SHORT
                ).show()
                // Refresh to get the actual current (unchanged) state
                refreshStatus()
            }
        }
    }

    fun setUsbDebugging(enabled: Boolean) {
        viewModelScope.launch {
            val success = DeveloperSettingManager.setUsbDebugging(enabled, application)
            if (success) {
                _usbDebuggingEnabled.value = enabled
            } else {
                Toast.makeText(
                    application,
                    "Permission required. Grant via ADB.",
                    Toast.LENGTH_SHORT
                ).show()
                refreshStatus()
            }
        }
    }

    fun setWifiDebugging(enabled: Boolean) {
        viewModelScope.launch {
            val success = DeveloperSettingManager.setWifiDebugging(enabled, application)
            if (success) {
                _wifiDebuggingEnabled.value = enabled
            } else {
                Toast.makeText(
                    application,
                    "Permission required. Grant via ADB.",
                    Toast.LENGTH_SHORT
                ).show()
                refreshStatus()
            }
        }
    }
} 