package dev.whysoezzy.core_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.whysoezzy.core_database.entity.PokemonEntity
import dev.whysoezzy.core_database.entity.PokemonListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    // Pokemon List operations
    @Query("SELECT * FROM pokemon_list ORDER BY id ASC")
    fun getAllPokemonList(): Flow<List<PokemonListItemEntity>>

    @Query("SELECT * FROM pokemon_list LIMIT :limit OFFSET :offset")
    suspend fun getPokemonListPaginated(limit: Int, offset: Int): List<PokemonListItemEntity>

    @Query("SELECT COUNT(*) FROM pokemon_list")
    suspend fun getPokemonListCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonListItems(pokemonList: List<PokemonListItemEntity>)

    @Query("DELETE FROM pokemon_list")
    suspend fun clearPokemonList()

    // Pokemon Details operations
    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    @Query("SELECT * FROM pokemon WHERE id IN (:ids)")
    suspend fun getPokemonByIds(ids: List<Int>): List<PokemonEntity>

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetails(pokemonList: List<PokemonEntity>)

    @Query("DELETE FROM pokemon")
    suspend fun clearAllPokemon()

    @Query("DELETE FROM pokemon WHERE id = :id")
    suspend fun deletePokemonById(id: Int)

    // Cache validation
    @Query("SELECT lastUpdated FROM pokemon_list WHERE id = :id LIMIT 1")
    suspend fun getPokemonListItemLastUpdated(id: String): Long?

    @Query("SELECT lastUpdated FROM pokemon WHERE id = :id LIMIT 1")
    suspend fun getPokemonLastUpdated(id: Int): Long?

    // Search operations
    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    suspend fun searchPokemonByName(query: String): List<PokemonEntity>
}