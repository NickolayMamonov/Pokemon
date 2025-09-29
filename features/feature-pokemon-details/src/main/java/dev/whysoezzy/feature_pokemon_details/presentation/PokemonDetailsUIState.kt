package dev.whysoezzy.feature_pokemon_details.presentation

import androidx.compose.runtime.Immutable
import dev.whysoezzy.core_data.state.LoadingState
import dev.whysoezzy.core_data.state.error
import dev.whysoezzy.core_data.state.errorMessage
import dev.whysoezzy.core_data.state.hasError
import dev.whysoezzy.core_data.state.isIdle
import dev.whysoezzy.core_data.state.isLoading
import dev.whysoezzy.core_data.state.isRefreshing
import dev.whysoezzy.core_data.state.isSuccess
import dev.whysoezzy.domain.model.Pokemon

/**
 * UI состояние для экрана деталей покемона
 * Использует LoadingState для управления состоянием загрузки
 */
@Immutable
data class PokemonDetailsUiState(
    val loadingState: LoadingState = LoadingState.Idle,
    val pokemon: Pokemon? = null,
    val isImageLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val showExtendedStats: Boolean = false,
) {
    // Удобные computed properties для UI
    val isLoading get() = loadingState.isLoading
    val isRefreshing get() = loadingState.isRefreshing
    val hasError get() = loadingState.hasError
    val isSuccess get() = loadingState.isSuccess
    val isIdle get() = loadingState.isIdle

    // Получение ошибки
    val error: LoadingState.Error? get() = loadingState.error
    val errorMessage: String? get() = loadingState.errorMessage
    val canRetry: Boolean get() = error?.isRetry == true
    val hasExistingData: Boolean get() = error?.hasExistingData == true

    // Состояние данных
    val hasData get() = pokemon != null
    val hasValidStats get() = pokemon?.stats?.any { it.baseStat > 0 } == true

    /**
     * Нужно ли показать только индикатор загрузки
     * Показываем loading только когда нет данных и идет загрузка
     */
    val shouldShowLoadingOnly: Boolean get() = !hasData && isLoading

    /**
     * Получает основной тип покемона для стилизации
     */
    val primaryType: String get() = pokemon?.types?.firstOrNull()?.name ?: "normal"

    /**
     * Проверяет, готов ли покемон для отображения
     */
    val isPokemonReady: Boolean get() = pokemon != null && hasValidStats
}
