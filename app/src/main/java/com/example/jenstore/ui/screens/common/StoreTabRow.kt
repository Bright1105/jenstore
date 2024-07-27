package com.example.jenstore.ui.screens.common


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import java.util.Locale


@Composable
fun StoreTabRow(
    allScreensBar: List<StoreDestinations>,
    onTabSelected: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations
) {
    val backgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background,
        label = "background color"
    )
    Surface(
        Modifier
            .height(TabHeight)
            .fillMaxWidth(),
        color = backgroundColor
    ) {
        HorizontalDivider()
        Row(
            Modifier
                .selectableGroup()
                .padding(start = dimensionResource(R.dimen.dp_10))
        ) {
            allScreensBar.forEach {
                StoreTab(
                    text = it.route,
                    icon = if (currentScreen == it) it.selectedIcon else it.unSelectedIcon,
                    onSelected = { onTabSelected(it) },
                    selected = currentScreen == it
                )
            }
        }
    }
}

@Composable
private fun StoreTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.onBackground
    val durationMillis = if (selected) TabFadeInAnimationDuration else TabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis = TabFadeInAnimationDelay
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) color else color.copy(alpha = InactiveTabOpacity),
        animationSpec = animSpec, label = ""
    )
    Row(
        modifier = modifier
            .padding(dimensionResource(R.dimen.dp_16))
            .animateContentSize()
            .height(TabHeight)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = tabTintColor
        )
        if (selected) {
            Spacer(modifier = modifier.width(dimensionResource(R.dimen.dp_5)))
            Text(text.uppercase(Locale.getDefault()), color = tabTintColor)
        }
    }
}



private val TabHeight = 56.dp
private const val InactiveTabOpacity = 0.60f

private const val TabFadeInAnimationDuration = 150
private const val TabFadeInAnimationDelay = 100
private const val TabFadeOutAnimationDuration = 100
