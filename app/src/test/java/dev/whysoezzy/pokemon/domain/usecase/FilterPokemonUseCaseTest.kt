package dev.whysoezzy.pokemon.domain.usecase


import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import dev.whysoezzy.domain.model.SortBy
import dev.whysoezzy.domain.usecase.FilterPokemonUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FilterPokemonUseCaseTest {

    private lateinit var filterPokemonUseCase: FilterPokemonUseCase
    private lateinit var testPokemonList: List<Pokemon>

    @Before
    fun setUp() {
        filterPokemonUseCase = FilterPokemonUseCase()

        testPokemonList = listOf(
            createTestPokemon(
                id = 1,
                name = "Bulbasaur",
                types = listOf("grass", "poison"),
                hp = 45,
                attack = 49,
                defense = 49,
                speed = 45,
                height = 7,
                weight = 69
            ),
            createTestPokemon(
                id = 4,
                name = "Charmander",
                types = listOf("fire"),
                hp = 39,
                attack = 52,
                defense = 43,
                speed = 65,
                height = 6,
                weight = 85
            ),
            createTestPokemon(
                id = 7,
                name = "Squirtle",
                types = listOf("water"),
                hp = 44,
                attack = 48,
                defense = 65,
                speed = 43,
                height = 5,
                weight = 90
            ),
            createTestPokemon(
                id = 25,
                name = "Pikachu",
                types = listOf("electric"),
                hp = 35,
                attack = 55,
                defense = 40,
                speed = 90,
                height = 4,
                weight = 60
            )
        )
    }

    @Test
    fun `filter by empty query returns all pokemon`() = runTest {
        val filter = PokemonFilter()

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertEquals(4, result.size)
    }

    @Test
    fun `filter by pokemon name returns matching pokemon`() = runTest {
        val filter = PokemonFilter(searchQuery = "pika")

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertEquals(1, result.size)
        assertEquals("Pikachu", result.first().name)
    }

    @Test
    fun `filter by pokemon id returns matching pokemon`() = runTest {
        val filter = PokemonFilter(searchQuery = "25")

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertEquals(1, result.size)
        assertEquals("Pikachu", result.first().name)
    }

    @Test
    fun `filter by pokemon type returns matching pokemon`() = runTest {
        val filter = PokemonFilter(searchQuery = "fire")

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertEquals(1, result.size)
        assertEquals("Charmander", result.first().name)
    }

    @Test
    fun `filter by selected types returns matching pokemon`() = runTest {
        val filter = PokemonFilter(selectedTypes = setOf("water"))

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertEquals(1, result.size)
        assertEquals("Squirtle", result.first().name)
    }

    @Test
    fun `filter by multiple selected types returns pokemon with all types`() = runTest {
        val filter = PokemonFilter(selectedTypes = setOf("grass", "poison"))

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertEquals(1, result.size)
        assertEquals("Bulbasaur", result.first().name)
    }

    @Test
    fun `filter by min hp returns pokemon with hp greater than or equal to threshold`() = runTest {
        val filter = PokemonFilter(minHp = 40)

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertEquals(2, result.size)
        assertTrue(result.all { it.hp >= 40 })
    }

    @Test
    fun `filter by max hp returns pokemon with hp less than or equal to threshold`() = runTest {
        val filter = PokemonFilter(maxHp = 40)

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertEquals(2, result.size)
        assertTrue(result.all { it.hp <= 40 })
    }

    @Test
    fun `sort by name ascending returns alphabetically sorted pokemon`() = runTest {
        val filter = PokemonFilter(sortBy = SortBy.NAME, isAscending = true)

        val result = filterPokemonUseCase(testPokemonList, filter)

        val expectedOrder = listOf("Bulbasaur", "Charmander", "Pikachu", "Squirtle")
        assertEquals(expectedOrder, result.map { it.name })
    }

    @Test
    fun `sort by name descending returns reverse alphabetically sorted pokemon`() = runTest {
        val filter = PokemonFilter(sortBy = SortBy.NAME, isAscending = false)

        val result = filterPokemonUseCase(testPokemonList, filter)

        val expectedOrder = listOf("Squirtle", "Pikachu", "Charmander", "Bulbasaur")
        assertEquals(expectedOrder, result.map { it.name })
    }

    @Test
    fun `sort by id ascending returns id sorted pokemon`() = runTest {
        val filter = PokemonFilter(sortBy = SortBy.ID, isAscending = true)

        val result = filterPokemonUseCase(testPokemonList, filter)

        val expectedOrder = listOf(1, 4, 7, 25)
        assertEquals(expectedOrder, result.map { it.id })
    }

    @Test
    fun `sort by attack descending returns attack sorted pokemon`() = runTest {
        val filter = PokemonFilter(sortBy = SortBy.ATTACK, isAscending = false)

        val result = filterPokemonUseCase(testPokemonList, filter)

        assertTrue(result[0].attack >= result[1].attack)
        assertTrue(result[1].attack >= result[2].attack)
        assertTrue(result[2].attack >= result[3].attack)
    }

    private fun createTestPokemon(
        id: Int,
        name: String,
        types: List<String>,
        hp: Int,
        attack: Int,
        defense: Int,
        speed: Int,
        height: Int,
        weight: Int
    ) = Pokemon(
        id = id,
        name = name,
        height = height,
        weight = weight,
        imageUrl = "https://example.com/$id.png",
        types = types.mapIndexed { index, typeName ->
            PokemonType(name = typeName, slot = index + 1)
        },
        stats = listOf(
            PokemonStat(name = "hp", baseStat = hp, effort = 0),
            PokemonStat(name = "attack", baseStat = attack, effort = 0),
            PokemonStat(name = "defense", baseStat = defense, effort = 0),
            PokemonStat(name = "speed", baseStat = speed, effort = 0)
        )
    )
}
