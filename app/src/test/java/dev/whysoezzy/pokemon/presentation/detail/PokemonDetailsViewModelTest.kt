package dev.whysoezzy.pokemon.presentation.detail


import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import dev.whysoezzy.feature_pokemon_details.presentation.PokemonDetailsViewModel
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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {

        viewModel = createViewModel()

        val initialState = viewModel.uiState.value
        assertFalse(initialState.isLoading)
        assertFalse(initialState.isImageLoading)
        assertFalse(initialState.showExtendedStats)
        assertNull(initialState.error)
        assertNull(initialState.pokemon)
    }

    @Test
    fun `loadPokemonDetails with valid stats updates state correctly`() = runTest {
        val testPokemon = createTestPokemon()
        viewModel = createViewModel()

        viewModel.loadPokemonDetails(testPokemon)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(testPokemon, state.pokemon)
        coVerify(exactly = 0) { getPokemonDetailsUseCase(any()) }
    }

    @Test
    fun `loadPokemonDetails with invalid stats triggers reload`() = runTest {
        val testPokemonWithInvalidStats = createTestPokemon().copy(
            stats = listOf(
                PokemonStat(name = "hp", baseStat = 0, effort = 0),
                PokemonStat(name = "attack", baseStat = 0, effort = 0)
            )
        )
        val validPokemon = createTestPokemon()
        coEvery { getPokemonDetailsUseCase("25") } returns Result.success(validPokemon)
        viewModel = createViewModel()

        viewModel.loadPokemonDetails(testPokemonWithInvalidStats)
        advanceUntilIdle()

        coVerify { getPokemonDetailsUseCase("25") }
    }

    @Test
    fun `loadPokemonById success updates state correctly`() = runTest {
        val testPokemon = createTestPokemon()
        coEvery { getPokemonDetailsUseCase("25") } returns Result.success(testPokemon)
        viewModel = createViewModel()

        viewModel.loadPokemonById("25")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(testPokemon, state.pokemon)
        coVerify { getPokemonDetailsUseCase("25") }
    }

    @Test
    fun `loadPokemonById failure updates error state`() = runTest {
        coEvery { getPokemonDetailsUseCase("25") } returns Result.failure(Exception("Network error"))
        viewModel = createViewModel()

        viewModel.loadPokemonById("25")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
        assertNull(state.pokemon)
    }

    @Test
    fun `onImageLoadingChanged updates image loading state`() = runTest {
        viewModel = createViewModel()

        viewModel.onImageLoadingChanged(true)

        assertTrue(viewModel.uiState.value.isImageLoading)

        viewModel.onImageLoadingChanged(false)

        assertFalse(viewModel.uiState.value.isImageLoading)
    }

    @Test
    fun `clearError clears error state`() = runTest {
        coEvery { getPokemonDetailsUseCase("25") } returns Result.failure(Exception("Network error"))
        viewModel = createViewModel()
        viewModel.loadPokemonById("25")
        advanceUntilIdle()

        assertEquals("Network error", viewModel.uiState.value.error)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `toggleExtendedStats changes extended stats state`() = runTest {
        viewModel = createViewModel()
        assertFalse(viewModel.uiState.value.showExtendedStats)

        viewModel.toggleExtendedStats()

        assertTrue(viewModel.uiState.value.showExtendedStats)

        viewModel.toggleExtendedStats()

        assertFalse(viewModel.uiState.value.showExtendedStats)
    }

    private fun createViewModel(): PokemonDetailsViewModel {
        return PokemonDetailsViewModel(getPokemonDetailsUseCase)
    }

    private fun createTestPokemon() = Pokemon(
        id = 25,
        name = "Pikachu",
        height = 4,
        weight = 60,
        imageUrl = "https://example.com/25.png",
        types = listOf(PokemonType(name = "electric", slot = 1)),
        stats = listOf(
            PokemonStat(name = "hp", baseStat = 35, effort = 0),
            PokemonStat(name = "attack", baseStat = 55, effort = 0),
            PokemonStat(name = "defense", baseStat = 40, effort = 0),
            PokemonStat(name = "speed", baseStat = 90, effort = 0)
        )
    )
}
