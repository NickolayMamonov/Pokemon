package dev.whysoezzy.feature_settings.presentation

import dev.whysoezzy.domain.model.Settings

sealed interface SettingsUIState {
    data object Loading : SettingsUIState
    data class Success(val settings: Settings) : SettingsUIState
    data class Error(val message: String) : SettingsUIState
}