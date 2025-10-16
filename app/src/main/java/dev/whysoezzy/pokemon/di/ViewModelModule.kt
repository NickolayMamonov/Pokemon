package dev.whysoezzy.pokemon.di

import dev.whysoezzy.feature_favorites.presentation.FavoritesViewModel
import dev.whysoezzy.feature_pokemon_details.presentation.PokemonDetailsViewModel
import dev.whysoezzy.feature_pokemon_list.presentation.PokemonViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PokemonViewModel(get(), get())
    }
    viewModel {
        PokemonDetailsViewModel(get())
    }
    viewModel {
        FavoritesViewModel(get(), get())
    }
}