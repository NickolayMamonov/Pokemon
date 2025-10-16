package dev.whysoezzy.feature_favorites.presentation

import dev.whysoezzy.domain.model.Pokemon

sealed interface FavoritesUIState {
    data object Loading : FavoritesUIState
    data class Success(val favorites: List<Pokemon>) : FavoritesUIState
    data class Error(val message: String) : FavoritesUIState
    data object Empty : FavoritesUIState
}