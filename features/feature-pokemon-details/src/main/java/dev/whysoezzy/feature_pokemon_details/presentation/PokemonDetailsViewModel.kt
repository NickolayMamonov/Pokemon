package dev.whysoezzy.feature_pokemon_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailsUiState>(PokemonDetailsUiState.Loading)
    val uiState: StateFlow<PokemonDetailsUiState> = _uiState.asStateFlow()


    fun loadPokemon(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailsUiState.Loading
            getPokemonDetailsUseCase(pokemonId.toString()).fold(
                onSuccess = { pokemon ->
                    _uiState.value = PokemonDetailsUiState.Success(pokemon)
                },
                onFailure = { error ->
                    _uiState.value = PokemonDetailsUiState.Error(
                        error.message ?: "Не удалось загрузить покемона"
                    )

                }
            )
        }
    }
}

