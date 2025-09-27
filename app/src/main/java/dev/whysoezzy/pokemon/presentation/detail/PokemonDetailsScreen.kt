package dev.whysoezzy.pokemon.presentation.detail

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
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.pokemon.presentation.components.errors.ErrorMessageDetail
import dev.whysoezzy.pokemon.presentation.components.loadings.LoadingIndicator
import dev.whysoezzy.pokemon.presentation.components.sections.PokemonBattleStatsSection
import dev.whysoezzy.pokemon.presentation.components.sections.PokemonHeaderSection
import dev.whysoezzy.pokemon.presentation.components.sections.PokemonPhysicalStatsSection
import dev.whysoezzy.pokemon.presentation.components.sections.PokemonTypesSection
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailsScreen(
    pokemon: Pokemon,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val primaryType = pokemon.types.firstOrNull()?.name ?: "normal"
    val backgroundColor = primaryType.toTypeColor()

    // Загружаем детали покемона в ViewModel
    LaunchedEffect(pokemon) {
        Timber.d("Pokemon received in details screen: ${pokemon.name}")
        Timber.d("Pokemon stats: ${pokemon.stats.map { "${it.name}: ${it.baseStat}" }}")
        viewModel.loadPokemonDetails(pokemon)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = pokemon.name.toDisplayName(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor.copy(alpha = 0.3f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        val currentError = uiState.error
        when {
            uiState.isLoading -> {
                LoadingIndicator()
            }

            uiState.error != null -> {
                ErrorMessageDetail(
                    error = currentError,
                    onRetry = { viewModel.loadPokemonById(pokemon.id.toString()) },
                    onDismiss = { viewModel.clearError() }
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                ) {
                    // Верхняя секция с изображением и основной информацией
                    PokemonHeaderSection(
                        pokemon = pokemon,
                        backgroundColor = backgroundColor,
                        isImageLoading = uiState.isImageLoading,
                        onImageLoadingChanged = viewModel::onImageLoadingChanged
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Секция с типами
                    PokemonTypesSection(
                        types = pokemon.types,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Физические характеристики
                    PokemonPhysicalStatsSection(
                        height = pokemon.height,
                        weight = pokemon.weight,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Боевые характеристики
                    PokemonBattleStatsSection(
                        stats = pokemon.stats,
                        showExtended = uiState.showExtendedStats,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}