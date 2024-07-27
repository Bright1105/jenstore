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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.jenstore.Orders
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.ui.screens.common.StoreTabRow
import com.example.jenstore.ui.theme.JenstoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun CartScreen(
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onItemClicked: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateToOrders: (StoreDestinations) -> Unit,
    cartViewModel: CartViewModel
) {

    val cartUiState by cartViewModel.cartUiState.collectAsState()

    val scope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CartAppBar(
                items = cartUiState.items,
                navigateBack = navigateBack
            )
        },
        bottomBar = {
            Column {
                CartBottomBarCheckout(
                    ordersEntity = cartUiState.items,
                    onCheckoutClicked = {
                        scope.launch {
                            cartViewModel.onCheckout(it)
                            cartViewModel.clearCart(it)
                            navigateToOrders(Orders)
                        }
                    },
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
            if (cartUiState.items.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBasket,
                        contentDescription = stringResource(R.string.empty),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.dp_180))
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
                    Text(
                        text = stringResource(R.string.noItem),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                CartContent(
                    items = cartUiState.items,
                    removeItem = cartViewModel::deleteItem,
                    increaseItemCount = cartViewModel::increaseCount,
                    decreaseItemCount = cartViewModel::decreaseCount,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartAppBar(
    items: List<OrdersEntity>,
    navigateBack: () -> Unit
) {
    val resource = LocalContext.current.resources
    val itemCount = remember(items.size, resource) {
        resource.getQuantityString(
            R.plurals.cart_order_count,
            items.size, items.size
        )
    }
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
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
    )
}

@Composable
private fun CartContent(
    items: List<OrdersEntity>,
    removeItem: (OrdersEntity) -> Unit,
    increaseItemCount: (OrdersEntity) -> Unit,
    decreaseItemCount: (OrdersEntity) -> Unit,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        items(items, key = { item -> item.id } ) { order ->
            CartItem(
                count = order.countItem,
                item = order,
                removeItem = { removeItem(it) },
                increaseItemCount = { increaseItemCount(order) },
                decreaseItemCount = { decreaseItemCount(order) },
                onItemClicked = onItemClicked
            )
        }
        item {
            SummaryItem(
                subtotal = items.sumOf { it.price * it.countItem },
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
    increaseItemCount: (OrdersEntity) -> Unit,
    decreaseItemCount: (OrdersEntity) -> Unit,
    onItemClicked: (String) -> Unit,
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
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.secondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .width(dimensionResource(R.dimen.dp_180))
                .constrainAs(title) {
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
        CartPriceAndIcon(
            item = item,
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
            decreaseItemCount = { decreaseItemCount(item) },
            increaseItemCount = { increaseItemCount(item) },
            modifier = Modifier.constrainAs(quantity) {
                baseline.linkTo(price.baseline)
                end.linkTo(parent.end)
            },
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
private fun CartPriceAndIcon(
    item: OrdersEntity,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.naira_sign),
            contentDescription = stringResource(R.string.naira),
            modifier = Modifier
                .alignBy(LastBaseline)
                .size(dimensionResource(R.dimen.dp_15))
                .padding(top = dimensionResource(R.dimen.dp_3))
        )
        Text(
            text = item.price.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
        )

    }
}

@Composable
fun CartQuantitySelector(
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = dimensionResource(R.dimen.dp_30),
) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(R.string.qty),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.dp_18))
                .align(Alignment.CenterVertically)
        )
        CartProductQ(
            onClicked =  decreaseItemCount,
            imageVector = Icons.Default.Remove,
            contentDescription = R.string.decrease,
            modifier = Modifier.align(Alignment.CenterVertically),
            iconSize = iconSize,
            isButtonEnable = count > 1
        )
        Crossfade(
            targetState = count,
            modifier = Modifier
                .align(Alignment.CenterVertically), label = ""
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
            modifier = Modifier.align(Alignment.CenterVertically),
            iconSize = iconSize,
            isButtonEnable = count > 0
        )
    }
}

@Composable
private fun SummaryItem(
    subtotal: Int,
    shippingCosts: Int,
    modifier: Modifier = Modifier
) {
    val total = subtotal + shippingCosts

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
            Icon(
                painter = painterResource(R.drawable.naira_sign),
                contentDescription = stringResource(R.string.naira),
                modifier = Modifier
                    .alignBy(LastBaseline)
                    .size(dimensionResource(R.dimen.dp_15))
            )
            Text(
                text = subtotal.toString(),
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
            Icon(
                painter = painterResource(R.drawable.naira_sign),
                contentDescription = stringResource(R.string.naira),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.dp_15))
            )
            Text(
                text = shippingCosts.toString(),
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
            Icon(
                painter = painterResource(R.drawable.naira_sign),
                contentDescription = stringResource(R.string.naira),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.dp_20))
                    .padding(vertical = dimensionResource(R.dimen.dp_3))
            )
            Text(
                text = total.toString(),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
        JenStoreDivider()
    }
}



@Composable
fun JenStoreDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
    thickness: Dp = 1.dp,
) {
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness,
    )
}


@Composable
fun CartProductQ(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit,
    imageVector: ImageVector,
    contentDescription: Int,
    isButtonEnable: Boolean = true,
    iconSize: Dp
) {
    IconButton(
        onClick = onClicked,
        enabled = isButtonEnable,
        modifier = modifier
            .clip(RectangleShape)
            .border(
                dimensionResource(R.dimen.dp_2),
                MaterialTheme.colorScheme.primary,
                RectangleShape
            )
            .size(dimensionResource(R.dimen.dp_20))
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(contentDescription),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .size(iconSize)
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
            .data(image)
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
    onCheckoutClicked: (List<OrdersEntity>) -> Unit,
    ordersEntity: List<OrdersEntity>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.background(MaterialTheme.colorScheme.background)
    ) {

        JenStoreDivider()
        Row {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { onCheckoutClicked(ordersEntity) },
                shape = RectangleShape,
                enabled = ordersEntity.isNotEmpty(),
                modifier = Modifier
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


@Preview(showBackground = true)
@Composable
private fun CartPreview() {
    JenstoreTheme {
//        CartProductQ(
//            onClicked = { /*TODO*/ },
//            imageVector = Icons.Default.Add,
//            contentDescription = R.string.add,
//
//        )
    }
}

