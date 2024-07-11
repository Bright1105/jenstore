package com.example.jenstore.ui.screens.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
/**
 * Holds the Swipe to dismiss composable, its animation and the current state
 */
fun SwipeDisMissItem(
    modifier: Modifier = Modifier,
    direction: Set<DismissDirection> = setOf(DismissDirection.EndToStart),
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(),
    background: @Composable (offset: Dp) -> Unit,
    content: @Composable (isDismissed: Boolean) -> Unit
) {
    // Hold the current state from the swipe to Dismiss composable
    val dismissState = rememberDismissState()
    // Boolean value used for hiding the item if the current state is dismissed
    val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
    // Returns the swiped valur in dp
    val offset = with(LocalDensity.current) { dismissState.offset.value.toDp() }


    AnimatedVisibility(
        visible = !isDismissed,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        SwipeToDismiss(
            modifier = modifier,
            state = dismissState,
            directions = direction,
            background = { background(offset) },
            dismissContent = { content(isDismissed) }
        )

    }
}