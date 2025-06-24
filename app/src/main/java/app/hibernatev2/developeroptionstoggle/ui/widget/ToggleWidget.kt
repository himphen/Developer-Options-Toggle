package app.hibernatev2.developeroptionstoggle.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalGlanceId
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import app.hibernatev2.developeroptionstoggle.data.DeveloperSettingManager
import kotlinx.coroutines.launch

class ToggleWidget : GlanceAppWidget() {

    override var stateDefinition = PreferencesGlanceStateDefinition

    companion object {
        private val devOptionsEnabledKey = booleanPreferencesKey("dev_options_enabled")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    private fun Content() {
        val context = LocalContext.current
        val glanceId = LocalGlanceId.current
        val isEnabled = currentState(key = devOptionsEnabledKey) ?: false
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(if (isEnabled) Color(0xFF0081C9) else Color.DarkGray)
                .clickable {
                    coroutineScope.launch {
                        val currentStatus =
                            DeveloperSettingManager.isDeveloperOptionsEnabled(context)
                        val newStatus = !currentStatus
                        if (DeveloperSettingManager.setDeveloperOptions(newStatus, context)) {
                            updateAppWidgetState(context, glanceId) { prefs ->
                                prefs[devOptionsEnabledKey] = newStatus
                            }
                            update(context, glanceId)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = if (isEnabled) "ON" else "OFF")
        }
    }
} 