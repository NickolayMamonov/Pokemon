package dev.whysoezzy.feature_pokemon_list.presentation

import androidx.compose.runtime.Immutable
import dev.whysoezzy.core_data.state.LoadingState
import dev.whysoezzy.core_data.state.canShowContent
import dev.whysoezzy.core_data.state.error
import dev.whysoezzy.core_data.state.errorMessage
import dev.whysoezzy.core_data.state.hasError
import dev.whysoezzy.core_data.state.isIdle
import dev.whysoezzy.core_data.state.isLoading
import dev.whysoezzy.core_data.state.isLoadingMore
import dev.whysoezzy.core_data.state.isRefreshing
import dev.whysoezzy.core_data.state.isSuccess
import dev.whysoezzy.core_data.state.shouldShowLoadingOnly
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonListItem

//data class PokemonListUiState(
//    val pokemonList: List<PokemonListItem> = emptyList(),
//    val detailedPokemon: Map<String, Pokemon> = emptyMap(),
//    val filteredPokemon: List<Pokemon> = emptyList(),
//    val isLoading: Boolean = false,
//    val isLoadingMore: Boolean = false,
//    val isRefreshing: Boolean = false,
//    val error: String? = null,
//    val availableTypes: Set<String> = emptySet(),
//    val hasNextPage: Boolean = true,
//    val currentPage: Int = 0,
//    val totalCount: Int = 0,
//    val cachedCount: Int = 0
//)

/**
 * UI состояние для экрана списка покемонов
 * Использует LoadingState вместо множества boolean флагов
 */
@Immutable
data class PokemonListUiState(
    val loadingState: LoadingState = LoadingState.Idle,
    val pokemonList: List<PokemonListItem> = emptyList(),
    val filteredPokemon: List<Pokemon> = emptyList(),
    val detailedPokemon: Map<String, Pokemon> = emptyMap(),
    val availableTypes: Set<String> = emptySet(),
    val hasNextPage: Boolean = false,
    val currentPage: Int = 0,
    val totalCount: Int = 0
) {
    // Удобные computed properties для UI
    val isLoading get() = loadingState.isLoading
    val isLoadingMore get() = loadingState.isLoadingMore
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
    val hasData get() = filteredPokemon.isNotEmpty()
    val isEmpty get() = filteredPokemon.isEmpty() && isSuccess
    val hasLoadedDetails get() = detailedPokemon.isNotEmpty()

    // Прогресс загрузки деталей
    val detailsLoadingProgress: Float get() {
        if (pokemonList.isEmpty()) return 0f
        return detailedPokemon.size.toFloat() / pokemonList.size.toFloat()
    }

    // Количество элементов при LoadingMore
    val loadingMoreCurrentItems: Int get() =
        (loadingState as? LoadingState.LoadingMore)?.currentItems ?: 0

    /**
     * Можно ли показать контент (есть данные или загружается больше)
     */
    val canShowContent: Boolean get() =
        loadingState.canShowContent(hasData)

    /**
     * Нужно ли показать пустое состояние
     */
    val shouldShowEmptyState: Boolean get() =
        isEmpty && !isLoading && !hasError

    /**
     * Нужно ли показать только индикатор загрузки
     */
    val shouldShowLoadingOnly: Boolean get() =
        loadingState.shouldShowLoadingOnly(hasData)

    /**
     * Получает покемона по ID из детализированного кэша
     */
    fun getPokemonById(id: String): Pokemon? = detailedPokemon[id]

    /**
     * Проверяет загружены ли детали для покемона
     */
    fun hasDetailsFor(pokemonId: String): Boolean = detailedPokemon.containsKey(pokemonId)
}