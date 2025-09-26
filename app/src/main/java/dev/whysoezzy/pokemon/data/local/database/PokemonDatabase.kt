package dev.whysoezzy.pokemon.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.whysoezzy.pokemon.data.local.dao.PokemonDao
import dev.whysoezzy.pokemon.data.local.entity.PokemonConverters
import dev.whysoezzy.pokemon.data.local.entity.PokemonEntity
import dev.whysoezzy.pokemon.data.local.entity.PokemonListItemEntity

@Database(
    entities = [PokemonEntity::class, PokemonListItemEntity::class],
    version = 1,
)
@TypeConverters(PokemonConverters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object {
        const val DATABASE_NAME = "pokemon_database"
    }
}