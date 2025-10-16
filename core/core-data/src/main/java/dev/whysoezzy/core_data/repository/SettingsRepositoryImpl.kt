package dev.whysoezzy.core_data.repository

import android.content.Context
import dev.whysoezzy.core_data.datastore.SettingsDataStore
import dev.whysoezzy.core_database.dao.PokemonDao
import dev.whysoezzy.domain.model.AppTheme
import dev.whysoezzy.domain.model.Settings
import dev.whysoezzy.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class SettingsRepositoryImpl(
    private val context: Context,
    private val settingsDataStore: SettingsDataStore,
    private val pokemonDao: PokemonDao
) : SettingsRepository {
    override fun getSettings(): Flow<Settings> {
        return settingsDataStore.themeFlow.map { theme ->
            Settings(
                theme = theme,
                cacheSize = getCacheSize()
            )
        }
    }

    override suspend fun saveTheme(theme: AppTheme) {
        Timber.d("Saving theme: $theme")
        settingsDataStore.saveTheme(theme)
    }

    override suspend fun clearCache(): Result<Unit> {
        return try {
            Timber.d("Clearing cache...")

            pokemonDao.clearAllPokemon()
            pokemonDao.clearPokemonList()

            context.cacheDir.deleteRecursively()
            Timber.i("Cache cleared successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error clearing cache")
            Result.failure(e)
        }
    }

    override suspend fun getCacheSize(): Long {
        return try {
            val dbSize = getDatabaseSize()
            val imageCacheSize = getImageCacheSize()
            dbSize + imageCacheSize
        } catch (e: Exception) {
            Timber.e(e, "Error calculating cache size")
            0L
        }
    }

    private fun getDatabaseSize(): Long {
        val dbPath = context.getDatabasePath("pokemon_database")
        return if (dbPath.exists()) dbPath.length() else 0L
    }

    private fun getImageCacheSize(): Long {
        return context.cacheDir.walkTopDown()
            .filter { it.isFile }
            .map { it.length() }
            .sum()
    }

}