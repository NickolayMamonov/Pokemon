package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.repository.FavoritesRepository

class IsFavoriteUseCase(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(pokemonId: Int): Boolean {
        return favoritesRepository.isFavorite(pokemonId)
    }
}