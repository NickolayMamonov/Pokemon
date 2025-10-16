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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import dev.whysoezzy.core_uikit.components.cards.PokemonGridCard
import dev.whysoezzy.core_uikit.components.empty.EmptyStates
import dev.whysoezzy.core_uikit.components.errors.ErrorMessage
import dev.whysoezzy.core_uikit.components.loadings.PokemonGridCardSkeleton
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
    val snackbarHostState = remember { SnackbarHostState() }


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

    LaunchedEffect(pokemonPagingItems.loadState.append) {
        if (pokemonPagingItems.loadState.append is LoadState.Error) {
            val error = (pokemonPagingItems.loadState.append as LoadState.Error).error
            val result = snackbarHostState.showSnackbar(
                message = error.message ?: "Ошибка загрузки данных",
                actionLabel = "Повторить"
            )
            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                pokemonPagingItems.retry()
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
        ) {
            when {
                pokemonPagingItems.loadState.refresh is LoadState.Error -> {
                    val error = ((pokemonPagingItems.loadState.refresh) as LoadState.Error).error
                    ErrorMessage(
                        error = error.message ?: "Ошибка загрузки",
                        onRetry = { pokemonPagingItems.retry() }
                    )
                }

                pokemonPagingItems.loadState.refresh is LoadState.Loading && isFirstLoad.value -> {
                    SearchAndFilterBar(
                        searchQuery = filter.searchQuery,
                        onSearchQueryChange = viewModel::updateSearchQuery,
                        onFilterClick = { showFilterBottomSheet = true },
                        hasActiveFilters =
                            filter.selectedTypes.isNotEmpty() ||
                                    filter.sortBy != SortBy.ID ||
                                    !filter.isAscending,
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(6) { // Показываем 6 skeleton карточек
                            PokemonGridCardSkeleton()
                        }
                    }
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
                        onRefresh = {
                            isManualRefreshing = true
                            pokemonPagingItems.refresh()
                        },
                        state = pullToRefreshState
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
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

                            // Список покемонов
                            items(
                                count = pokemonPagingItems.itemCount,
                                key = { index ->
                                    pokemonPagingItems.peek(index)?.id ?: index
                                }
                            ) { index ->
                                val pokemon = pokemonPagingItems[index]

                                if (pokemon != null) {
                                    PokemonGridCard(
                                        id = pokemon.id,
                                        name = pokemon.name,
                                        imageUrl = pokemon.imageUrl,
                                        types = pokemon.types.map { it.name },
                                        onClick = { onPokemonSelected(pokemon) },
                                        showFavoriteButton = true,
                                        isFavorite = pokemon.isFavorite,
                                        onFavoriteClick = {
                                            viewModel.toggleFavorite(pokemon.id)
                                        }
                                    )
                                }
                            }

                            // Индикатор загрузки следующей страницы
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

                            // Ошибка при загрузке следующей страницы
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

                            // Показываем EmptyState если нет результатов
                            if (pokemonPagingItems.itemCount == 0 &&
                                pokemonPagingItems.loadState.refresh is LoadState.NotLoading
                            ) {
                                item(span = { GridItemSpan(2) }) {
                                    EmptyStates.NoSearchResults(
                                        onClearFilters = {
                                            viewModel.updateSearchQuery("")
                                            viewModel.clearFilters()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
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
