package dev.whysoezzy.pokemon.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.pokemon.presentation.main.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    MainScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}