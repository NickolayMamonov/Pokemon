package dev.whysoezzy.core_database.di

import androidx.room.Room
import dev.whysoezzy.core_database.database.PokemonDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule =
    module {
        single {
            Room.databaseBuilder(
                androidContext(),
                PokemonDatabase::class.java,
                PokemonDatabase.DATABASE_NAME,
            ).build()
        }

        single { get<PokemonDatabase>().pokemonDao() }
    }
