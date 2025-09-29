package dev.whysoezzy.core_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "pokemon")
@TypeConverters(PokemonConverters::class)
data class PokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String,
    val types: List<PokemonTypeEntity>,
    val stats: List<PokemonStatEntity>,
    val lastUpdated: Long = System.currentTimeMillis(),
)

data class PokemonTypeEntity(
    val name: String,
    val slot: Int,
)

data class PokemonStatEntity(
    val name: String,
    val baseStat: Int,
    val effort: Int,
)

@Entity(tableName = "pokemon_list")
data class PokemonListItemEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val url: String,
    val lastUpdated: Long = System.currentTimeMillis(),
)

class PokemonConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromTypesList(types: List<PokemonTypeEntity>): String {
        return gson.toJson(types)
    }

    @TypeConverter
    fun toTypesList(typesString: String): List<PokemonTypeEntity> {
        val listType = object : TypeToken<List<PokemonTypeEntity>>() {}.type
        return gson.fromJson(typesString, listType)
    }

    @TypeConverter
    fun fromStatsList(stats: List<PokemonStatEntity>): String {
        return gson.toJson(stats)
    }

    @TypeConverter
    fun toStatsList(statsString: String): List<PokemonStatEntity> {
        val listType = object : TypeToken<List<PokemonStatEntity>>() {}.type
        return gson.fromJson(statsString, listType)
    }
}
