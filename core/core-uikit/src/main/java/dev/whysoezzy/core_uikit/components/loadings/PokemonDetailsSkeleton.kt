package dev.whysoezzy.core_uikit.components.loadings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonElevation
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

@Composable
fun PokemonDetailsSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing().medium),
        verticalArrangement = Arrangement.spacedBy(spacing().medium)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = CustomShapes.PokemonCard,
            elevation = CardDefaults.cardElevation(
                defaultElevation = PokemonElevation.PokemonCard
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing().large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing().medium)
            ) {
                ShimmerBox(
                    modifier = Modifier.size(dimensions().pokemonImageSizeLarge),
                    shape = CircleShape
                )

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .height(16.dp)
                )

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(28.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing().small)
                ) {
                    repeat(2) {
                        ShimmerBox(
                            modifier = Modifier
                                .size(
                                    width = 80.dp,
                                    height = dimensions().chipHeightMedium
                                ),
                            shape = CustomShapes.TypeChip
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = CustomShapes.PokemonCard,
            elevation = CardDefaults.cardElevation(
                defaultElevation = PokemonElevation.PokemonCard
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing().medium),
                verticalArrangement = Arrangement.spacedBy(spacing().small)
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.height(spacing().small))

                repeat(6) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(spacing().extraSmall)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .height(14.dp)
                            )

                            ShimmerBox(
                                modifier = Modifier
                                    .size(40.dp, 14.dp)
                            )
                        }
                        ShimmerBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                        )
                    }

                }
            }

        }


    }
}

@Preview(name = "Pokemon Grid Card Skeleton - Light", showBackground = true)
@Composable
private fun PokemonGridCardSkeletonPreviewLight() {
    PokemonTheme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PokemonDetailsSkeleton(
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
    }
}

@Preview(name = "Pokemon Grid Card Skeleton - Dark", showBackground = true)
@Composable
private fun PokemonGridCardSkeletonPreviewDark() {
    PokemonTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PokemonDetailsSkeleton(
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
    }
}