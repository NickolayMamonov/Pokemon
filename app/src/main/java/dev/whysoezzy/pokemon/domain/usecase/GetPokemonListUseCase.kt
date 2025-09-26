package dev.whysoezzy.pokemon.domain.usecase

import dev.whysoezzy.pokemon.domain.model.PaginatedData
import dev.whysoezzy.pokemon.domain.model.PokemonListItem
import dev.whysoezzy.pokemon.domain.repository.PokemonRepository

class GetPokemonListUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(
        limit: Int = 20,
        offset: Int = 0
    ): Result<PaginatedData<PokemonListItem>> {
        return repository.getPokemonList(limit, offset)
    }
}