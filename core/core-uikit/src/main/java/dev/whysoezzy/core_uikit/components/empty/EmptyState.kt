package dev.whysoezzy.core_uikit.components.empty

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.components.buttons.PokemonButton
import dev.whysoezzy.core_uikit.components.buttons.PokemonButtonStyle
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.StatusColors
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

enum class EmptyStateType {
    NO_RESULTS,
    NO_FAVOURITES,
    NETWORK_ERROR,
    GENERIC,
    NO_DATA
}

@Composable
fun EmptyState(
    type: EmptyStateType,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    icon: ImageVector? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    iconTint: Color? = null
) {
    val defaultTitle = when (type) {
        EmptyStateType.NO_RESULTS -> "Ничего не найдено"
        EmptyStateType.NO_FAVOURITES -> "Нет избранных покемонов"
        EmptyStateType.NETWORK_ERROR -> "Нет подключения"
        EmptyStateType.GENERIC -> "Пусто"
        EmptyStateType.NO_DATA -> "Нет данных"
    }

    val defaultDescription = when (type) {
        EmptyStateType.NO_RESULTS -> "Попробуйте изменить поисковый запрос или фильтры"
        EmptyStateType.NO_FAVOURITES -> "Добавляйте покемонов в избранное, чтобы быстро находить их позже"
        EmptyStateType.NETWORK_ERROR -> "Проверьте интернет-соединение и попробуйте снова"
        EmptyStateType.NO_DATA -> "Данные отсутствуют или еще не загружены"
        EmptyStateType.GENERIC -> "Здесь пока ничего нет"
    }

    val defaultIcon = when (type) {
        EmptyStateType.NO_RESULTS -> ImageVector.vectorResource(R.drawable.outline_search_off)
        EmptyStateType.NO_FAVOURITES -> ImageVector.vectorResource(R.drawable.outline_favorite_border_24)
        EmptyStateType.NETWORK_ERROR -> ImageVector.vectorResource(R.drawable.outline_cloud_off)
        EmptyStateType.NO_DATA -> ImageVector.vectorResource(R.drawable.outline_inbox)
        EmptyStateType.GENERIC -> ImageVector.vectorResource(R.drawable.outline_help)
    }

    val defaultIconTint = when (type) {
        EmptyStateType.NETWORK_ERROR -> StatusColors.Error
        EmptyStateType.NO_FAVOURITES -> StatusColors.Warning
        else -> MaterialTheme.colorScheme.primary
    }
    var isVisible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "empty_state_scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(spacing().large)
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(
                    color = (iconTint ?: defaultIconTint).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon ?: defaultIcon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = iconTint ?: defaultIconTint
            )
        }

        Spacer(modifier = Modifier.height(dimensions().spacerLarge))

        Text(
            text = title ?: defaultTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(dimensions().spacerSmall))

        Text(
            text = description ?: defaultDescription,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = spacing().medium)
        )

        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(dimensions().spacerLarge))

            PokemonButton(
                text = actionText,
                onClick = onActionClick,
                style = when (type) {
                    EmptyStateType.NETWORK_ERROR -> PokemonButtonStyle.PRIMARY
                    else -> PokemonButtonStyle.OUTLINED
                }
            )
        }

    }
}

object EmptyStates {
    @Composable
    fun NoSearchResults(
        onClearFilters: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        EmptyState(
            type = EmptyStateType.NO_RESULTS,
            modifier = modifier,
            actionText = "Очистить фильтры",
            onActionClick = onClearFilters
        )
    }

    @Composable
    fun NoFavourites(
        onOpenPokedex: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        EmptyState(
            type = EmptyStateType.NO_FAVOURITES,
            modifier = modifier,
            actionText = "Открыть Покедекс",
            onActionClick = onOpenPokedex
        )
    }

    @Composable
    fun NetworkError(
        onRetry: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        EmptyState(
            type = EmptyStateType.NETWORK_ERROR,
            modifier = modifier,
            actionText = "Повторить",
            onActionClick = onRetry
        )
    }

    @Composable
    fun NoData(
        modifier: Modifier = Modifier,
        actionText: String? = null,
        onActionClick: (() -> Unit)? = null
    ) {
        EmptyState(
            type = EmptyStateType.NO_DATA,
            modifier = modifier,
            actionText = actionText,
            onActionClick = onActionClick
        )
    }

    @Composable
    fun NoTeams(
        onCreateTeam: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        EmptyState(
            type = EmptyStateType.GENERIC,
            title = "Нет команд",
            modifier = modifier,
            description = "Создайте свою первую команду покемонов и начните сражения",
            icon = ImageVector.vectorResource(R.drawable.outline_group),
            actionText = "Создать команду",
            onActionClick = onCreateTeam,
            iconTint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview(name = "Empty States - Light", showBackground = true, heightDp = 700)
@Composable
private fun EmptyStatePreviewLight() {
    PokemonTheme(darkTheme = false) {
        EmptyStateShowcase()
    }
}

@Preview(name = "Empty States - Dark", showBackground = true, heightDp = 700)
@Composable
private fun EmptyStatePreviewDark() {
    PokemonTheme(darkTheme = true) {
        EmptyStateShowcase()
    }
}

@Composable
private fun EmptyStateShowcase() {
    var currentState by remember { mutableStateOf(0) }

    val states = listOf(
        "No Results",
        "No Favorites",
        "Network Error",
        "No Data",
        "No Teams"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing().medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = states[currentState],
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(spacing().small)) {
                    PokemonButton(
                        text = "Prev",
                        onClick = {
                            currentState = (currentState - 1).coerceAtLeast(0)
                        },
                        enabled = currentState > 0,
                        size = dev.whysoezzy.core_uikit.components.buttons.PokemonButtonSize.SMALL
                    )

                    PokemonButton(
                        text = "Next",
                        onClick = {
                            currentState = (currentState + 1).coerceAtMost(states.size - 1)
                        },
                        enabled = currentState < states.size - 1,
                        size = dev.whysoezzy.core_uikit.components.buttons.PokemonButtonSize.SMALL
                    )
                }
            }

            // Empty state display
            Box(modifier = Modifier.weight(1f)) {
                when (currentState) {
                    0 -> EmptyStates.NoSearchResults(onClearFilters = { })
                    1 -> EmptyStates.NoFavourites(onOpenPokedex = { })
                    2 -> EmptyStates.NetworkError(onRetry = { })
                    3 -> EmptyStates.NoData()
                    4 -> EmptyStates.NoTeams(onCreateTeam = { })
                }
            }
        }
    }
}