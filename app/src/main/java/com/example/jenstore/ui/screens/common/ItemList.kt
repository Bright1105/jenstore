package com.example.jenstore.ui.screens.common

import android.icu.util.Currency
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.ProductItem


@Composable
fun ItemListScreen(
    value: String,
    onValueChange: (String) -> Unit,
    onListBackClicked: () -> Unit,
    items: List<ProductItem>,
    onBuyClicked: () -> Unit,
    onItemClicked: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            ItemListTopAppBar(
                value = value,
                onValueChange = onValueChange,
                onListBackClicked = onListBackClicked
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            ItemList(
                items = items,
                onBuyClicked = onBuyClicked,
                onItemClicked = onItemClicked,
                modifier = Modifier
                    .padding()
            )
        }
    }
}


@Composable
private fun ItemList(
    items: List<ProductItem>,
    onBuyClicked: () -> Unit,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items, key = { item -> item.items.id }) {
            Item(
                item = it,
                onBuyClicked = onBuyClicked,
                modifier = modifier
                    .clickable { onItemClicked(it.items.id) }
            )
        }
    }
}


@Composable
private fun Item(
    item: ProductItem,
    onBuyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box {
        Card(
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer),
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10)),
            modifier = modifier
                .widthIn(max = dimensionResource(R.dimen.dp_350))
                .heightIn(max = dimensionResource(R.dimen.dp_300))
                .padding(
                    start = dimensionResource(R.dimen.dp_25),
                    top = dimensionResource(R.dimen.dp_30),
                )
        ) {
            Column(modifier = modifier) {
                Row {
                    ItemImage(image = item)
                    ItemText(item = item)
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.background)
                ItemButton(
                    onBuyClicked = onBuyClicked,
                    modifier = modifier
                        .align(alignment = Alignment.End)
                        .padding(
                            end = dimensionResource(R.dimen.dp_10),
                            bottom = dimensionResource(R.dimen.dp_5),
                            start = dimensionResource(R.dimen.dp_10),
                            top = dimensionResource(R.dimen.dp_5)
                        )
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
private fun ItemText(
    item: ProductItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Text(
            text = item.items.title,
            style = MaterialTheme.typography.displayLarge,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.ExtraBold,
            modifier = modifier
                .padding(
                    top = dimensionResource(R.dimen.dp_15),
                    bottom = dimensionResource(R.dimen.dp_20)
                )
        )
        Row() {
            Text(
                text = "Price:",
                style = MaterialTheme.typography.displayMedium,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )
            Text(
                text = item.items.price.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = modifier
            )
        }
    }
}


@Composable
private fun ItemImage(
    image: ProductItem,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.dp_20)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer),
        modifier = modifier
            .padding(
                top = dimensionResource(R.dimen.dp_10),
                start = dimensionResource(R.dimen.dp_10),
                bottom = dimensionResource(R.dimen.dp_10),
                end = dimensionResource(R.dimen.dp_15)
            )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://86gnbdfj-8000.uks1.devtunnels.ms/${image.image}")
                .crossfade(true)
                .build(),
            contentDescription = image.items.title,
            placeholder = painterResource(R.drawable.loading_img),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_broken_image),
            modifier = modifier
                .width(dimensionResource(R.dimen.dp_150))
                .height(dimensionResource(R.dimen.dp_200))


        )
    }
}


@Composable
private fun ItemButton(
    onBuyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onBuyClicked,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onTertiaryContainer),
        elevation = ButtonDefaults.elevatedButtonElevation(dimensionResource(R.dimen.dp_5)),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.buy),
            style = MaterialTheme.typography.labelMedium
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListTopAppBar(
    value: String,
    onValueChange: (String) -> Unit,
    onListBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            ItemInputFiled(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier
                    .padding(start = dimensionResource(R.dimen.dp_15))
            )
        },
        actions = {
            IconButton(
                onClick = onListBackClicked
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.tertiaryContainer),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputFiled(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
        },
        shape = ShapeDefaults.Small,
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.background
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            autoCorrect = true,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.background,
            unfocusedBorderColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        modifier = modifier
            .animateContentSize()
            .height(dimensionResource(R.dimen.dp_50))
    )
}