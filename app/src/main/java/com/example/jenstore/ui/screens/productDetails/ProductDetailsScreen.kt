package com.example.jenstore.ui.screens.productDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            onAddToCartClicked = { /*TODO*/ },
            decreaseItemCount = { /*TODO*/ },
            increaseItemCount = { /*TODO*/ },
            item = item
        )
    }
}

@Composable
fun ProductDetails(
    count: Int,
    onAddToCartClicked: () -> Unit,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    item: ProductItem?,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            ProductDetailsBottomBar(
                count = count,
                decreaseItemCount = decreaseItemCount,
                increaseItemCount = increaseItemCount,
                onAddToCartClicked = onAddToCartClicked
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            ProductDetailsText(item = item)
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_20)))
            HorizontalDivider()
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
            shape = MaterialTheme.shapes.small
        ) {
            item?.let {
                Column(modifier = modifier) {
                    Text(text = it.items.title)
                    Row {
                        Text(text = stringResource(R.string.brand))
                        Text(text = it.items.brand)
                    }
                    Text(text = it.items.price.toString())
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
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.description)
            )
            HorizontalDivider()
            item?.let {
                Text(text = it.items.description)
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
            .height(dimensionResource(R.dimen.dp_400))
    )
}


@Composable
private fun QuantitySelector(
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    onAddToCartClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(R.string.qty)
        )
        IconButtons(
            onClicked = decreaseItemCount,
            painter = painterResource(R.drawable.minus),
            contentDescription = R.string.decrease
        )
        Crossfade(targetState = count) {
            Text(
                text = "$it"
            )
        }
        IconButtons(
            onClicked = increaseItemCount,
            painter = painterResource(R.drawable.add),
            contentDescription = R.string.increase
        )
        Button(
            onClick = onAddToCartClicked
        ) {
            Text(
                text = stringResource(R.string.add)
            )
        }
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
            contentDescription = stringResource(contentDescription)
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
            QuantitySelector(
                count = count,
                decreaseItemCount = decreaseItemCount,
                increaseItemCount = increaseItemCount,
                onAddToCartClicked = onAddToCartClicked
            )
        },
        modifier = modifier
    )
}

