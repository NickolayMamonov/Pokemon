package dev.whysoezzy.pokemon.domain.usecase


import dev.whysoezzy.domain.model.PaginatedData
import dev.whysoezzy.domain.model.PokemonListItem
import dev.whysoezzy.domain.repository.PokemonRepository
import dev.whysoezzy.domain.usecase.GetPokemonListUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetPokemonListUseCaseTest {

    private lateinit var useCase: GetPokemonListUseCase
    private lateinit var repository: PokemonRepository

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPokemonListUseCase(repository)
    }

    @Test
    fun `invoke with valid parameters returns success result from repository`() = runTest {
        val limit = 20
        val offset = 0
        val expectedData = PaginatedData(
            items = listOf(
                PokemonListItem(id = "1", name = "bulbasaur", url = "url1"),
                PokemonListItem(id = "2", name = "ivysaur", url = "url2")
            ),
            totalCount = 100,
            hasNextPage = true,
            currentPage = 1
        )
        coEvery { repository.getPokemonList(limit, offset) } returns Result.success(expectedData)

        val result = useCase(limit, offset)

        assertTrue(result.isSuccess)
        assertEquals(expectedData, result.getOrThrow())
        coVerify { repository.getPokemonList(limit, offset) }
    }

    @Test
    fun `invoke with repository failure returns failure result`() = runTest {

        val limit = 20
        val offset = 0
        val exception = IOException("Network error")
        coEvery { repository.getPokemonList(limit, offset) } returns Result.failure(exception)

        val result = useCase(limit, offset)

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
        coVerify { repository.getPokemonList(limit, offset) }
    }

}
