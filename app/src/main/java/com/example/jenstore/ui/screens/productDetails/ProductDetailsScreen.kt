package com.example.jenstore.ui.screens.productDetails

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.ui.screens.cart.CartProductQ
import com.example.jenstore.ui.screens.cart.JenStoreDivider
import com.example.jenstore.ui.theme.JenstoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun ProductDetailsScreen(
    item: ProductItem?,
    route: StoreDestinations,
    onBackClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    productDetailsViewModel: ProductDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val productUiState: ProductDetailsUiState by productDetailsViewModel.uiState.collectAsState()

    Column {
        item?.let {
            ProductDetails(
                count = productDetailsViewModel.countItem.intValue,
                decreaseItemCount = {
                    productDetailsViewModel.decreaseCountItem()
                },
                increaseItemCount = {
                    productDetailsViewModel.increaseCountItem()
                },
                item = it,
                onAddToCartClicked = {
                         scope.launch {
                             productDetailsViewModel.onAddToCartClicked(it)
                             onCartClicked(MyCart)
                         }
                },
                onBackClicked = onBackClicked,
                onCartClicked = onCartClicked,
                onFavouriteClicked = {
                     productDetailsViewModel.onFavouriteClicked()
                    // check when finish setting users
                },
                onFavourite = productDetailsViewModel.onFavourite.value,
                route = route,
                totalPrice = productDetailsViewModel.countItem.intValue,
            )
        }
    }
}

@Composable
fun ProductDetails(
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    item: ProductItem,
    route: StoreDestinations,
    onAddToCartClicked: (ProductItem) -> Unit,
    onBackClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onFavouriteClicked: () -> Unit,
    onFavourite: Boolean,
    totalPrice: Int,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ProductDetailsTopBarImage(
                image = item,
                onBackClicked = onBackClicked,
                onCartClicked = onCartClicked,
                onFavouriteClicked = onFavouriteClicked,
                route = route,
                onFavourite = onFavourite
            )
        },
        bottomBar = {
          ProductDetailsBottomBar(
              item = item,
              totalPrice = totalPrice,
              onAddToCartClicked
          )
        },
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
            ProductDetailsTextAndItem(
                item = item,
                count = count,
                onDecreaseClicked = decreaseItemCount,
                onInCreaseClicked = increaseItemCount
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
            JenStoreDivider()
            ProductDetailsDescription(item = item)
        }
    }
}

@Composable
private fun ProductDetailsDescription(
    item: ProductItem,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = stringResource(R.string.details),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.dp_10))

        )
        Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_15)))
        Text(
            text = item.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.dp_15))
        )
    }
}


// try using column in the box
@Composable
private fun ProductDetailsTextAndItem(
    item: ProductItem,
    count: Int,
    onDecreaseClicked: () -> Unit,
    onInCreaseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(modifier = modifier) {
            Text(
                text = item.title.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.dp_15),
                        start = dimensionResource(R.dimen.dp_20),
                        bottom = dimensionResource(R.dimen.dp_5)
                    )
            )
            Row {
                Text(
                    text = item.brand.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.dp_20),
                            bottom = dimensionResource(R.dimen.dp_5)
                        )
                        .weight(1f)
                )
                ProductQualityIncrease(
                    count = count,
                    onDecreaseClicked = onDecreaseClicked,
                    onInCreaseClicked = onInCreaseClicked,
                    isButtonEnable = !(count >= item.itemAvailable || count <= item.itemAvailable),  //,/count >= item.items.itemAvailable,
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(R.dimen.dp_5),
                        )
                )
                // set the search screen
            }
            Row {
                Text(
                    text = "(50 Likes)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = dimensionResource(R.dimen.dp_20)
                        )
                )
                Text(
                    text = stringResource(R.string.available),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .padding(
                            end = dimensionResource(R.dimen.dp_20),
                        )
                )
                Text(
                    text = item.itemAvailable.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(R.dimen.dp_5),
                            end = dimensionResource(R.dimen.dp_20)
                        )
                )
            }
            Row {
                Icon(
                    painter = painterResource(R.drawable.naira_sign),
                    contentDescription = stringResource(R.string.naira),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.dp_35))
                        .height(dimensionResource(R.dimen.dp_20))
                        .padding(start = dimensionResource(R.dimen.dp_20))

                )
                Text(
                    text = item.price.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier

                )

            }
        }
    }
}

