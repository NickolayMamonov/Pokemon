package dev.whysoezzy.feature_pokemon_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PokemonDetailsViewModel(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailsUiState())
    val uiState: StateFlow<PokemonDetailsUiState> = _uiState.asStateFlow()

    private var loadingJob: Job? = null

    fun loadPokemonDetails(pokemon: Pokemon) {
        Timber.d("Загружаем детали для покемона: ${pokemon.name} (ID: ${pokemon.id})")

        val hasValidStats = pokemon.stats.any { it.baseStat > 0 }

        if (!hasValidStats) {
            Timber.w("У покемона ${pokemon.name} нет валидных статистик, перезагружаем...")
            loadPokemonById(pokemon.id.toString())
        } else {
            _uiState.value = _uiState.value.copy(
                pokemon = pokemon,
                isLoading = false,
                error = null
            )
        }
    }

    fun loadPokemonById(pokemonId: String) {
        loadingJob?.cancel()

        loadingJob = viewModelScope.launch {
            Timber.i("Загружаем покемона по ID: $pokemonId")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                getPokemonDetailsUseCase(pokemonId).fold(
                    onSuccess = { pokemon ->
                        // Проверяем, не была ли отменена корутина
                        ensureActive()

                        Timber.i("Успешно загружены детали для покемона: ${pokemon.name}")
                        _uiState.value = _uiState.value.copy(
                            pokemon = pokemon,
                            isLoading = false,
                            error = null
                        )
                    },
                    onFailure = { error ->
                        ensureActive()

                        Timber.e(error, "Ошибка загрузки покемона с ID: $pokemonId")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Не удалось загрузить покемона"
                        )
                    }
                )
            } catch (e: CancellationException) {
                Timber.d("Загрузка покемона $pokemonId была отменена")
                throw e
            } catch (e: Exception) {
                Timber.e(e, "Неожиданная ошибка при загрузке покемона $pokemonId")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Неожиданная ошибка: ${e.message}"
                )
            }
        }
    }

    fun onImageLoadingChanged(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isImageLoading = isLoading)
    }


    fun toggleExtendedStats() {
        val newState = !_uiState.value.showExtendedStats
        Timber.d("Переключаем расширенную статистику: $newState")
        _uiState.value = _uiState.value.copy(showExtendedStats = newState)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        loadingJob?.cancel()
    }
}
