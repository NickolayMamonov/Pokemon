package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.repository.FavoritesRepository

class ToggleFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(pokemonId: Int): Result<Boolean> {
        return favoritesRepository.toggleFavorite(pokemonId)
    }
}