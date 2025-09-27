package dev.whysoezzy.pokemon.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.domain.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val backStack = mutableListOf<Screen>()

    init {
        backStack.add(Screen.PokemonList)
    }

    fun navigateToPokemonDetails(pokemon: Pokemon) {
        Timber.d("Навигация к деталям покемона: ${pokemon.name} (ID: ${pokemon.id})")

        val newScreen = Screen.PokemonDetails(pokemon)

        if (backStack.lastOrNull() !is Screen.PokemonDetails ||
            (backStack.lastOrNull() as? Screen.PokemonDetails)?.pokemon?.id != pokemon.id
        ) {
            backStack.add(newScreen)
        }

        updateCurrentScreen(newScreen, pokemon)

        Timber.d("BackStack size: ${backStack.size}, screens: ${backStack.map { it::class.simpleName }}")
    }

    fun navigateToList() {
        Timber.d("Навигация к списку покемонов с очисткой backstack")

        backStack.clear()
        backStack.add(Screen.PokemonList)

        updateCurrentScreen(Screen.PokemonList, null)
    }

    fun navigateBack(): Boolean {
        Timber.d("Попытка навигации назад. BackStack size: ${backStack.size}")

        return if (backStack.size > 1) {
            backStack.removeLastOrNull()

            val previousScreen = backStack.lastOrNull() ?: Screen.PokemonList

            val selectedPokemon = when (previousScreen) {
                is Screen.PokemonDetails -> previousScreen.pokemon
                is Screen.PokemonList -> null
            }

            updateCurrentScreen(previousScreen, selectedPokemon)

            Timber.d("Навигация назад выполнена. Текущий экран: ${previousScreen::class.simpleName}")
            Timber.d("BackStack size: ${backStack.size}")

            true // Навигация обработана
        } else {
            Timber.d("BackStack пуст или содержит только начальный экран - завершаем приложение")
            false
        }
    }

    fun getBackStackSize(): Int = backStack.size

    fun canNavigateBack(): Boolean {
        return backStack.size > 1
    }

    fun onNavigationComplete() {
        _uiState.value = _uiState.value.copy(isNavigating = false)
    }

    private fun updateCurrentScreen(screen: Screen, selectedPokemon: Pokemon?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                currentScreen = screen,
                selectedPokemon = selectedPokemon,
                isNavigating = true,
                canNavigateBack = canNavigateBack()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        backStack.clear()
        Timber.d("MainViewModel cleared")
    }
}