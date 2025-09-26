package dev.whysoezzy.pokemon.domain.repository

import dev.whysoezzy.pokemon.domain.model.PaginatedData
import dev.whysoezzy.pokemon.domain.model.Pokemon
import dev.whysoezzy.pokemon.domain.model.PokemonListItem

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<PaginatedData<PokemonListItem>>
    suspend fun getPokemonDetails(id: String): Result<Pokemon>

    suspend fun clearCache()
    suspend fun getCachedPokemonCount(): Int
}