package com.example.jenstore.ui.screens.productDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.R
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.ui.theme.JenstoreTheme


@Composable
fun ProductDetailsScreen(
    item: ProductItem?
) {
    Column {
        item?.let {
            ProductDetailsImage(image = it)
        }
        ProductDetails(
            count = 1,
            decreaseItemCount = { /*TODO*/ },
            increaseItemCount = { /*TODO*/ },
            item = item,
            onAddToCartClicked = {}
        )
    }
}

@Composable
fun ProductDetails(
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    item: ProductItem?,
    onAddToCartClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            ProductDetailsBottomBar(
                count = count,
                decreaseItemCount = decreaseItemCount,
                increaseItemCount = increaseItemCount,
                onAddToCartClicked = onAddToCartClicked,
            )
        },
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_15)))
            ProductDetailsText(item = item);
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_10)))
            ProductDetailsTextDescription(item = item)
        }
    }
}


@Composable
private fun ProductDetailsText(
    item: ProductItem?,
    modifier: Modifier = Modifier
) {
    Box {
        Card(
            shape = MaterialTheme.shapes.small,
            modifier = modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_150))
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                ),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            item?.let {
                Column(modifier = modifier) {
                    Text(
                        text = it.items.title,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = modifier
                            .padding(
                                start = dimensionResource(R.dimen.dp_10),
                                top = dimensionResource(R.dimen.dp_15)
                            )
                    )
                    Row {
                        Text(
                            text = stringResource(R.string.brand),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = modifier
                                .padding(
                                    start = dimensionResource(R.dimen.dp_10),
                                    top = dimensionResource(R.dimen.dp_15),
                                    end = dimensionResource(R.dimen.dp_5)
                                )
                        )
                        Text(
                            text = it.items.brand,
                            style = MaterialTheme.typography.displayMedium,
                            modifier = modifier
                                .padding(
                                    top = dimensionResource(R.dimen.dp_15)
                                )
                        )
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.price1),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = modifier
                                .padding(
                                    start = dimensionResource(R.dimen.dp_10),
                                    top = dimensionResource(R.dimen.dp_15),
                                    end = dimensionResource(R.dimen.dp_5)
                                )
                        )
                        Text(
                            text = it.items.price.toString(),
                            style = MaterialTheme.typography.displayMedium,
                            modifier = modifier
                                .padding(
                                    top = dimensionResource(R.dimen.dp_15)
                                )
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun ProductDetailsTextDescription(
    item: ProductItem?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer),
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.dp_200))
            .padding(
                start = dimensionResource(R.dimen.dp_10),
                end = dimensionResource(R.dimen.dp_10)
            )
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .padding(
                        top = dimensionResource(R.dimen.dp_5),
                        start = dimensionResource(R.dimen.dp_10)
                    )
            )
            HorizontalDivider()
            item?.let {
                Text(
                    text = it.items.description,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = modifier
                        .padding(
                            top = dimensionResource(R.dimen.dp_5),
                            start = dimensionResource(R.dimen.dp_10)
                        )
                )
            }
        }
    }
}


@Composable
private fun ProductDetailsImage(
    image: ProductItem,
    modifier: Modifier = Modifier
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
}


@Composable
private fun QuantitySelector(
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        IconButtons(
            onClicked = decreaseItemCount,
            painter = painterResource(R.drawable.minus),
            contentDescription = R.string.decrease
        )
        Crossfade(targetState = count) {
            Text(
                text = "$it",
                style = MaterialTheme.typography.displayLarge,
            )
        }
        IconButtons(
            onClicked = increaseItemCount,
            painter = painterResource(R.drawable.add),
            contentDescription = R.string.increase
        )
    }
}


@Composable
private fun IconButtons(
    onClicked: () -> Unit,
    painter: Painter,
    @StringRes contentDescription: Int,
) {
    IconButton(
        onClick = onClicked,
    ) {
        Icon(
            painter = painter,
            contentDescription = stringResource(contentDescription),
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier
                .width(dimensionResource(R.dimen.dp_40))
                .height(dimensionResource(R.dimen.dp_35))
        )
    }

}

@Composable
private fun ProductDetailsBottomBar(
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    onAddToCartClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {
            Text(
                text = stringResource(R.string.qty),
                style = MaterialTheme.typography.displayLarge,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.dp_20),
                        end = dimensionResource(R.dimen.dp_5)
                    )
            )
            QuantitySelector(
                count = count,
                decreaseItemCount = decreaseItemCount,
                increaseItemCount = increaseItemCount,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.dp_10),
                        end = dimensionResource(R.dimen.dp_15)
                    )
            )
            Button(
                onClick = onAddToCartClicked,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiaryContainer),
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.add),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.tertiaryContainer
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        modifier = modifier
            .height(dimensionResource(R.dimen.dp_56))
    )
}

