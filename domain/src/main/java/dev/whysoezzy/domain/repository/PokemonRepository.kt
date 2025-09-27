package dev.whysoezzy.domain.repository

import dev.whysoezzy.domain.model.PaginatedData
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonListItem

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Result<PaginatedData<PokemonListItem>>
    suspend fun getPokemonDetails(id: String): Result<Pokemon>
    suspend fun clearCache()
    suspend fun getCachedPokemonCount(): Int
}