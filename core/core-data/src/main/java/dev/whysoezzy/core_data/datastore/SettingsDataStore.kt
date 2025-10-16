package dev.whysoezzy.core_data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.whysoezzy.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    private object PreferencesKeys {
        val THEME = stringPreferencesKey("app_theme")
    }

    val themeFlow: Flow<AppTheme> = context.settingsDataStore.data
        .map { preferences ->
            val themeName = preferences[PreferencesKeys.THEME] ?: AppTheme.SYSTEM.name
            AppTheme.valueOf(themeName)
        }

    suspend fun saveTheme(theme: AppTheme) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme.name
        }
    }
}