package dev.whysoezzy.core_uikit.components.loadings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun PokemonGridCardSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.75f),
        shape = CustomShapes.PokemonCard,
        elevation = CardDefaults.cardElevation(
            defaultElevation = PokemonElevation.PokemonCard
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing().medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(spacing().small),
                contentAlignment = Alignment.Center
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .size(dimensions().pokemonImageSizeMedium * 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(dimensions().spacerSmall))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing().extraSmall)
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(14.dp)
                )

                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                )

                Spacer(modifier = Modifier.height(dimensions().spacerExtraSmall))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        spacing().extraSmall,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(spacing().extraSmall),
                    modifier = Modifier.fillMaxWidth()

                ) {
                    repeat(2) {
                        ShimmerBox(
                            modifier = Modifier
                                .size(width = 60.dp, height = dimensions().chipHeightSmall),
                            shape = CustomShapes.TypeChip
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
            PokemonGridCardSkeleton(
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
            PokemonGridCardSkeleton(
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        }
    }
}