package dev.whysoezzy.pokemon.di

import dev.whysoezzy.pokemon.presentation.detail.PokemonDetailsViewModel
import dev.whysoezzy.pokemon.presentation.list.PokemonListViewModel
import dev.whysoezzy.pokemon.presentation.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
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