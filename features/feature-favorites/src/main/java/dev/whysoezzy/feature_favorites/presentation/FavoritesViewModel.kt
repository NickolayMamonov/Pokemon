package dev.whysoezzy.feature_favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.domain.usecase.GetFavoritePokemonsUseCase
import dev.whysoezzy.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber

class FavoritesViewModel(
    private val getFavoritePokemonsUseCase: GetFavoritePokemonsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<FavoritesUIState>(FavoritesUIState.Loading)
    val uiState: StateFlow<FavoritesUIState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesUIState.Loading

            getFavoritePokemonsUseCase()  // ← Возвращает Flow<List<Pokemon>>
                .catch { error ->  // ← .catch() работает для Flow
                    Timber.e(error, "Error loading favorites")
                    _uiState.value = FavoritesUIState.Error(
                        error.message ?: "Ошибка загрузки избранных покемонов"
                    )
                }
                .collect { favorites ->  // ← .collect() также для Flow
                    _uiState.value = if (favorites.isEmpty()) {
                        FavoritesUIState.Empty
                    } else {
                        FavoritesUIState.Success(favorites)
                    }
                }
        }
    }

    fun toggleFavorite(pokemonId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(pokemonId)
                .onSuccess {
                    Timber.d("Successfully toggled favorite for pokemon $pokemonId")
                }
                .onFailure { error ->
                    Timber.e(error, "Error toggling favorite for pokemon $pokemonId")
                }
        }
    }

    fun retry() {
        loadFavorites()
    }
}