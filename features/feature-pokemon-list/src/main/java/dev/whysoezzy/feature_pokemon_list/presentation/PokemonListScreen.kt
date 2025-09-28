package dev.whysoezzy.feature_pokemon_list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.whysoezzy.core_data.state.LoadingState
import dev.whysoezzy.core_uikit.components.bars.SearchAndFilterBar
import dev.whysoezzy.core_uikit.components.cards.PokemonCard
import dev.whysoezzy.core_uikit.components.errors.ErrorMessage
import dev.whysoezzy.core_uikit.components.loadings.LoadingIndicator
import dev.whysoezzy.core_uikit.components.messages.NoResultsMessage
import dev.whysoezzy.core_uikit.components.sheets.FilterBottomSheet
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.model.SortBy
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonSelected: (Pokemon) -> Unit,
    viewModel: PokemonListViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()

    var showFilterBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Автоматически загружаем данные при первом появлении экрана
    LaunchedEffect(Unit) {
        Timber.d("PokemonListScreen appeared, loading initial data")
        viewModel.onIntent(PokemonListViewModel.Intent.LoadInitialData)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.shouldShowLoadingOnly -> {
                    // Показываем только индикатор загрузки
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                uiState.hasError && !uiState.hasExistingData -> {
                    // Показываем ошибку без данных
                    ErrorMessage(
                        error = uiState.errorMessage ?: "Unknown error",
                        onRetry = if (uiState.canRetry) {
                            { viewModel.onIntent(PokemonListViewModel.Intent.Retry) }
                        } else null
                    )
                }

                uiState.canShowContent -> {
                    // Показываем контент с возможным индикатором ошибки сверху
                    PokemonListContent(
                        uiState = uiState,
                        filter = filter,
                        viewModel = viewModel,
                        onPokemonSelected = onPokemonSelected,
                        onShowFilters = { showFilterBottomSheet = true }
                    )
                }

                uiState.shouldShowEmptyState -> {
                    // Показываем пустое состояние
                    NoResultsMessage(
                        message = if (filter.searchQuery.isNotEmpty() || filter.selectedTypes.isNotEmpty()) {
                            "No Pokémon found matching your filters"
                        } else {
                            "No Pokémon available"
                        },
                        onRetry = { viewModel.onIntent(PokemonListViewModel.Intent.Refresh) }
                    )
                }

                else -> {
                    // Fallback для неожиданных состояний
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    // Bottom Sheet для фильтров
    if (showFilterBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            FilterBottomSheet(
                filter = filter,
                availableTypes = uiState.availableTypes,
                onFilterChange = { newFilter ->
                    viewModel.onIntent(PokemonListViewModel.Intent.UpdateFilter(newFilter))
                },
                onDismiss = { showFilterBottomSheet = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PokemonListContent(
    uiState: PokemonListUiState,
    filter: PokemonFilter,
    viewModel: PokemonListViewModel,
    onPokemonSelected: (Pokemon) -> Unit,
    onShowFilters: () -> Unit
) {
    val gridState = rememberLazyGridState()

    // Отслеживаем когда нужно загрузить больше данных
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.filteredPokemon.size - 5 &&
                    uiState.hasNextPage &&
                    !uiState.isLoadingMore
                ) {
                    Timber.d("Triggering load more - near end of list")
                    viewModel.onIntent(PokemonListViewModel.Intent.LoadMore)
                }
            }
    }

    // Предзагружаем следующую страницу при скроллинге
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.filteredPokemon.size - 10 &&
                    uiState.hasNextPage &&
                    !uiState.isLoadingMore &&
                    !uiState.isLoading
                ) {
                    viewModel.preloadNextPage()
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search and Filter Bar
        SearchAndFilterBar(
            searchQuery = filter.searchQuery,
            onSearchQueryChange = { query ->
                viewModel.onIntent(PokemonListViewModel.Intent.UpdateSearchQuery(query))
            },
            onFilterClick = onShowFilters,
            hasActiveFilters = filter.selectedTypes.isNotEmpty() ||
                    filter.sortBy != SortBy.ID ||
                    !filter.isAscending,
            onClearFilters = { viewModel.onIntent(PokemonListViewModel.Intent.ClearFilters) }
        )

        // Error banner если есть ошибка с существующими данными
        if (uiState.hasError && uiState.hasExistingData) {
            ErrorMessage(
                error = uiState.errorMessage ?: "Unknown error",
                onRetry = if (uiState.canRetry) {
                    { viewModel.onIntent(PokemonListViewModel.Intent.Retry) }
                } else null,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Pull to refresh wrapper
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                viewModel.onIntent(PokemonListViewModel.Intent.Refresh)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            if (uiState.filteredPokemon.isEmpty() && uiState.hasData) {
                // Показываем сообщение что фильтры ничего не нашли
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoResultsMessage(
                        message = "No Pokémon found matching your filters",
                        actionText = "Clear Filters"
                    ) {
                        viewModel.onIntent(PokemonListViewModel.Intent.ClearFilters)
                    }
                }
            } else {
                // Основной список покемонов
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.filteredPokemon,
                        key = { pokemon -> pokemon.id }
                    ) { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            onPokemonClick = { selectedPokemon ->
                                onPokemonSelected(selectedPokemon)
                            }
                        )
                    }

                    // Индикатор загрузки больше данных
                    if (uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        // Добавляем пустой item для визуального отступа
                        item {
                            Spacer(modifier = Modifier.height(1.dp))
                        }
                    }
                }
            }
        }
    }
}
