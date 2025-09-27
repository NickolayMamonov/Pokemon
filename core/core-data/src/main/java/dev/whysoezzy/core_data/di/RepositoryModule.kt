package dev.whysoezzy.core_data.di

import dev.whysoezzy.core_data.repository.PokemonRepositoryImpl
import dev.whysoezzy.domain.repository.PokemonRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PokemonRepository> {
        PokemonRepositoryImpl(get(), get())
    }
}