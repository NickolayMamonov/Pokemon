package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.repository.FavoritesRepository

class GetFavoritePokemonsUseCase(
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke() = favoritesRepository.getFavoritePokemons()

}