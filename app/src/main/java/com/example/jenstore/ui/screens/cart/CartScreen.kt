package com.example.jenstore.ui.screens.cart

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.ui.screens.common.StoreTabRow
import com.example.jenstore.ui.theme.JenstoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    //removeItem: (Int) -> Unit,
    //    increaseItemCount: (Int) -> Unit,
    //    decreaseItemCount: (Int) -> Unit,
    //onItemClicked: (Int) -> Unit,
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val cartUiState by cartViewModel.cartUiState.collectAsState()

    val scope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            Column {
                CartBottomBarCheckout(
                    onCheckoutClicked = { /*TODO*/ },
                )
                StoreTabRow(
                    allScreensBar = allScreen,
                    onTabSelected = onTabClicked,
                    currentScreen = currentScreen
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            CartContent(
                items = cartUiState.items,
                removeItem = cartViewModel::deleteItem,
                increaseItemCount = {},
                decreaseItemCount = {},
                onItemClicked = {}
            )
        }
    }
}

@Composable
private fun CartContent(
    items: List<OrdersEntity>,
    removeItem: (OrdersEntity) -> Unit,
    increaseItemCount: (Int) -> Unit,
    decreaseItemCount: (Int) -> Unit,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val resource = LocalContext.current.resources
    val itemCount = remember(items.size, resource) {
        resource.getQuantityString(
            R.plurals.cart_order_count,
            items.size, items.size
        )
    }

    LazyColumn(modifier) {
        item {
            Spacer(
                modifier = Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 1.dp))
                )
            )
            Text(
                text = stringResource(R.string.cart_order_header, itemCount),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 25.dp, vertical = 4.dp)
                    .wrapContentHeight()
            )
        }
        items(items, key = { item -> item.id } ) { order ->
            SwipeDisMissItem(
                background = { offsetX ->
                    /*
                    Background color changes from light gray to red when the
                    swipe to delete with exceeds 160.dp
                     */
                    val backgroundColor = if (offsetX < (-160).dp) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onTertiary
                    }
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(backgroundColor),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Set 4.dp padding only if offset is bigger than 160.dp
                        val padding: Dp by animateDpAsState(
                            if (offsetX > (-160).dp) 4.dp else 0.dp, label = "padding"
                        )

                        Box(
                            modifier = Modifier
                                .width(offsetX * -1)
                                .padding(padding)
                        ) {
                            // Height equals to width removing padding
                            val height = (offsetX + 8.dp) * -1
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height)
                                    .align(Alignment.Center),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.error
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Icon must be visible while in this width range
                                    if (offsetX < (-40).dp && offsetX > (-152).dp) {
                                        // Icon alpha decreases as it is about to disappear
                                        val iconAlpha: Float by animateFloatAsState(
                                            if (offsetX < (-120).dp) 0.5f else 1f, label = "iconAlpha"
                                        )

                                        Icon(
                                            imageVector = Icons.Filled.DeleteForever,
                                            contentDescription = stringResource(R.string.delete),
                                            modifier = Modifier
                                                .size(dimensionResource(R.dimen.dp_16))
                                                .graphicsLayer(alpha = iconAlpha),
                                            tint = MaterialTheme.colorScheme.background,
                                        )
                                    }
                                    /*Text opacity increases as the text is supposed to appear in
                                     the screen*/
                                    val textAlpha by animateFloatAsState(
                                        if (offsetX > (-144).dp) 0.5f else 1f, label = "textAlpha"
                                    )
                                    if (offsetX < (-120).dp) {
                                        Text(
                                            text = stringResource(R.string.remove),
                                            style = MaterialTheme.typography.displayLarge,
                                            color = MaterialTheme.colorScheme.background,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .graphicsLayer(
                                                    alpha = textAlpha
                                                )
                                        )

                                    }
                                }
                            }
                        }
                    }
                },
            ) {
                CartItem(
                    count = order.countItem,
                    item = order,
                    removeItem = removeItem,
                    increaseItemCount = { increaseItemCount(order.id) },
                    decreaseItemCount = { decreaseItemCount(order.id) },
                    onItemClicked = onItemClicked
                )
            }
        }
        item {
            SummaryItem(
                subtotal = items.sumOf { it.price.toLong() * it.countItem },
                shippingCosts = 3000
            )
        }
    }
}

