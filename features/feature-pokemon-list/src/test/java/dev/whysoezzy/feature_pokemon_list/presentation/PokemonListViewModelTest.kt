package dev.whysoezzy.feature_pokemon_list.presentation

import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.repository.PokemonRepository
import dev.whysoezzy.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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
class PokemonListViewModelTest {
    private lateinit var viewModel: PokemonViewModel
    private lateinit var repository: PokemonRepository
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        toggleFavoriteUseCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test ViewModel creation`() = runTest {
        viewModel = createViewModel()

        assertNotNull("ViewModel should be created", viewModel)
        assertNotNull("Filter flow should be available", viewModel.filter)
        assertNotNull("Available types flow should be available", viewModel.availableTypes)
        assertNotNull("Paging flow should be available", viewModel.pokemonPagingFlow)
    }

    @Test
    fun `test initial filter state`() = runTest {
        viewModel = createViewModel()

        val initialFilter = viewModel.filter.value
        assertNotNull("Initial filter should not be null", initialFilter)
        assertEquals("Search query should be empty", "", initialFilter.searchQuery)
        assertTrue("Selected types should be empty", initialFilter.selectedTypes.isEmpty())
        assertTrue("Should be ascending by default", initialFilter.isAscending)
    }

    @Test
    fun `test update search query`() = runTest {
        viewModel = createViewModel()

        viewModel.updateSearchQuery("pikachu")
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedFilter = viewModel.filter.value
        assertEquals("Search query should be updated", "pikachu", updatedFilter.searchQuery)
    }

    @Test
    fun `test update filter`() = runTest {
        viewModel = createViewModel()

        val newFilter = PokemonFilter(
            searchQuery = "charizard",
            selectedTypes = setOf("fire", "flying"),
            isAscending = false
        )

        viewModel.updateFilter(newFilter)
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedFilter = viewModel.filter.value
        assertEquals("Filter should be updated", newFilter, updatedFilter)
        assertEquals("Search query should match", "charizard", updatedFilter.searchQuery)
        assertEquals(
            "Selected types should match",
            setOf("fire", "flying"),
            updatedFilter.selectedTypes
        )
        assertEquals("Ascending should match", false, updatedFilter.isAscending)
    }

    @Test
    fun `test clear filters`() = runTest {
        viewModel = createViewModel()

        // Сначала устанавливаем фильтры
        val filterWithData = PokemonFilter(
            searchQuery = "test",
            selectedTypes = setOf("fire"),
            isAscending = false
        )
        viewModel.updateFilter(filterWithData)
        testDispatcher.scheduler.advanceUntilIdle()

        // Теперь очищаем
        viewModel.clearFilters()
        testDispatcher.scheduler.advanceUntilIdle()

        val clearedFilter = viewModel.filter.value
        assertEquals("Search query should be empty", "", clearedFilter.searchQuery)
        assertTrue("Selected types should be empty", clearedFilter.selectedTypes.isEmpty())
        assertTrue("Should be ascending", clearedFilter.isAscending)
    }

    @Test
    fun `test update available types`() = runTest {
        viewModel = createViewModel()

        val types = setOf("fire", "water", "grass")
        viewModel.updateAvailableTypes(types)
        testDispatcher.scheduler.advanceUntilIdle()

        val availableTypes = viewModel.availableTypes.value
        assertEquals("Available types should be updated", types, availableTypes)
    }

    @Test
    fun `test toggleFavorite calls use case`() = runTest {
        val pokemonId = 1
        coEvery { toggleFavoriteUseCase(pokemonId) } returns Result.success(true)

        viewModel = createViewModel()
        viewModel.toggleFavorite(pokemonId)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { toggleFavoriteUseCase(pokemonId) }
    }

    @Test
    fun `test toggleFavorite handles success`() = runTest {
        val pokemonId = 25
        coEvery { toggleFavoriteUseCase(pokemonId) } returns Result.success(true)

        viewModel = createViewModel()

        // Должно выполниться без ошибок
        viewModel.toggleFavorite(pokemonId)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { toggleFavoriteUseCase(pokemonId) }
    }

    @Test
    fun `test toggleFavorite handles failure`() = runTest {
        val pokemonId = 25
        val exception = RuntimeException("Database error")
        coEvery { toggleFavoriteUseCase(pokemonId) } returns Result.failure(exception)

        viewModel = createViewModel()

        // Должно выполниться без краша
        viewModel.toggleFavorite(pokemonId)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { toggleFavoriteUseCase(pokemonId) }
    }

    @Test
    fun `test multiple filter updates`() = runTest {
        viewModel = createViewModel()

        viewModel.updateSearchQuery("pokemon1")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("First query", "pokemon1", viewModel.filter.value.searchQuery)

        viewModel.updateSearchQuery("pokemon2")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Second query", "pokemon2", viewModel.filter.value.searchQuery)

        viewModel.clearFilters()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Cleared query", "", viewModel.filter.value.searchQuery)
    }

    @Test
    fun `test filter updates preserve other properties`() = runTest {
        viewModel = createViewModel()

        // Устанавливаем полный фильтр
        val fullFilter = PokemonFilter(
            searchQuery = "test",
            selectedTypes = setOf("fire"),
            isAscending = false
        )
        viewModel.updateFilter(fullFilter)
        testDispatcher.scheduler.advanceUntilIdle()

        // Обновляем только search query
        viewModel.updateSearchQuery("updated")
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedFilter = viewModel.filter.value
        assertEquals("Search query updated", "updated", updatedFilter.searchQuery)
        assertEquals("Selected types preserved", setOf("fire"), updatedFilter.selectedTypes)
        assertEquals("Ascending preserved", false, updatedFilter.isAscending)
    }

    @Test
    fun `test paging flow is not null`() = runTest {
        viewModel = createViewModel()

        val pagingFlow = viewModel.pokemonPagingFlow
        assertNotNull("Paging flow should not be null", pagingFlow)
    }

    @Test
    fun `test available types flow initial state`() = runTest {
        viewModel = createViewModel()

        val initialTypes = viewModel.availableTypes.value
        assertNotNull("Available types should not be null", initialTypes)
        assertTrue("Available types should be empty initially", initialTypes.isEmpty())
    }

    private fun createViewModel() = PokemonViewModel(
        repository = repository,
        toggleFavoriteUseCase = toggleFavoriteUseCase
    )
}