package com.example.jenstore.ui.screens.cart



//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//suspend
//        /**
// * Holds the Swipe to dismiss composable, its animation and the current state
// */
//fun SwipeDisMissItem(
//    modifier: Modifier = Modifier,
//    direction: Set<SwipeToDismissBoxValue> = setOf(SwipeToDismissBoxValue.EndToStart),
//    enter: EnterTransition = expandVertically(),
//    exit: ExitTransition = shrinkVertically(),
//    background: @Composable (offset: Dp) -> Unit,
//    content: @Composable (isDismissed: Boolean) -> Unit
//) {
//    // Hold the current state from the swipe to Dismiss composable
//    val dismissState = rememberSwipeToDismissBoxState()
//    // Boolean value used for hiding the item if the current state is dismissed
//    val isDismissed = dismissState.dismiss(SwipeToDismissBoxValue.EndToStart)
//    // Returns the swiped valur in dp
//    val offset = with(LocalDensity.current) { dismissState.requireOffset().toDp() }
//
//
//    AnimatedVisibility(
//        visible = isDismissed,
//        enter = enter,
//        exit = exit,
//        modifier = modifier
//    ) {
//        SwipeToDismissBox(
//            modifier = modifier,
//            state = dismissState,
//            enableDismissFromEndToStart = true,
//            enableDismissFromStartToEnd = false,
//            backgroundContent = {
//
//            }
//        )
//
//    }
//}