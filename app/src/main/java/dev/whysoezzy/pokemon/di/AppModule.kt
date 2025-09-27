package dev.whysoezzy.pokemon.di

import dev.whysoezzy.core_data.di.repositoryModule
import dev.whysoezzy.core_database.di.databaseModule
import dev.whysoezzy.core_network.di.networkModule
import dev.whysoezzy.domain.di.useCaseModule

val appModules = listOf(
    databaseModule,
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)