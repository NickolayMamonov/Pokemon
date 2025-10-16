package dev.whysoezzy.feature_settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.domain.model.AppTheme
import dev.whysoezzy.domain.usecase.ClearCacheUseCase
import dev.whysoezzy.domain.usecase.GetSettingsUseCase
import dev.whysoezzy.domain.usecase.SaveThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

sealed interface SettingsEvent {
    data class ShowMessage(val message: String) : SettingsEvent
    data class CacheClearSuccess(val message: String) : SettingsEvent
    data class CacheClearError(val message: String) : SettingsEvent
}

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val clearCacheUseCase: ClearCacheUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SettingsUIState>(SettingsUIState.Loading)
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    private val _events = MutableStateFlow<SettingsEvent?>(null)
    val events: StateFlow<SettingsEvent?> = _events.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            getSettingsUseCase()
                .catch { error ->
                    Timber.e(error, "Error loading settings")
                    _uiState.value = SettingsUIState.Error(
                        error.message ?: "Ошибка загрузки настроек"
                    )
                }
                .collect { settings ->
                    _uiState.value = SettingsUIState.Success(settings)
                }
        }
    }

    fun saveTheme(theme: AppTheme) {
        viewModelScope.launch {
            try {
                saveThemeUseCase(theme)
                Timber.d("Theme saved: $theme")
            } catch (e: Exception) {
                Timber.e(e, "Error saving theme: $theme")
                _events.value = SettingsEvent.ShowMessage("Ошибка сохранения темы")
            }
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            clearCacheUseCase().onSuccess {
                Timber.i("Cache cleared successfully")
                _events.value = SettingsEvent.CacheClearSuccess("Кэш успешно очищен")
                loadSettings()
            }
                .onFailure { error ->
                    Timber.e(error, "Error clearing cache")
                    _events.value = SettingsEvent.CacheClearError(
                        error.message ?: "Ошибка при очистке кэша"
                    )
                }
        }
    }

    fun eventHandled() {
        _events.value = null
    }
}