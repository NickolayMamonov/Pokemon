package dev.whysoezzy.feature_pokemon_list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.whysoezzy.core_uikit.components.bars.SearchAndFilterBar
import dev.whysoezzy.core_uikit.components.cards.PokemonCard
import dev.whysoezzy.core_uikit.components.errors.ErrorMessage
import dev.whysoezzy.core_uikit.components.loadings.LoadingIndicator
import dev.whysoezzy.core_uikit.components.sheets.FilterBottomSheet
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.SortBy
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonSelected: (Pokemon) -> Unit,
    viewModel: PokemonViewModel = koinViewModel(),
) {
    val pokemonPagingItems: LazyPagingItems<Pokemon> =
        viewModel.pokemonPagingFlow.collectAsLazyPagingItems()

    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val availableTypes by viewModel.availableTypes.collectAsStateWithLifecycle()

    val isFirstLoad = remember { mutableStateOf(true) }
    var showFilterBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val pullToRefreshState = rememberPullToRefreshState()
    var isManualRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(pokemonPagingItems.loadState.refresh) {
        if (pokemonPagingItems.loadState.refresh is LoadState.NotLoading) {
            isFirstLoad.value = false
            isManualRefreshing = false
        }
    }

    LaunchedEffect(pokemonPagingItems.itemSnapshotList) {
        val types = pokemonPagingItems.itemSnapshotList.items
            .flatMap { it.types.map { type -> type.name } }
            .toSet()
        if (types.isNotEmpty()) {
            viewModel.updateAvailableTypes(types)
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when {
                pokemonPagingItems.loadState.refresh is LoadState.Error -> {
                    val error = ((pokemonPagingItems.loadState.refresh) as LoadState.Error).error
                    ErrorMessage(
                        error = error.message ?: "Ошибка загрузки",
                        onRetry = { pokemonPagingItems.retry() }
                    )
                }

                // Показываем полноэкранный индикатор только при первой загрузке
                pokemonPagingItems.loadState.refresh is LoadState.Loading && isFirstLoad.value -> {
                    LoadingIndicator()
                }

                else -> {
                    SearchAndFilterBar(
                        searchQuery = filter.searchQuery,
                        onSearchQueryChange = viewModel::updateSearchQuery,
                        onFilterClick = { showFilterBottomSheet = true },
                        hasActiveFilters =
                            filter.selectedTypes.isNotEmpty() ||
                                    filter.sortBy != SortBy.ID ||
                                    !filter.isAscending,
                    )

                    PullToRefreshBox(
                        isRefreshing = isManualRefreshing,
                        onRefresh = { pokemonPagingItems.refresh() },
                        state = pullToRefreshState
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Показываем индикатор загрузки внутри Grid при обновлении
                            if (pokemonPagingItems.loadState.refresh is LoadState.Loading && !isFirstLoad.value) {
                                item(span = { GridItemSpan(2) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            items(
                                count = pokemonPagingItems.itemCount,
                                key = { index ->
                                    pokemonPagingItems.peek(index)?.id ?: index
                                }
                            ) { index ->
                                val pokemon = pokemonPagingItems[index]

                                if (pokemon != null) {
                                    PokemonCard(
                                        pokemon = pokemon,
                                        onPokemonClick = { onPokemonSelected(it) }
                                    )
                                }
                            }

                            if (pokemonPagingItems.loadState.append is LoadState.Loading) {
                                item(span = { GridItemSpan(2) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            if (pokemonPagingItems.loadState.append is LoadState.Error) {
                                item(span = { GridItemSpan(2) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Ошибка загрузки",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }

                            // Показываем сообщение если нет результатов
                            if (pokemonPagingItems.itemCount == 0 &&
                                pokemonPagingItems.loadState.refresh is LoadState.NotLoading
                            ) {
                                item(span = { GridItemSpan(2) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Покемоны не найдены",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                availableTypes = availableTypes,
                onFilterChange = { newFilter ->
                    viewModel.updateFilter(newFilter)
                },
                onDismiss = { showFilterBottomSheet = false },
            )
        }
    }
}
