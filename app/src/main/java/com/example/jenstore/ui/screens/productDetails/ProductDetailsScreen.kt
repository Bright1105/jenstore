package com.example.jenstore.ui.screens.productDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.ui.theme.JenstoreTheme
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
                onFavouriteClicked = {},
                route = route
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
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            ProductDetailsTopBarImage(
                image = item,
                onBackClicked = onBackClicked,
                onCartClicked = onCartClicked,
                onFavouriteClicked = onFavouriteClicked,
                route = route
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
            ProductDetailsClotheSize()
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
            text = stringResource(R.string.description)
        )
        Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_15)))
        Text(
            text = item.items.description
        )
    }
}


@Composable
private fun ProductDetailsClotheSize() {
    Column {
        Text(text = "Size")
        Row {
            ProductClotheSizeSelector(text = "S")
            ProductClotheSizeSelector(text = "M")
            ProductClotheSizeSelector(text = "L")
            ProductClotheSizeSelector(text = "XL")
            ProductClotheSizeSelector(text = "XXL")
            ProductColorSelector()
        }
    }
}

@Composable
private fun ProductClotheSizeSelector(text: String,modifier: Modifier = Modifier) {
    Row {
        IconButton(onClick = { /*TODO*/ }) {
            Text(text)
        }
    }
}

@Composable
private fun ProductColorSelector(modifier: Modifier = Modifier) {
    Card(shape = MaterialTheme.shapes.small) {
        Column {
            Text(
                text = "white",
                color = Color.White,
                modifier = modifier
                    .background(Color.White, MaterialTheme.shapes.small)
                    .border(2.dp, Color.LightGray)
            )
            Text(
                text = "b",
                color = Color.Black,
                modifier = modifier
                    .background(Color.Black, MaterialTheme.shapes.small)
            )
            Text(
                text = "y",
                color = Color.Yellow,
                modifier = modifier
                    .background(Color.Yellow, MaterialTheme.shapes.small)
            )
            Text(
                text = "r",
                color = Color.Red,
                modifier = modifier
                    .background(Color.Red, MaterialTheme.shapes.small)
            )
        }
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
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                text = item.items.title.uppercase(),
                style = MaterialTheme.typography.titleSmall,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.dp_10),
                        bottom = dimensionResource(R.dimen.dp_10)
                    )
            )
            Text(
                text = item.items.brand.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                style = MaterialTheme.typography.labelMedium,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.dp_10),
                        bottom = dimensionResource(R.dimen.dp_10)
                    )
            )
            ProductQualityIncrease(
                count = count,
                onDecreaseClicked = onDecreaseClicked,
                onInCreaseClicked = onInCreaseClicked,
                modifier = modifier
                    .align(alignment = Alignment.End)
            )
            Row(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = "20"
                )
                Text(
                    text = "(50 Likes)"
                )
                Text(
                    text = "Available in stock",
                    modifier = modifier
                        .align(alignment = Alignment.CenterVertically)
                )
            }
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
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .padding(
                            bottom = dimensionResource(R.dimen.dp_2)
                        )

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
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10)),
        modifier = modifier
            .width(dimensionResource(R.dimen.dp_120))
            .height(dimensionResource(R.dimen.dp_30))
    ) {
        Row {
            TextButton(
                onClick = onDecreaseClicked,
                modifier = modifier
            ) {
                Text(
                    text = "-",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier
                )
            }
            Crossfade(
                targetState = count
            ) {
                Text(
                    text = "$it",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier
                )
            }
            TextButton(
                onClick = onInCreaseClicked
            ) {
                Text(
                    text = "+",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
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
        IconButton(
            onClick = onBackClicked,
            modifier = modifier
                .align(alignment = Alignment.TopStart)
                .padding(top = dimensionResource(R.dimen.dp_35))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.background,
                modifier = modifier
                    .background(
                        MaterialTheme.colorScheme.onBackground,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(
                        top = dimensionResource(R.dimen.dp_3),
                        start = dimensionResource(R.dimen.dp_3),
                        end = dimensionResource(R.dimen.dp_3),
                        bottom = dimensionResource(R.dimen.dp_3)
                    )
            )
        }
        IconButton(
            onClick = { onCartClicked(route) },
            modifier = modifier
                .align(Alignment.TopEnd)
                .padding(top = dimensionResource(R.dimen.dp_35))
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = modifier
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(
                        top = dimensionResource(R.dimen.dp_3),
                        start = dimensionResource(R.dimen.dp_3),
                        end = dimensionResource(R.dimen.dp_3),
                        bottom = dimensionResource(R.dimen.dp_3)
                    )
            )
        }
        IconButton(
            onClick = onFavouriteClicked,
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = dimensionResource(R.dimen.dp_10))
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = modifier
                    .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.small)
                    .padding(
                        top = dimensionResource(R.dimen.dp_3),
                        start = dimensionResource(R.dimen.dp_3),
                        end = dimensionResource(R.dimen.dp_3),
                        bottom = dimensionResource(R.dimen.dp_3)
                    )

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
                    text = "Total Price",
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
                text = "Add to cart",
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