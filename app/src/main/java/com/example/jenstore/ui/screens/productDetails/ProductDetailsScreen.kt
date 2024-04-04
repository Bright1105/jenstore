package com.example.jenstore.ui.screens.productDetails

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.ui.theme.JenstoreTheme
import com.example.jenstore.ui.theme.Shapes
import java.util.Locale


@Composable
fun ProductDetailsScreen(
    item: ProductItem?,
    route: StoreDestinations,
    onBackClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    productDetailsViewModel: ProductDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Column {
        item?.let {
            ProductDetails(
                count = 1,
                decreaseItemCount = { /*TODO*/ },
                increaseItemCount = { /*TODO*/ },
                item = it,
                onAddToCartClicked = {},
                onBackClicked = onBackClicked,
                onCartClicked = onCartClicked,
                onFavouriteClicked = {
                     productDetailsViewModel.onFavouriteClicked()
                    // check when finish setting users
                },
                onFavourite = productDetailsViewModel.onFavourite.value,
                route = route,
                onColorSelected = { color ->
                      productDetailsViewModel.onColorSelected(color)
                },
                currentColor = productDetailsViewModel.currentColor.value,
                selectedSize = productDetailsViewModel.currentSize.value,
                onSizeSelected = { size ->
                    productDetailsViewModel.onSizeSelected(size)
                }
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
    onAddToCartClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onFavouriteClicked: () -> Unit,
    onFavourite: Boolean,
    onColorSelected: (Color) -> Unit,
    selectedSize: String,
    onSizeSelected: (String) -> Unit,
    currentColor: Color,
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
              onAddToCartClicked
          )
        },
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_10)))
            ProductDetailsTextAndItem(
                item = item,
                count = count,
                onDecreaseClicked = decreaseItemCount,
                onInCreaseClicked = increaseItemCount
            )
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_10)))
            ProductDetailsClotheSize(
                allSize = size,
                selectedSize = selectedSize,
                onSizeSelected = onSizeSelected,
                onColorSelected,
                currentColor,
            )
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
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.titleMedium,
            textDecoration = TextDecoration.Underline,
            modifier = modifier
                .padding(start = dimensionResource(R.dimen.dp_10))

        )
        Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_15)))
        Text(
            text = item.items.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
                .padding(start = dimensionResource(R.dimen.dp_15))
        )
    }
}



val size: List<String> = listOf(
    "S",
    "M",
    "L",
    "XL",
    "XXL"
)


@Composable
private fun ProductDetailsClotheSize(
    allSize: List<String>,
    selectedSize: String,
    onSizeSelected: (String) -> Unit,
    onColorSelected: (Color) -> Unit,
    currentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.size),
            style = MaterialTheme.typography.titleSmall,
            modifier = modifier
                .padding(
                    top = dimensionResource(R.dimen.dp_15),
                    start = dimensionResource(R.dimen.dp_15),
                    bottom = dimensionResource(R.dimen.dp_5)
                )
        )
        Row(
            modifier = modifier
                .selectableGroup()
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                )
        ) {
           allSize.forEach {
               ProductClotheSizeSelector(
                   text = it,
                   selectedSize = selectedSize == it,
                   onSizeSelected = { onSizeSelected(it) },
                   modifier = modifier
               )
           }
            ProductColorColumn(
                allColor = color,
                onColorSelected = onColorSelected,
                currentColor = currentColor,
            )
        }
    }
}

@Composable
private fun ProductClotheSizeSelector(
    text: String,
    selectedSize: Boolean,
    onSizeSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .selectable(
                selected = selectedSize,
                onClick = onSizeSelected
            )
            .padding(
                end = dimensionResource(R.dimen.dp_8)
            )
    ) {
        Button(
            onClick = onSizeSelected,
            shape = CircleShape,
            border = BorderStroke(2.dp, color = if (selectedSize) Color.Blue else MaterialTheme.colorScheme.outlineVariant),
            modifier = modifier
                .width(55.dp)
                .height(40.dp)
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ProductColorColumn(
    allColor: List<Color>,
    onColorSelected: (Color) -> Unit,
    currentColor: Color
) {
    Column(
        Modifier
            .selectableGroup()
    ) {
        allColor.forEach {
            ProductColor(
                text = "O",
                onSelected = { onColorSelected(it) },
                selected = currentColor == it,
                color = it,
            )
        }
    }
}

val color: List<Color> = listOf(
    Color.Yellow,
    Color.Black,
    Color.DarkGray,
    Color.Green
)

@Composable
private fun ProductColor(
    text: String,
    onSelected: () -> Unit,
    selected: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onSelected
            )
            .padding(
                bottom = dimensionResource(R.dimen.dp_3),
            )
    ) {
        Text(
            text = text,
            color = color,
            modifier = modifier
                .background(color, shape = CircleShape)
                .border(
                    2.dp,
                    color = if (selected) Color.Blue else MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape
                )
                .width(30.dp)
                .height(30.dp)

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
                text = item.items.title.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = modifier
                    .padding(
                        top = dimensionResource(R.dimen.dp_15),
                        start = dimensionResource(R.dimen.dp_20),
                        bottom = dimensionResource(R.dimen.dp_5)
                    )
            )
            Row {
                Text(
                    text = item.items.brand.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = modifier
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
                    modifier = modifier
                        .padding(
                            bottom = dimensionResource(R.dimen.dp_5),
                        )
                )
            }
            Row {
                Text(
                    text = "(50 Likes)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = modifier
                        .weight(1f)
                        .padding(
                            start = dimensionResource(R.dimen.dp_20)
                        )
                )
                Text(
                    text = stringResource(R.string.available),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = modifier
                        .padding(
                            end = dimensionResource(R.dimen.dp_20),
                        )
                )
                Text(
                    text = "20",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = modifier
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
                    modifier = modifier
                        .width(dimensionResource(R.dimen.dp_35))
                        .height(dimensionResource(R.dimen.dp_20))
                        .padding(start = dimensionResource(R.dimen.dp_20))

                )
                Text(
                    text = item.items.price.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier

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
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10)),
        modifier = modifier
            .padding(
                end = dimensionResource(R.dimen.dp_10)
            )
    ) {
        Row {
            IconButtonQ(
                onValueCHanged = onDecreaseClicked,
                painter = painterResource(R.drawable.minus),
                contentDescription = R.string.decrease,
                buttonEnable = true,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.dp_15),
                        top = dimensionResource(R.dimen.dp_5),
                        end = dimensionResource(R.dimen.dp_5)
                    )
            )
            Crossfade(
                targetState = count,
                //  animationSpec = TweenSpec(2, 1, FastOutLinearInEasing)
            ) {
                Text(
                    text = "$it",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier
                        .padding(
                            start = dimensionResource(R.dimen.dp_5),
                        )
                )
            }
            IconButtonQ(
                onValueCHanged = onInCreaseClicked,
                painter = painterResource(R.drawable.add),
                contentDescription = R.string.increase,
                buttonEnable = true,
                modifier = modifier
                    .padding(
                        top = dimensionResource(R.dimen.dp_5),
                        end = dimensionResource(R.dimen.dp_15),
                        start = dimensionResource(R.dimen.dp_5)
                    )
            )
        }
    }
}

@Composable
private fun IconButtonQ(
    onValueCHanged: () -> Unit,
    painter: Painter,
    @StringRes contentDescription: Int,
    buttonEnable: Boolean,
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
            contentDescription = image.items.title,
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
    onAddToCartClicked: () -> Unit,
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
                        text = item.items.price.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

            }
        }
        Button(
            onClick = onAddToCartClicked,
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