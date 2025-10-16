package dev.whysoezzy.core_data.repository

import dev.whysoezzy.core_data.mappers.toDomainModel
import dev.whysoezzy.core_database.dao.PokemonDao
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class FavoritesRepositoryImpl(
    private val pokemonDao: PokemonDao
) : FavoritesRepository {
    override fun getFavoritePokemons(): Flow<List<Pokemon>> {
        return pokemonDao.getFavoritePokemon()
            .map { entities ->
                entities.map { it.toDomainModel() }
            }
    }

    override suspend fun getFavoritesCount(): Int {
        return pokemonDao.getFavoritesCount()
    }

    override suspend fun toggleFavorite(pokemonId: Int): Result<Boolean> {
        return try {
            val currentStatus = pokemonDao.isFavorite(pokemonId) ?: false
            val newStatus = !currentStatus
            pokemonDao.updateFavoriteStatus(pokemonId, newStatus)
            Timber.d("Toggled favorite for pokemon $pokemonId: $currentStatus -> $newStatus")
            Result.success(newStatus)
        } catch (e: Exception) {
            Timber.e(e, "Error toggling favorite for pokemon $pokemonId")
            Result.failure(e)
        }
    }

    override suspend fun isFavorite(pokemonId: Int): Boolean {
        return pokemonDao.isFavorite(pokemonId) ?: false
    }
}