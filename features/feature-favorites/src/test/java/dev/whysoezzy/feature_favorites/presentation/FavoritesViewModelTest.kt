package dev.whysoezzy.feature_favorites.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import dev.whysoezzy.domain.usecase.GetFavoritePokemonsUseCase
import dev.whysoezzy.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getFavoritePokemonsUseCase: GetFavoritePokemonsUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var viewModel: FavoritesViewModel

    private val testPokemon = Pokemon(
        id = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        imageUrl = "https://example.com/1.png",
        types = listOf(PokemonType("grass", 1)),
        stats = listOf(PokemonStat("hp", 45, 0)),
        isFavorite = true
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getFavoritePokemonsUseCase = mockk()
        toggleFavoriteUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads favorites and emits success state`() = runTest {
        // Given
        val favorites = listOf(testPokemon)
        coEvery { getFavoritePokemonsUseCase() } returns flowOf(favorites)

        // When
        viewModel = FavoritesViewModel(getFavoritePokemonsUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is FavoritesUIState.Success)
            assertEquals(1, (state as FavoritesUIState.Success).favorites.size)
        }
    }

    @Test
    fun `init with empty favorites emits empty state`() = runTest {
        // Given
        coEvery { getFavoritePokemonsUseCase() } returns flowOf(emptyList())

        // When
        viewModel = FavoritesViewModel(getFavoritePokemonsUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is FavoritesUIState.Empty)
        }
    }

    @Test
    fun `init with error emits error state`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { getFavoritePokemonsUseCase() } returns kotlinx.coroutines.flow.flow {
            throw RuntimeException(errorMessage)
        }

        // When
        viewModel = FavoritesViewModel(getFavoritePokemonsUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is FavoritesUIState.Error)
            assertEquals(errorMessage, (state as FavoritesUIState.Error).message)
        }
    }

    @Test
    fun `toggleFavorite calls use case`() = runTest {
        // Given
        val pokemonId = 1
        coEvery { getFavoritePokemonsUseCase() } returns flowOf(listOf(testPokemon))
        coEvery { toggleFavoriteUseCase(pokemonId) } returns Result.success(false)

        viewModel = FavoritesViewModel(getFavoritePokemonsUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.toggleFavorite(pokemonId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { toggleFavoriteUseCase(pokemonId) }
    }

}