package dev.whysoezzy.core_database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.whysoezzy.core_database.dao.PokemonDao
import dev.whysoezzy.core_database.entity.PokemonConverters
import dev.whysoezzy.core_database.entity.PokemonEntity
import dev.whysoezzy.core_database.entity.PokemonListItemEntity

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
