package dev.whysoezzy.pokemon.presentation.list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonListScreenUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pokemonListScreen_displaysLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            PokemonTheme {
            }
        }
    }
}
