package dev.whysoezzy.pokemon.presentation.list


import dev.whysoezzy.domain.model.PaginatedData
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.model.PokemonListItem
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import dev.whysoezzy.domain.usecase.FilterPokemonUseCase
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import dev.whysoezzy.domain.usecase.GetPokemonListUseCase
import dev.whysoezzy.feature_pokemon_list.presentation.PokemonListViewModel

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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private lateinit var viewModel: PokemonListViewModel
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase
    private lateinit var getPokemonDetailsUseCase: GetPokemonDetailsUseCase
    private lateinit var filterPokemonUseCase: FilterPokemonUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getPokemonListUseCase = mockk()
        getPokemonDetailsUseCase = mockk()
        filterPokemonUseCase = mockk()

        coEvery { getPokemonListUseCase(any(), any()) } returns Result.success(
            PaginatedData(
                items = listOf(
                    PokemonListItem(id = "1", name = "bulbasaur", url = "url1"),
                    PokemonListItem(id = "2", name = "ivysaur", url = "url2")
                ),
                totalCount = 2,
                hasNextPage = false,
                currentPage = 1
            )
        )

        coEvery { getPokemonDetailsUseCase("1") } returns Result.success(
            createTestPokemon(
                1,
                "Bulbasaur"
            )
        )
        coEvery { getPokemonDetailsUseCase("2") } returns Result.success(
            createTestPokemon(
                2,
                "Ivysaur"
            )
        )

        coEvery { filterPokemonUseCase(any(), any()) } returns emptyList()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMorePokemon when hasNextPage false does nothing`() = runTest {

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.loadMorePokemon()
        advanceUntilIdle()

        coVerify(exactly = 1) { getPokemonListUseCase(any(), any()) }
    }

    @Test
    fun `loadMorePokemon when hasNextPage true loads next page`() = runTest {
        coEvery { getPokemonListUseCase(20, 0) } returns Result.success(
            PaginatedData(
                items = listOf(PokemonListItem(id = "1", name = "bulbasaur", url = "url1")),
                totalCount = 50,
                hasNextPage = true,
                currentPage = 1
            )
        )
        coEvery { getPokemonListUseCase(20, 1) } returns Result.success(
            PaginatedData(
                items = listOf(PokemonListItem(id = "2", name = "ivysaur", url = "url2")),
                totalCount = 50,
                hasNextPage = true,
                currentPage = 2
            )
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.loadMorePokemon()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.pokemonList.size)
        assertEquals(2, state.currentPage)
        assertTrue(state.hasNextPage)
        assertFalse(state.isLoadingMore)
    }


    @Test
    fun `updateFilter updates filter state`() = runTest {
        viewModel = createViewModel()
        val newFilter = PokemonFilter(
            searchQuery = "test",
            selectedTypes = setOf("fire", "water")
        )

        viewModel.updateFilter(newFilter)

        val filter = viewModel.filter.value
        assertEquals("test", filter.searchQuery)
        assertEquals(setOf("fire", "water"), filter.selectedTypes)
    }

    @Test
    fun `clearFilters resets filter to default`() = runTest {
        viewModel = createViewModel()
        viewModel.updateFilter(PokemonFilter(searchQuery = "test", selectedTypes = setOf("fire")))

        viewModel.clearFilters()

        val filter = viewModel.filter.value
        assertEquals(PokemonFilter(), filter)
    }


    private fun createViewModel(): PokemonListViewModel {
        return PokemonListViewModel(
            getPokemonListUseCase = getPokemonListUseCase,
            getPokemonDetailsUseCase = getPokemonDetailsUseCase,
            filterPokemonUseCase = filterPokemonUseCase
        )
    }

    private fun createTestPokemon(id: Int, name: String) = Pokemon(
        id = id,
        name = name,
        height = 10,
        weight = 100,
        imageUrl = "https://example.com/$id.png",
        types = listOf(PokemonType(name = "grass", slot = 1)),
        stats = listOf(
            PokemonStat(name = "hp", baseStat = 45, effort = 0),
            PokemonStat(name = "attack", baseStat = 49, effort = 0)
        )
    )
}
