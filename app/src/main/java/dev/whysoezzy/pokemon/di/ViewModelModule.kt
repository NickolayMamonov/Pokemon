package dev.whysoezzy.pokemon.di

import dev.whysoezzy.feature_pokemon_details.presentation.PokemonDetailsViewModel
import dev.whysoezzy.feature_pokemon_list.presentation.PokemonListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PokemonListViewModel(
            getPokemonListUseCase = get(),
            getPokemonDetailsUseCase = get(),
            filterPokemonUseCase = get()
        )
    }
    viewModel {
        PokemonDetailsViewModel(
            getPokemonDetailsUseCase = get()
        )
    }
}