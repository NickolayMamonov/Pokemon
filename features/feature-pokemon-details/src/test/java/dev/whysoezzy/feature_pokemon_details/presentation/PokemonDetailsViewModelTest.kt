package dev.whysoezzy.feature_pokemon_details.presentation

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
import org.junit.Assert.assertNotNull
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
    fun `initial state is Loading`() {
        val initialState = viewModel.uiState.value
        assertTrue("Initial state should be Loading", initialState is PokemonDetailsUiState.Loading)
    }

    @Test
    fun `loadPokemon success updates state to Success`() = runTest {
        val testPokemon = createTestPokemon()
        coEvery { getPokemonDetailsUseCase("25") } returns Result.success(testPokemon)

        viewModel.loadPokemon(25)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Success", state is PokemonDetailsUiState.Success)

        val successState = state as PokemonDetailsUiState.Success
        assertEquals("Pokemon should match", testPokemon, successState.pokemon)

        coVerify { getPokemonDetailsUseCase("25") }
    }

    @Test
    fun `loadPokemon failure updates state to Error`() = runTest {
        val errorMessage = "Network error"
        coEvery { getPokemonDetailsUseCase("25") } returns Result.failure(Exception(errorMessage))

        viewModel.loadPokemon(25)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Error", state is PokemonDetailsUiState.Error)

        val errorState = state as PokemonDetailsUiState.Error
        assertEquals("Error message should match", errorMessage, errorState.message)
        
        coVerify { getPokemonDetailsUseCase("25") }
    }

    @Test
    fun `loadPokemon sets Loading state before fetching`() = runTest {
        val testPokemon = createTestPokemon()
        coEvery { getPokemonDetailsUseCase("25") } returns Result.success(testPokemon)

        viewModel.loadPokemon(25)

        // Перед advanceUntilIdle состояние должно быть Loading
        val loadingState = viewModel.uiState.value
        assertTrue(
            "State should be Loading during fetch",
            loadingState is PokemonDetailsUiState.Loading
        )
        
        advanceUntilIdle()
    }

    @Test
    fun `loadPokemon multiple times with different IDs`() = runTest {
        val pokemon1 = createTestPokemon(id = 1, name = "Bulbasaur")
        val pokemon2 = createTestPokemon(id = 2, name = "Ivysaur")

        coEvery { getPokemonDetailsUseCase("1") } returns Result.success(pokemon1)
        coEvery { getPokemonDetailsUseCase("2") } returns Result.success(pokemon2)

        // Загружаем первого покемона
        viewModel.loadPokemon(1)
        advanceUntilIdle()

        var state = viewModel.uiState.value as PokemonDetailsUiState.Success
        assertEquals("First pokemon name", "Bulbasaur", state.pokemon.name)

        // Загружаем второго покемона
        viewModel.loadPokemon(2)
        advanceUntilIdle()

        state = viewModel.uiState.value as PokemonDetailsUiState.Success
        assertEquals("Second pokemon name", "Ivysaur", state.pokemon.name)

        coVerify { getPokemonDetailsUseCase("1") }
        coVerify { getPokemonDetailsUseCase("2") }
    }

    @Test
    fun `loadPokemon with null error message uses default message`() = runTest {
        coEvery { getPokemonDetailsUseCase("25") } returns Result.failure(Exception())

        viewModel.loadPokemon(25)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("State should be Error", state is PokemonDetailsUiState.Error)

        val errorState = state as PokemonDetailsUiState.Error
        assertEquals(
            "Should use default error message",
            "Не удалось загрузить покемона",
            errorState.message
        )
    }

    @Test
    fun `viewModel can be created`() {
        assertNotNull("ViewModel should be created", viewModel)
        assertNotNull("UI state should be available", viewModel.uiState)
    }

    private fun createTestPokemon(
        id: Int = 25,
        name: String = "Pikachu"
    ) = Pokemon(
        id = id,
        name = name,
        height = 4,
        weight = 60,
        imageUrl = "https://example.com/$id.png",
        types = listOf(PokemonType(name = "electric", slot = 1)),
        stats = listOf(
            PokemonStat(name = "hp", baseStat = 35, effort = 0),
            PokemonStat(name = "attack", baseStat = 55, effort = 0),
            PokemonStat(name = "defense", baseStat = 40, effort = 0),
            PokemonStat(name = "special-attack", baseStat = 50, effort = 0),
            PokemonStat(name = "special-defense", baseStat = 50, effort = 0),
            PokemonStat(name = "speed", baseStat = 90, effort = 0),
        )
    )
}
