package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.model.PaginatedData
import dev.whysoezzy.domain.model.PokemonListItem
import dev.whysoezzy.domain.repository.PokemonRepository

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