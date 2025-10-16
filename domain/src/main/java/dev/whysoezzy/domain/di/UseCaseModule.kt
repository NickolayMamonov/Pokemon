package dev.whysoezzy.domain.di

import dev.whysoezzy.domain.usecase.FilterPokemonUseCase
import dev.whysoezzy.domain.usecase.GetFavoritePokemonsUseCase
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import dev.whysoezzy.domain.usecase.GetPokemonListUseCase
import dev.whysoezzy.domain.usecase.IsFavoriteUseCase
import dev.whysoezzy.domain.usecase.ToggleFavoriteUseCase
import org.koin.dsl.module

val useCaseModule =
    module {
        factory { GetPokemonListUseCase(get()) }
        factory { GetPokemonDetailsUseCase(get()) }
        factory { FilterPokemonUseCase() }

        factory { ToggleFavoriteUseCase(get()) }
        factory { GetFavoritePokemonsUseCase(get()) }
        factory { IsFavoriteUseCase(get()) }
    }
