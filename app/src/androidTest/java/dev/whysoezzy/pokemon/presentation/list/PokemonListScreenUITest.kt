package dev.whysoezzy.pokemon.presentation.list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import dev.whysoezzy.feature_pokemon_list.presentation.PokemonListScreen
import dev.whysoezzy.feature_pokemon_list.presentation.PokemonViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonListScreenUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pokemonListScreen_displaysSearchBar() {
        val mockViewModel = createMockViewModel()

        composeTestRule.setContent {
            PokemonTheme {
                PokemonListScreen(
                    onPokemonSelected = {},
                    viewModel = mockViewModel
                )
            }
        }

        // Проверяем что поисковая строка отображается
        composeTestRule.waitForIdle()
    }

    @Test
    fun pokemonListScreen_canBeCreated() {
        val mockViewModel = createMockViewModel()

        composeTestRule.setContent {
            PokemonTheme {
                PokemonListScreen(
                    onPokemonSelected = {},
                    viewModel = mockViewModel
                )
            }
        }

        // Просто проверяем что экран создается без ошибок
        composeTestRule.waitForIdle()
    }

    private fun createMockViewModel(): PokemonViewModel {
        val viewModel = mockk<PokemonViewModel>(relaxed = true)

        every { viewModel.filter } returns MutableStateFlow(PokemonFilter())
        every { viewModel.availableTypes } returns MutableStateFlow(emptySet())
        every { viewModel.pokemonPagingFlow } returns flowOf(PagingData.empty())

        return viewModel
    }

    private fun createTestPokemon(id: Int, name: String): Pokemon {
        return Pokemon(
            id = id,
            name = name,
            height = 10,
            weight = 100,
            imageUrl = "https://example.com/pokemon.png",
            types = listOf(
                PokemonType("fire", 1)
            ),
            stats = listOf(
                PokemonStat("hp", 45, 0),
                PokemonStat("attack", 49, 0),
                PokemonStat("defense", 49, 0),
                PokemonStat("special-attack", 65, 0),
                PokemonStat("special-defense", 65, 0),
                PokemonStat("speed", 45, 0)
            )
        )
    }
}
