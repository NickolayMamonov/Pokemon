package dev.whysoezzy.pokemon.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.whysoezzy.core_data.datastore.SettingsDataStore
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.ThemeMode
import dev.whysoezzy.domain.model.AppTheme
import dev.whysoezzy.pokemon.presentation.main.MainScreen
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val settingsDataStore: SettingsDataStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            settingsDataStore.themeFlow.collect { theme ->
                val mode = when (theme) {
                    AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    AppTheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
        setContent {
            val currentTheme by settingsDataStore.themeFlow.collectAsStateWithLifecycle(initialValue = AppTheme.SYSTEM)

            val isDarkTheme = isSystemInDarkTheme()
            PokemonTheme(
                darkTheme = isDarkTheme,
                themeMode = when (currentTheme) {
                    AppTheme.LIGHT -> ThemeMode.LIGHT
                    AppTheme.DARK -> ThemeMode.DARK
                    AppTheme.SYSTEM -> ThemeMode.SYSTEM
                }
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}