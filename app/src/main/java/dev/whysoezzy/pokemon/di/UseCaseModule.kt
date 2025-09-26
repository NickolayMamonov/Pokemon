package dev.whysoezzy.pokemon.di

import dev.whysoezzy.pokemon.domain.usecase.FilterPokemonUseCase
import dev.whysoezzy.pokemon.domain.usecase.GetPokemonDetailsUseCase
import dev.whysoezzy.pokemon.domain.usecase.GetPokemonListUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonDetailsUseCase(get()) }
    factory { FilterPokemonUseCase() }
}