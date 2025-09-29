package dev.whysoezzy.feature_pokemon_details.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.whysoezzy.core_common.extensions.toDisplayName
import dev.whysoezzy.core_common.extensions.toTypeColor
import dev.whysoezzy.core_uikit.components.errors.ErrorMessage
import dev.whysoezzy.core_uikit.components.loadings.LoadingIndicator
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.feature_pokemon_details.components.sections.PokemonBattleStatsSection
import dev.whysoezzy.feature_pokemon_details.components.sections.PokemonHeaderSection
import dev.whysoezzy.feature_pokemon_details.components.sections.PokemonPhysicalStatsSection
import dev.whysoezzy.feature_pokemon_details.components.sections.PokemonTypesSection
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailsScreen(
    pokemon: Pokemon,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Добавляем логирование изменений состояния
    LaunchedEffect(uiState.pokemon) {
        if (uiState.pokemon != null) {
            Timber.d("uiState.pokemon updated: ${uiState.pokemon!!.name}")
            Timber.d("uiState.pokemon stats: ${uiState.pokemon!!.stats.map { "${it.name}: ${it.baseStat}" }}")
        } else {
            Timber.d("uiState.pokemon is null")
        }
    }

    // Используем покемона из состояния, если доступен, иначе переданный параметр
    val displayPokemon = uiState.pokemon ?: pokemon
    val primaryType = displayPokemon.types.firstOrNull()?.name ?: "normal"
    val backgroundColor = primaryType.toTypeColor()

    // Логируем какой покемон отображается
    LaunchedEffect(displayPokemon, uiState.loadingState) {
        Timber.d(
            "displayPokemon: ${displayPokemon.name}, from uiState: ${uiState.pokemon != null}, stats: ${displayPokemon.stats.map { "${it.name}: ${it.baseStat}" }}",
        )
    }

    // Загружаем детали покемона при появлении экрана
    LaunchedEffect(pokemon) {
        Timber.d("Pokemon received in details screen: ${pokemon.name}")
        Timber.d("Pokemon stats received: ${pokemon.stats.map { "${it.name}: ${it.baseStat}" }}")
        Timber.d("Pokemon total stats: ${pokemon.stats.sumOf { it.baseStat }}")
        viewModel.onIntent(PokemonDetailsViewModel.Intent.LoadPokemonDetails(pokemon))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = displayPokemon.name.toDisplayName(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = backgroundColor.copy(alpha = 0.3f),
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        },
    ) { paddingValues ->
        when {
            // Показываем только loading если нет данных вообще и идет загрузка
            uiState.shouldShowLoadingOnly -> {
                Timber.d("Showing loading - state: ${uiState.loadingState}")
                LoadingIndicator()
            }

            // Показываем ошибку если нет данных и есть ошибка
            uiState.hasError && !uiState.hasData -> {
                Timber.d("Showing error - ${uiState.errorMessage}")
                ErrorMessage(
                    error = uiState.errorMessage ?: "Unknown error",
                    onRetry =
                        if (uiState.canRetry) {
                            { viewModel.onIntent(PokemonDetailsViewModel.Intent.Retry) }
                        } else {
                            null
                        },
                    isFullScreen = true,
                )
            }

            // Во всех остальных случаях показываем контент
            else -> {
                Timber.d("Showing content - pokemon: ${displayPokemon.name}, state: ${uiState.loadingState}")
                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
                        viewModel.onIntent(PokemonDetailsViewModel.Intent.Refresh)
                    },
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Error banner если есть ошибка с существующими данными
                        if (uiState.hasError && uiState.hasData) {
                            ErrorMessage(
                                error = uiState.errorMessage ?: "Unknown error",
                                onRetry =
                                    if (uiState.canRetry) {
                                        { viewModel.onIntent(PokemonDetailsViewModel.Intent.Retry) }
                                    } else {
                                        null
                                    },
                                isFullScreen = false,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            )
                        }

                        PokemonDetailsContent(
                            pokemon = displayPokemon,
                            uiState = uiState,
                            viewModel = viewModel,
                            backgroundColor = backgroundColor,
                            paddingValues = paddingValues,
                            scrollState = scrollState,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonDetailsContent(
    pokemon: Pokemon,
    uiState: PokemonDetailsUiState,
    viewModel: PokemonDetailsViewModel,
    backgroundColor: androidx.compose.ui.graphics.Color,
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
    scrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(paddingValues)
                .verticalScroll(scrollState),
    ) {
        // Верхняя секция с изображением и основной информацией
        PokemonHeaderSection(
            pokemon = pokemon,
            backgroundColor = backgroundColor,
            isImageLoading = uiState.isImageLoading,
            onImageLoadingChanged = { isLoading ->
                viewModel.onIntent(PokemonDetailsViewModel.Intent.ImageLoadingChanged(isLoading))
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Секция с типами
        PokemonTypesSection(
            types = pokemon.types,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Физические характеристики
        PokemonPhysicalStatsSection(
            height = pokemon.height,
            weight = pokemon.weight,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Боевые характеристики
        PokemonBattleStatsSection(
            stats = pokemon.stats,
            showExtended = uiState.showExtendedStats,
            onToggleExtended = { show ->
                viewModel.onIntent(PokemonDetailsViewModel.Intent.ToggleExtendedStats(show))
            },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