@Composable
private fun CartItem(
    count: Int,
    item: OrdersEntity,
    removeItem: (OrdersEntity) -> Unit,
    increaseItemCount: () -> Unit,
    decreaseItemCount: () -> Unit,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item.id) }
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        val (divider , image, title, brand, priceSpacer, price, remove, quantity) = createRefs()
        createVerticalChain(title, brand, priceSpacer, price, chainStyle = ChainStyle.Packed)
        CartProductImage(
            image = item.image,
            contentDescription = item.image,
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = item.title,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.constrainAs(title) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = remove.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        IconButton(
            onClick = { removeItem(item) },
            modifier = Modifier
                .constrainAs(remove) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.remove),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            text = item.brand,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.constrainAs(brand) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }

        )
        Spacer(
            modifier = Modifier
                .height(dimensionResource(R.dimen.dp_8))
                .constrainAs(priceSpacer) {
                    linkTo(top = brand.bottom, bottom = price.top)
                }
        )
        Text(
            text = formatPrice(item.price.toLong()),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.constrainAs(price) {
                linkTo(
                    start = image.end,
                    end = quantity.start,
                    startMargin = 16.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        CartQuantitySelector(
            count = count,
            decreaseItemCount = decreaseItemCount,
            increaseItemCount = increaseItemCount,
            modifier = Modifier.constrainAs(quantity) {
                baseline.linkTo(price.baseline)
                end.linkTo(parent.end)
            }
        )
        JenStoreDivider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
private fun CartQuantitySelector(
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        CompositionLocalProvider(value = LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = stringResource(R.string.qty),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.dp_18))
                    .align(Alignment.CenterVertically)
            )
            CartProductQ(
                onClicked = decreaseItemCount,
                imageVector = Icons.Default.Remove,
                contentDescription = R.string.decrease,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Crossfade(
                targetState = count,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "$it",
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(min = 24.dp)
                )
            }
            CartProductQ(
                onClicked = increaseItemCount,
                imageVector = Icons.Default.Add,
                contentDescription = R.string.increase,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun SummaryItem(
    subtotal: Long,
    shippingCosts: Long,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.summary),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.tertiary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.dp_25))
                .heightIn(min = dimensionResource(R.dimen.dp_56))
                .wrapContentHeight()
        )
        Row(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_25))) {
            Text(
                text = stringResource(R.string.subtotal),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Row(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_25))) {
            Text(
                text = stringResource(R.string.shipping),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(shippingCosts),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_8)))
        JenStoreDivider()
        Row(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_25))) {
            Text(
                text = stringResource(R.string.total),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = dimensionResource(R.dimen.dp_16))
                    .wrapContentWidth(Alignment.End)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subtotal + shippingCosts),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        JenStoreDivider()
    }
}

fun formatPrice(price: Long): String {
    return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(
        BigDecimal(price).movePointLeft(2)
    )
}


@Composable
fun JenStoreDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}


@Composable
fun CartProductQ(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    imageVector: ImageVector,
    contentDescription: Int,
    isButtonEnable: Boolean = true,
) {
    IconButton(
        onClick = onClicked,
        enabled = isButtonEnable,
        modifier = modifier
            .clip(CircleShape)
            .border(
                dimensionResource(R.dimen.dp_3),
                MaterialTheme.colorScheme.primary,
                CircleShape
            )
            .size(dimensionResource(R.dimen.dp_20))
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(contentDescription),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .size(dimensionResource(R.dimen.dp_30))
        )
    }
}

@Composable
private fun CartProductImage(
    image: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://86gnbdfj-8000.uks1.devtunnels.ms/${image}")
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_img),
        modifier = modifier
            .width(dimensionResource(R.dimen.dp_100))
            .height(dimensionResource(R.dimen.dp_100))
            .clip(CircleShape)
    )
}

@Composable
private fun CartBottomBarCheckout(
    onCheckoutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.background(MaterialTheme.colorScheme.background)
    ) {

        JenStoreDivider()
        Row {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onCheckoutClicked,
                shape = RectangleShape,
                modifier = Modifier
                  //  .padding(horizontal = dimensionResource(R.dimen.dp_12), vertical = dimensionResource(R.dimen.dp_8))
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.checkout),
                    maxLines = 1,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartTopBar(
    modifier: Modifier = Modifier,
    currentScreen: StoreDestinations
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentScreen.route,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun CartPreview() {
    JenstoreTheme {
        CartProductQ(
            onClicked = { /*TODO*/ },
            imageVector = Icons.Default.Add,
            contentDescription = R.string.add
        )
    }
}

