package dev.whysoezzy.pokemon.domain.usecase

import dev.whysoezzy.pokemon.domain.model.Pokemon
import dev.whysoezzy.pokemon.domain.repository.PokemonRepository

class GetPokemonDetailsUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: String): Result<Pokemon> {
        return repository.getPokemonDetails(id)
    }
}