@Composable
private fun ProductQualityIncrease(
    count: Int,
    onInCreaseClicked: () -> Unit,
    onDecreaseClicked: () -> Unit,
    isButtonEnable: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.dp_15))
    ) {
        CartProductQ(
            onClicked = onDecreaseClicked,
            imageVector = Icons.Default.Remove,
            contentDescription = R.string.decrease,
            isButtonEnable = isButtonEnable,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Crossfade(
            targetState = count,
            animationSpec = TweenSpec(9, 5, LinearOutSlowInEasing),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "$it",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .widthIn(min = 24.dp)
            )
        }
        CartProductQ(
            onClicked = onInCreaseClicked,
            imageVector = Icons.Default.Add,
            contentDescription = R.string.increase,
            isButtonEnable = isButtonEnable,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun IconButtonQ(
    onValueCHanged: () -> Unit,
    painter: Painter,
    buttonEnable: Boolean,
    @StringRes contentDescription: Int,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = modifier
            .width(dimensionResource(R.dimen.dp_30))
            .height(dimensionResource(R.dimen.dp_30))
    ) {
        IconButton(
            onClick = onValueCHanged,
            colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.background),
            enabled = buttonEnable
        ) {
            Icon(
                painter = painter,
                contentDescription = stringResource(contentDescription),
            )
        }
    }
}

@Composable
private fun ProductDetailsTopBarImage(
    image: ProductItem,
    route: StoreDestinations,
    onBackClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onFavouriteClicked: () -> Unit,
    onFavourite: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopEnd,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://86gnbdfj-8000.uks1.devtunnels.ms/${image.image}")
                .crossfade(true)
                .build(),
            contentDescription = image.title,
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            modifier = modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_300))
        )

        IconButtonBar(
            onValueChanged = onBackClicked,
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = R.string.back,
            color = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onBackground),
            tint = MaterialTheme.colorScheme.background,
            modifier = modifier
                .align(alignment = Alignment.TopStart)
                .padding(
                    top = dimensionResource(R.dimen.dp_35),
                    start = dimensionResource(R.dimen.dp_15)
                )
        )

        IconButtonBar(
            onValueChanged = { onCartClicked(route) },
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = R.string.cart,
            color = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.background),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
                .padding(
                    top = dimensionResource(R.dimen.dp_35),
                    end = dimensionResource(R.dimen.dp_15)
                )
        )

        IconButtonBar(
            onValueChanged = onFavouriteClicked,
            imageVector = Icons.Filled.Favorite,
            contentDescription = R.string.favourite,
            color = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.background),
            tint = if (onFavourite) Color.Red else MaterialTheme.colorScheme.onBackground,
            modifier = modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(
                    end = dimensionResource(R.dimen.dp_15),
                    bottom = dimensionResource(R.dimen.dp_15)
                )

        )
    }
}

@Composable
private fun IconButtonBar(
    onValueChanged: () -> Unit,
    imageVector: ImageVector,
    @StringRes contentDescription: Int,
    color: IconButtonColors,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = modifier
            .width(28.dp)
            .height(28.dp)

    ) {
        IconButton(
            onClick = onValueChanged,
            colors = color,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = stringResource(contentDescription),
                tint = tint
            )
        }
    }
}


@Composable
private fun ProductDetailsBottomBar(
    item: ProductItem,
    totalPrice: Int,
    onAddToCartClicked: (ProductItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground),
            modifier = modifier
                .width(dimensionResource(R.dimen.dp_180))
                .padding(
                    end = dimensionResource(R.dimen.dp_35),
                    start = dimensionResource(R.dimen.dp_10),
                    bottom = dimensionResource(R.dimen.dp_10)
                )
        ) {
            Column {
                Text(
                    text = stringResource(R.string.totalPrice),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    Icon(
                        painter = painterResource(R.drawable.naira_sign),
                        contentDescription = stringResource(R.string.naira),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = modifier
                            .size(dimensionResource(R.dimen.dp_15))
                    )
                    Text(
                        text = item.price.times(totalPrice).toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

            }
        }
        Button(
            onClick = { onAddToCartClicked(item) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground),
            modifier = modifier
                .width(dimensionResource(R.dimen.dp_200))
                .padding(
                    end = dimensionResource(R.dimen.dp_10),
                    bottom = dimensionResource(R.dimen.dp_10)
                )
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = stringResource(R.string.cart)
            )
            Text(
                text = stringResource(R.string.addToCart),
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductPreview() {
    JenstoreTheme {

    }
}