package dev.whysoezzy.pokemon.di

import dev.whysoezzy.core_database.di.databaseModule
import dev.whysoezzy.core_network.di.networkModule

val appModules = listOf(
    databaseModule,
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)