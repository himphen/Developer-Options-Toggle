package app.hibernatev2.developeroptionstoggle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import app.hibernatev2.developeroptionstoggle.ui.screen.MainScreen
import app.hibernatev2.developeroptionstoggle.ui.theme.DeveloperOptionsToggleTheme
import app.hibernatev2.developeroptionstoggle.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeveloperOptionsToggleTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
} 