package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.repository.PokemonRepository

class GetPokemonDetailsUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: String): Result<Pokemon> {
        return repository.getPokemonDetails(id)
    }
}