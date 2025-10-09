package dev.whysoezzy.core_network.di

import dev.whysoezzy.core_network.PokemonApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

val networkModule =
    module {
        single {
            val logging =
                HttpLoggingInterceptor { message ->
                    Timber.tag("HTTP").d(message)
                }
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient =
                OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()

            Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        single<PokemonApi> {
            get<Retrofit>().create(PokemonApi::class.java)
        }
    }
