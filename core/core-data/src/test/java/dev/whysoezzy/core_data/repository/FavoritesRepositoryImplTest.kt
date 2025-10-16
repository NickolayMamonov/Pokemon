package dev.whysoezzy.core_data.repository

import app.cash.turbine.test
import dev.whysoezzy.core_database.dao.PokemonDao
import dev.whysoezzy.core_database.entity.PokemonEntity
import dev.whysoezzy.core_database.entity.PokemonStatEntity
import dev.whysoezzy.core_database.entity.PokemonTypeEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoritesRepositoryImplTest {

    private lateinit var pokemonDao: PokemonDao
    private lateinit var repository: FavoritesRepositoryImpl

    private val testPokemonEntity = PokemonEntity(
        id = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        imageUrl = "https://example.com/1.png",
        types = listOf(PokemonTypeEntity("grass", 1)),
        stats = listOf(PokemonStatEntity("hp", 45, 0)),
        isFavorite = true
    )

    @Before
    fun setup() {
        pokemonDao = mockk()
        repository = FavoritesRepositoryImpl(pokemonDao)
    }

    @Test
    fun `getFavoritePokemons returns flow of favorite pokemon`() = runTest {
        // Given
        val favoriteEntities = listOf(testPokemonEntity)
        coEvery { pokemonDao.getFavoritePokemon() } returns flowOf(favoriteEntities)

        // When & Then
        repository.getFavoritePokemons().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("bulbasaur", result[0].name)
            assertTrue(result[0].isFavorite)
            awaitComplete()
        }
    }

    @Test
    fun `getFavoritesCount returns correct count`() = runTest {
        // Given
        coEvery { pokemonDao.getFavoritesCount() } returns 5

        // When
        val count = repository.getFavoritesCount()

        // Then
        assertEquals(5, count)
        coVerify { pokemonDao.getFavoritesCount() }
    }

    @Test
    fun `toggleFavorite changes status from false to true`() = runTest {
        // Given
        val pokemonId = 1
        coEvery { pokemonDao.isFavorite(pokemonId) } returns false
        coEvery { pokemonDao.updateFavoriteStatus(pokemonId, true) } returns Unit

        // When
        val result = repository.toggleFavorite(pokemonId)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull() == true)
        coVerify { pokemonDao.updateFavoriteStatus(pokemonId, true) }
    }

    @Test
    fun `toggleFavorite changes status from true to false`() = runTest {
        // Given
        val pokemonId = 1
        coEvery { pokemonDao.isFavorite(pokemonId) } returns true
        coEvery { pokemonDao.updateFavoriteStatus(pokemonId, false) } returns Unit

        // When
        val result = repository.toggleFavorite(pokemonId)

        // Then
        assertTrue(result.isSuccess)
        assertFalse(result.getOrNull() == true)
        coVerify { pokemonDao.updateFavoriteStatus(pokemonId, false) }
    }

    @Test
    fun `toggleFavorite returns failure on exception`() = runTest {
        // Given
        val pokemonId = 1
        val exception = RuntimeException("Database error")
        coEvery { pokemonDao.isFavorite(pokemonId) } throws exception

        // When
        val result = repository.toggleFavorite(pokemonId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `isFavorite returns true for favorite pokemon`() = runTest {
        // Given
        val pokemonId = 1
        coEvery { pokemonDao.isFavorite(pokemonId) } returns true

        // When
        val isFavorite = repository.isFavorite(pokemonId)

        // Then
        assertTrue(isFavorite)
    }

    @Test
    fun `isFavorite returns false for non-favorite pokemon`() = runTest {
        // Given
        val pokemonId = 1
        coEvery { pokemonDao.isFavorite(pokemonId) } returns false

        // When
        val isFavorite = repository.isFavorite(pokemonId)

        // Then
        assertFalse(isFavorite)
    }

    @Test
    fun `isFavorite returns false when pokemon not found`() = runTest {
        // Given
        val pokemonId = 999
        coEvery { pokemonDao.isFavorite(pokemonId) } returns null

        // When
        val isFavorite = repository.isFavorite(pokemonId)

        // Then
        assertFalse(isFavorite)
    }
}