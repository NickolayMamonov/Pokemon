package dev.whysoezzy.feature_pokemon_details.presentation

import dev.whysoezzy.core_data.state.LoadingState
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailsViewModelTest {
    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var getPokemonDetailsUseCase: GetPokemonDetailsUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getPokemonDetailsUseCase = mockk()
        viewModel = PokemonDetailsViewModel(getPokemonDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val initialState = viewModel.state.value
        
        assertTrue(initialState.isIdle)
        assertFalse(initialState.isLoading)
        assertFalse(initialState.isImageLoading)
        assertFalse(initialState.showExtendedStats)
        assertNull(initialState.pokemon)
        assertFalse(initialState.hasError)
    }

    @Test
    fun `LoadPokemonDetails with valid stats updates state correctly`() = runTest {
        val testPokemon = createTestPokemon(withValidStats = true)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonDetails(testPokemon))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isSuccess)
        assertEquals(testPokemon, state.pokemon)
        assertTrue(state.hasValidStats)
        coVerify(exactly = 0) { getPokemonDetailsUseCase(any()) }
    }

    @Test
    fun `LoadPokemonDetails with invalid stats triggers reload`() = runTest {
        val testPokemonWithInvalidStats = createTestPokemon(withValidStats = false)
        val validPokemon = createTestPokemon(withValidStats = true)
        coEvery { getPokemonDetailsUseCase("25") } returns Result.success(validPokemon)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonDetails(testPokemonWithInvalidStats))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isSuccess)
        assertEquals(validPokemon, state.pokemon)
        coVerify { getPokemonDetailsUseCase("25") }
    }

    @Test
    fun `LoadPokemonById success updates state correctly`() = runTest {
        val testPokemon = createTestPokemon(withValidStats = true)
        coEvery { getPokemonDetailsUseCase("25") } returns Result.success(testPokemon)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonById("25"))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isSuccess)
        assertEquals(testPokemon, state.pokemon)
        coVerify { getPokemonDetailsUseCase("25") }
    }

    @Test
    fun `LoadPokemonById failure updates error state`() = runTest {
        val exception = Exception("Network error")
        coEvery { getPokemonDetailsUseCase("25") } returns Result.failure(exception)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonById("25"))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.hasError)
        assertEquals("Network error", state.errorMessage)
        assertNull(state.pokemon)
        assertTrue(state.canRetry)
    }

    @Test
    fun `ImageLoadingChanged updates image loading state`() {
        viewModel.onIntent(PokemonDetailsViewModel.Intent.ImageLoadingChanged(true))
        assertTrue(viewModel.state.value.isImageLoading)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.ImageLoadingChanged(false))
        assertFalse(viewModel.state.value.isImageLoading)
    }

    @Test
    fun `ClearError clears error state when pokemon exists`() = runTest {
        // Сначала загружаем покемона
        val testPokemon = createTestPokemon(withValidStats = true)
        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonDetails(testPokemon))
        advanceUntilIdle()

        // Создаем ошибку через неудачную загрузку
        coEvery { getPokemonDetailsUseCase("999") } returns Result.failure(Exception("Not found"))
        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonById("999"))
        advanceUntilIdle()

        assertTrue(viewModel.state.value.hasError)

        // Очищаем ошибку
        viewModel.onIntent(PokemonDetailsViewModel.Intent.ClearError)
        
        val state = viewModel.state.value
        assertTrue(state.isSuccess)
        assertFalse(state.hasError)
    }

    @Test
    fun `ToggleExtendedStats changes extended stats state`() {
        assertFalse(viewModel.state.value.showExtendedStats)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.ToggleExtendedStats(true))
        assertTrue(viewModel.state.value.showExtendedStats)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.ToggleExtendedStats(false))
        assertFalse(viewModel.state.value.showExtendedStats)
    }

    @Test
    fun `Refresh with existing pokemon reloads data`() = runTest {
        // Сначала загружаем покемона
        val initialPokemon = createTestPokemon(withValidStats = true)
        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonDetails(initialPokemon))
        advanceUntilIdle()

        // Настраиваем мок для refresh
        val refreshedPokemon = initialPokemon.copy(name = "Refreshed Pikachu")
        coEvery { getPokemonDetailsUseCase("25") } returns Result.success(refreshedPokemon)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.Refresh)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isSuccess)
        assertEquals("Refreshed Pikachu", state.pokemon?.name)
        coVerify { getPokemonDetailsUseCase("25") }
    }

    @Test
    fun `Retry calls loadPokemonById with existing pokemon id`() = runTest {
        // Сначала загружаем покемона
        val testPokemon = createTestPokemon(withValidStats = true)
        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonDetails(testPokemon))
        advanceUntilIdle()

        // Настраиваем мок для retry
        coEvery { getPokemonDetailsUseCase("25") } returns Result.success(testPokemon)

        viewModel.onIntent(PokemonDetailsViewModel.Intent.Retry)
        advanceUntilIdle()

        coVerify { getPokemonDetailsUseCase("25") }
    }

    private fun createTestPokemon(withValidStats: Boolean) = Pokemon(
        id = 25,
        name = "Pikachu",
        height = 4,
        weight = 60,
        imageUrl = "https://example.com/25.png",
        types = listOf(PokemonType(name = "electric", slot = 1)),
        stats = if (withValidStats) {
            listOf(
                PokemonStat(name = "hp", baseStat = 35, effort = 0),
                PokemonStat(name = "attack", baseStat = 55, effort = 0),
                PokemonStat(name = "defense", baseStat = 40, effort = 0),
                PokemonStat(name = "speed", baseStat = 90, effort = 0),
            )
        } else {
            listOf(
                PokemonStat(name = "hp", baseStat = 0, effort = 0),
                PokemonStat(name = "attack", baseStat = 0, effort = 0),
                PokemonStat(name = "defense", baseStat = 0, effort = 0),
                PokemonStat(name = "speed", baseStat = 0, effort = 0),
            )
        }
    )
}
