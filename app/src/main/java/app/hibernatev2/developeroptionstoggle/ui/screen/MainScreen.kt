package app.hibernatev2.developeroptionstoggle.ui.screen

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.hibernatev2.developeroptionstoggle.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val isDeveloperOptionsEnabled by viewModel.developerOptionsEnabled.collectAsStateWithLifecycle()
    val isUsbDebuggingEnabled by viewModel.usbDebuggingEnabled.collectAsStateWithLifecycle()
    val isWifiDebuggingEnabled by viewModel.wifiDebuggingEnabled.collectAsStateWithLifecycle()
    val hasPermission by viewModel.hasPermission.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingSwitch(
            title = "Developer Options",
            enabled = hasPermission,
            checked = isDeveloperOptionsEnabled,
            onCheckedChange = { viewModel.setDeveloperOptions(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingSwitch(
            title = "USB Debugging",
            enabled = hasPermission && isDeveloperOptionsEnabled,
            checked = isUsbDebuggingEnabled,
            onCheckedChange = { viewModel.setUsbDebugging(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingSwitch(
            title = "Wi-Fi Debugging",
            enabled = hasPermission && isDeveloperOptionsEnabled,
            checked = isWifiDebuggingEnabled,
            onCheckedChange = { viewModel.setWifiDebugging(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            context.startActivity(intent)
        }) {
            Text("Developer Options")
        }

        if (!hasPermission) {
            Spacer(modifier = Modifier.height(32.dp))
            PermissionWarning()
        }
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    enabled: Boolean,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
private fun PermissionWarning() {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Core functionality is disabled. Grant the WRITE_SECURE_SETTINGS permission via ADB.",
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
} 