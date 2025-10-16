package dev.whysoezzy.domain.repository

import dev.whysoezzy.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoritePokemons(): Flow<List<Pokemon>>

    suspend fun getFavoritesCount(): Int

    suspend fun toggleFavorite(pokemonId: Int): Result<Boolean>

    suspend fun isFavorite(pokemonId: Int): Boolean
}