package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import dev.whysoezzy.domain.repository.PokemonRepository
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetPokemonDetailsUseCaseTest {
    private lateinit var useCase: GetPokemonDetailsUseCase
    private lateinit var repository: PokemonRepository

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPokemonDetailsUseCase(repository)
    }

    @Test
    fun `invoke with valid id returns success result from repository`() =
        runTest {
            val pokemonId = "1"
            val expectedPokemon = createTestPokemon(1, "Bulbasaur")
            coEvery { repository.getPokemonDetails(pokemonId) } returns Result.success(expectedPokemon)

            val result = useCase(pokemonId)

            assertTrue(result.isSuccess)
            assertEquals(expectedPokemon, result.getOrThrow())
            coVerify { repository.getPokemonDetails(pokemonId) }
        }

    @Test
    fun `invoke with repository failure returns failure result`() =
        runTest {
            val pokemonId = "1"
            val exception = IOException("Network error")
            coEvery { repository.getPokemonDetails(pokemonId) } returns Result.failure(exception)

            val result = useCase(pokemonId)

            assertTrue(result.isFailure)
            assertEquals("Network error", result.exceptionOrNull()?.message)
            coVerify { repository.getPokemonDetails(pokemonId) }
        }

    @Test
    fun `invoke with different pokemon ids calls repository with correct values`() =
        runTest {
            val pokemonIds = listOf("25", "150", "999")
            val expectedPokemons =
                listOf(
                    createTestPokemon(25, "Pikachu"),
                    createTestPokemon(150, "Mewtwo"),
                    createTestPokemon(999, "Unknown"),
                )

            pokemonIds.forEachIndexed { index, id ->
                coEvery { repository.getPokemonDetails(id) } returns Result.success(expectedPokemons[index])
            }

            pokemonIds.forEachIndexed { index, id ->
                val result = useCase(id)

                assertTrue(result.isSuccess)
                assertEquals(expectedPokemons[index], result.getOrThrow())
                coVerify { repository.getPokemonDetails(id) }
            }
        }

    @Test
    fun `invoke with string id calls repository correctly`() =
        runTest {
            val pokemonId = "pikachu"
            val expectedPokemon = createTestPokemon(25, "Pikachu")
            coEvery { repository.getPokemonDetails(pokemonId) } returns Result.success(expectedPokemon)

            val result = useCase(pokemonId)

            assertTrue(result.isSuccess)
            assertEquals(expectedPokemon, result.getOrThrow())
            coVerify { repository.getPokemonDetails(pokemonId) }
        }

    @Test
    fun `invoke with empty id calls repository correctly`() =
        runTest {
            val pokemonId = ""
            val exception = IllegalArgumentException("Invalid pokemon id")
            coEvery { repository.getPokemonDetails(pokemonId) } returns Result.failure(exception)

            val result = useCase(pokemonId)

            assertTrue(result.isFailure)
            assertEquals("Invalid pokemon id", result.exceptionOrNull()?.message)
            coVerify { repository.getPokemonDetails(pokemonId) }
        }

    private fun createTestPokemon(
        id: Int,
        name: String,
    ) = Pokemon(
        id = id,
        name = name,
        height = 10,
        weight = 100,
        imageUrl = "https://example.com/$id.png",
        types =
            listOf(
                PokemonType(name = "electric", slot = 1),
            ),
        stats =
            listOf(
                PokemonStat(name = "hp", baseStat = 35, effort = 0),
                PokemonStat(name = "attack", baseStat = 55, effort = 0),
                PokemonStat(name = "defense", baseStat = 40, effort = 0),
                PokemonStat(name = "speed", baseStat = 90, effort = 0),
            ),
    )
}
