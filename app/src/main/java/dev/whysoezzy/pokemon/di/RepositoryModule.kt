package dev.whysoezzy.pokemon.di

import dev.whysoezzy.pokemon.data.repository.PokemonRepositoryImpl
import dev.whysoezzy.pokemon.domain.repository.PokemonRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PokemonRepository> {
        PokemonRepositoryImpl(get(), get())
    }
}