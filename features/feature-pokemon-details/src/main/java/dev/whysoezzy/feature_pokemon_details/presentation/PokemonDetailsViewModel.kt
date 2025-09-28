package dev.whysoezzy.feature_pokemon_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.core_data.state.LoadingState
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PokemonDetailsViewModel(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase
) : ViewModel() {

    sealed class Intent {
        data class LoadPokemonDetails(val pokemon: Pokemon) : Intent()
        data class LoadPokemonById(val pokemonId: String) : Intent()
        data object Refresh : Intent()
        data object Retry : Intent()
        data class ToggleExtendedStats(val show: Boolean) : Intent()
        data class ImageLoadingChanged(val isLoading: Boolean) : Intent()
        data object ClearError : Intent()
    }

    private val _state = MutableStateFlow(PokemonDetailsUiState(loadingState = LoadingState.Idle))
    val state: StateFlow<PokemonDetailsUiState> = _state.asStateFlow()

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadPokemonDetails -> {
                Timber.d("Loading Pokemon details: ${intent.pokemon.name}")
                loadPokemonFromObject(intent.pokemon)
            }

            is Intent.LoadPokemonById -> {
                Timber.i("Loading Pokemon by ID: ${intent.pokemonId}")
                loadPokemonById(intent.pokemonId)
            }

            Intent.Refresh -> {
                Timber.i("Refreshing Pokemon details")
                val pokemonId = _state.value.pokemon?.id?.toString()
                if (pokemonId != null) {
                    _state.value = _state.value.copy(loadingState = LoadingState.Refreshing)
                    loadPokemonById(pokemonId)
                } else {
                    Timber.w("Cannot refresh - no Pokemon ID available")
                    _state.value = _state.value.copy(
                        loadingState = LoadingState.Error(
                            message = "Cannot refresh - no Pokemon loaded",
                            isRetry = false
                        )
                    )
                }
            }

            Intent.Retry -> {
                val pokemonId = _state.value.pokemon?.id?.toString()
                if (pokemonId != null) {
                    loadPokemonById(pokemonId)
                } else {
                    Timber.w("Cannot retry - no Pokemon ID available")
                }
            }

            is Intent.ToggleExtendedStats -> {
                Timber.d("Toggling extended stats: ${intent.show}")
                _state.value = _state.value.copy(showExtendedStats = intent.show)
            }

            is Intent.ImageLoadingChanged -> {
                _state.value = _state.value.copy(isImageLoading = intent.isLoading)
            }

            Intent.ClearError -> {
                if (_state.value.pokemon != null) {
                    _state.value = _state.value.copy(loadingState = LoadingState.Success)
                }
            }
        }
    }

    /**
     * Загружает покемона из переданного объекта (проверяет качество данных)
     */
    private fun loadPokemonFromObject(pokemon: Pokemon) {
        Timber.d("Loading Pokemon from object: ${pokemon.name}")
        Timber.d("Pokemon stats: ${pokemon.stats.map { "${it.name}: ${it.baseStat}" }}")
        val hasValidStats = pokemon.stats.any { it.baseStat > 0 }
        Timber.d("Has valid stats: $hasValidStats")

        if (!hasValidStats) {
            Timber.w("Pokemon ${pokemon.name} has invalid stats, reloading from API...")
            _state.value = _state.value.copy(loadingState = LoadingState.Loading)
            loadPokemonById(pokemon.id.toString())
        } else {
            Timber.d("Pokemon ${pokemon.name} has valid data, using provided object")
            _state.value = _state.value.copy(
                pokemon = pokemon,
                loadingState = LoadingState.Success
            )
        }
    }

    /**
     * Загружает покемона по ID через API
     */
    private fun loadPokemonById(pokemonId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loadingState = LoadingState.Loading)
            
            try {
                getPokemonDetailsUseCase(pokemonId).fold(
                    onSuccess = { pokemon ->
                        Timber.i("Successfully loaded Pokemon details: ${pokemon.name}")
                        Timber.d("New Pokemon stats: ${pokemon.stats.map { "${it.name}: ${it.baseStat}" }}")
                        Timber.d("Total stats: ${pokemon.stats.sumOf { it.baseStat }}")
                        _state.value = _state.value.copy(
                            pokemon = pokemon,
                            loadingState = LoadingState.Success
                        )
                        Timber.d("State updated with new Pokemon")
                    },
                    onFailure = { error ->
                        Timber.e(error, "Error loading Pokemon with ID: $pokemonId")
                        _state.value = _state.value.copy(
                            loadingState = LoadingState.Error(
                                message = error.message ?: "Failed to load Pokemon",
                                isRetry = true,
                                hasExistingData = _state.value.pokemon != null
                            )
                        )
                    }
                )
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error loading Pokemon $pokemonId")
                _state.value = _state.value.copy(
                    loadingState = LoadingState.Error(
                        message = "Unexpected error occurred",
                        isRetry = true,
                        hasExistingData = _state.value.pokemon != null
                    )
                )
            }
        }
    }
}
