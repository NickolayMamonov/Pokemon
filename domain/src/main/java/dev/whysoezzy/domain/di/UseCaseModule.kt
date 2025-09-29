package dev.whysoezzy.domain.di

import dev.whysoezzy.domain.usecase.FilterPokemonUseCase
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import dev.whysoezzy.domain.usecase.GetPokemonListUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        factory { GetPokemonListUseCase(get()) }
        factory { GetPokemonDetailsUseCase(get()) }
        factory { FilterPokemonUseCase() }
    }
