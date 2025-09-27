package dev.whysoezzy.pokemon.presentation.list

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
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.whysoezzy.core_uikit.components.bars.SearchAndFilterBar
import dev.whysoezzy.core_uikit.components.cards.PokemonCard
import dev.whysoezzy.core_uikit.components.errors.ErrorMessage
import dev.whysoezzy.core_uikit.components.loadings.LoadingIndicator
import dev.whysoezzy.core_uikit.components.messages.NoResultsMessage
import dev.whysoezzy.core_uikit.components.sheets.FilterBottomSheet
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.SortBy
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonSelected: (Pokemon) -> Unit,
    viewModel: PokemonListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    var showFilterBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var isRefreshingUI by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(uiState.isRefreshing) {
        if (uiState.isRefreshing && !isRefreshingUI) {
            isRefreshingUI = true
        } else if (!uiState.isRefreshing && isRefreshingUI) {
            delay(300)
            isRefreshingUI = false
        }
    }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.filteredPokemon.size - 5 &&
                    uiState.hasNextPage &&
                    !uiState.isLoadingMore
                ) {
                    viewModel.loadMorePokemon()
                }
            }
    }
    LaunchedEffect(showFilterBottomSheet) {
        if (showFilterBottomSheet) {
            bottomSheetState.expand()
        }
    }
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val currentError = uiState.error
            when {
                currentError != null -> {
                    ErrorMessage(
                        error = currentError,
                        onRetry = { viewModel.loadPokemonList() }
                    )
                }

                uiState.pokemonList.isEmpty() && uiState.isLoading -> {
                    LoadingIndicator()
                }

                else -> {
                    SearchAndFilterBar(
                        searchQuery = filter.searchQuery,
                        onSearchQueryChange = viewModel::updateSearchQuery,
                        onFilterClick = { showFilterBottomSheet = true },
                        hasActiveFilters = filter.selectedTypes.isNotEmpty() ||
                                filter.sortBy != SortBy.ID ||
                                !filter.isAscending,
                        onClearFilters = { viewModel.clearFilters() }
                    )
                    PullToRefreshBox(
                        isRefreshing = isRefreshingUI,
                        onRefresh = {
                            if (!isRefreshingUI) {
                                viewModel.refreshPokemonList()
                            }
                        },
                        state = pullToRefreshState,
                    ) {
                        if (uiState.filteredPokemon.isEmpty() && uiState.detailedPokemon.isNotEmpty()) {
                            NoResultsMessage(
                                hasActiveFilters = filter.selectedTypes.isNotEmpty() || filter.searchQuery.isNotBlank(),
                                onClearFilters = { viewModel.clearFilters() }
                            )
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                state = gridState,
                                contentPadding = PaddingValues(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    uiState.filteredPokemon,
                                    key = { pokemon -> pokemon.id }) { pokemon ->
                                    PokemonCard(
                                        pokemon = pokemon,
                                        onPokemonClick = { selectedPokemon ->
                                            onPokemonSelected(selectedPokemon)
                                        }
                                    )
                                }
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
                                    item {
                                        Spacer(modifier = Modifier.height(1.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showFilterBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            FilterBottomSheet(
                filter = filter,
                availableTypes = uiState.availableTypes,
                onFilterChange = { newFilter ->
                    viewModel.updateFilter(newFilter)
                },
                onDismiss = { showFilterBottomSheet = false }
            )
        }
    }
}