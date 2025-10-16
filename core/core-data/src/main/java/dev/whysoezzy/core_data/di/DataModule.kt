package dev.whysoezzy.core_data.di

import dev.whysoezzy.core_data.datastore.SettingsDataStore
import dev.whysoezzy.core_data.repository.FavoritesRepositoryImpl
import dev.whysoezzy.core_data.repository.PokemonRepositoryImpl
import dev.whysoezzy.core_data.repository.SettingsRepositoryImpl
import dev.whysoezzy.domain.repository.FavoritesRepository
import dev.whysoezzy.domain.repository.PokemonRepository
import dev.whysoezzy.domain.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule =
    module {
        single<PokemonRepository> {
            PokemonRepositoryImpl(get(), get())
        }

        single<FavoritesRepository> {
            FavoritesRepositoryImpl(get())
        }

        single {
            SettingsDataStore(androidContext())
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(
                context = androidContext(),
                settingsDataStore = get(),
                pokemonDao = get()
            )
        }

    }
