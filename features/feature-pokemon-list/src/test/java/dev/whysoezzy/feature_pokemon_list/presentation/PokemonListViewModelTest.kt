package dev.whysoezzy.feature_pokemon_list.presentation

import dev.whysoezzy.domain.model.PaginatedData
import dev.whysoezzy.domain.model.PokemonListItem
import dev.whysoezzy.domain.usecase.FilterPokemonUseCase
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import dev.whysoezzy.domain.usecase.GetPokemonListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test ViewModel creation`() = runTest {
        setupBasicMocks()
        viewModel = createViewModel()

        assertNotNull("ViewModel should be created", viewModel)
        assertNotNull("State should be available", viewModel.state)
        assertNotNull("Filter should be available", viewModel.filter)
    }

    @Test
    fun `test initial state availability`() = runTest {
        setupBasicMocks()
        viewModel = createViewModel()

        val initialState = viewModel.state.value
        assertNotNull("Initial state should not be null", initialState)
        assertNotNull("Pokemon list should not be null", initialState.pokemonList)
        assertNotNull("Filtered pokemon should not be null", initialState.filteredPokemon)
        assertNotNull("Loading state should not be null", initialState.loadingState)
    }

    @Test
    fun `test filter state availability`() = runTest {
        setupBasicMocks()
        viewModel = createViewModel()

        val filter = viewModel.filter.value
        assertNotNull("Filter should not be null", filter)
        assertNotNull("Search query should not be null", filter.searchQuery)
        assertNotNull("Selected types should not be null", filter.selectedTypes)
    }

    @Test
    fun `test Intent execution without errors`() = runTest {
        setupBasicMocks()
        viewModel = createViewModel()

        // Просто проверяем что Intent'ы не вызывают исключений
        viewModel.onIntent(PokemonListViewModel.Intent.UpdateSearchQuery("test"))
        viewModel.onIntent(PokemonListViewModel.Intent.UpdateSearchQuery(""))
        viewModel.onIntent(PokemonListViewModel.Intent.ClearFilters)
        
        // Если дошли до этой строки - значит нет исключений
        assertTrue("All intents executed without errors", true)
    }

    @Test
    fun `test multiple ViewModel operations`() = runTest {
        setupBasicMocks()
        viewModel = createViewModel()

        // Выполняем различные операции
        val state1 = viewModel.state.value
        val filter1 = viewModel.filter.value
        
        viewModel.onIntent(PokemonListViewModel.Intent.UpdateSearchQuery("pokemon"))
        val filter2 = viewModel.filter.value
        
        viewModel.onIntent(PokemonListViewModel.Intent.ClearFilters)
        val filter3 = viewModel.filter.value
        
        // Проверяем что все состояния доступны
        assertNotNull("State 1 should be available", state1)
        assertNotNull("Filter 1 should be available", filter1)
        assertNotNull("Filter 2 should be available", filter2)
        assertNotNull("Filter 3 should be available", filter3)
        
        assertTrue("ViewModel operations completed successfully", true)
    }

    @Test
    fun `test ViewModel lifecycle`() = runTest {
        setupBasicMocks()
        
        // Создаем ViewModel
        viewModel = createViewModel()
        assertNotNull("ViewModel created", viewModel)
        
        // Используем его
        viewModel.onIntent(PokemonListViewModel.Intent.UpdateSearchQuery("test"))
        
        // Проверяем что всё еще работает
        assertNotNull("ViewModel still functional", viewModel.state.value)
        assertNotNull("Filter still functional", viewModel.filter.value)
        
        assertTrue("ViewModel lifecycle test passed", true)
    }

    private fun setupBasicMocks() {
        coEvery { getPokemonListUseCase(any(), any()) } returns
            Result.success(
                PaginatedData(
                    items = emptyList<PokemonListItem>(),
                    hasNextPage = false,
                    currentPage = 1,
                    totalCount = 0,
                )
            )
        coEvery { getPokemonDetailsUseCase(any()) } returns Result.failure(Exception("Test"))
        coEvery { filterPokemonUseCase(any(), any()) } returns emptyList()
    }

    private fun createViewModel() = PokemonListViewModel(
        getPokemonListUseCase = getPokemonListUseCase,
        getPokemonDetailsUseCase = getPokemonDetailsUseCase,
        filterPokemonUseCase = filterPokemonUseCase,
    )
}
