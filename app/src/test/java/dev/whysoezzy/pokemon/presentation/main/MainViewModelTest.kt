package dev.whysoezzy.pokemon.presentation.main

import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is PokemonList screen`() =
        runTest {
            val initialState = viewModel.uiState.value

            assertTrue(initialState.currentScreen is Screen.PokemonList)
            assertNull(initialState.selectedPokemon)
            assertFalse(initialState.isNavigating)
            assertFalse(initialState.canNavigateBack)
        }

    @Test
    fun `navigateBack from list screen returns false`() =
        runTest {
            val handled = viewModel.navigateBack()

            assertFalse(handled)
            val state = viewModel.uiState.value
            assertTrue(state.currentScreen is Screen.PokemonList)
            assertFalse(state.canNavigateBack)
        }

    @Test
    fun `canNavigateBack returns correct value`() =
        runTest {
            assertFalse(viewModel.canNavigateBack())

            val testPokemon = createTestPokemon(1, "Bulbasaur")
            viewModel.navigateToPokemonDetails(testPokemon)
            assertTrue(viewModel.canNavigateBack())

            viewModel.navigateBack()
            assertFalse(viewModel.canNavigateBack())
        }

    private fun createTestPokemon(
        id: Int,
        name: String,
    ) = Pokemon(
        id = id,
        name = name,
        height = 10,
        weight = 100,
        imageUrl = "https://example.com/$id.png",
        types = listOf(PokemonType(name = "grass", slot = 1)),
        stats =
            listOf(
                PokemonStat(name = "hp", baseStat = 45, effort = 0),
                PokemonStat(name = "attack", baseStat = 49, effort = 0),
            ),
    )
}
