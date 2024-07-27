package com.example.jenstore.ui.screens.productDetails

import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.Home
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.Search
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.model.SavedItems
import com.example.jenstore.ui.screens.cart.CartProductQ
import com.example.jenstore.ui.screens.common.MyCartIcon
import com.example.jenstore.ui.screens.profile.account.saveitems.SavedItemsViewModel
import com.example.jenstore.ui.theme.JenstoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun ProductDetailsScreen(
    productDetails: ProductDetails,
    onBackClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit,
    navigateToHome: (StoreDestinations) -> Unit,
    productDetailsViewModel: ProductDetailsViewModel
) {
    val scope: CoroutineScope = rememberCoroutineScope()

    Column {
       when (productDetails) {
           is ProductDetails.Success -> {
               productDetails.currentProduct?.let {
                   ProductDetailsItems(
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
                       totalPrice = productDetailsViewModel.countItem.intValue,
                       onSearchClicked = onSearchClicked
                   )
               }
           }
           is ProductDetails.Loading -> {
               LoadingScreen()
           }
           is ProductDetails.Error -> {
               ErrorScreen(
                   message = productDetails.message,
                   onReloadClicked = {},
                   navigateToHome = navigateToHome
               )
           }
       }
    }
}

@Composable
private fun ProductDetailsItems(
    modifier: Modifier = Modifier,
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    item: Item,
    onAddToCartClicked: (Item) -> Unit,
    onBackClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit,
    totalPrice: Int,
    savedItemsViewModel: SavedItemsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    Scaffold(
        topBar = {
            ProductDetailsTopBar(
                onBackClicked = onBackClicked,
                onSearchClicked = onSearchClicked,
                onCartClicked = onCartClicked
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
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = dimensionResource(R.dimen.dp_10),
                        end = dimensionResource(R.dimen.dp_10)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_10))
            ) {
                items(item.imageUri) { img ->
                    ProductDetailsImage(
                        image = img,
                        imageName = item.name
                    )
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15)))
            ProductDetailsTextAndItem(
                item = item,
                count = count,
                onDecreaseClicked = decreaseItemCount,
                onInCreaseClicked = increaseItemCount,
                onFavouriteClicked = { save ->
                    savedItemsViewModel.saveItems(save)
                },
                like = savedItemsViewModel.like.value
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
            ProductDetailsDescription(item = item)
        }
    }
}

@Composable
private fun ProductDetailsDescription(
    item: Item,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        shape = ShapeDefaults.Small,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.dp_20))
    ) {
        Column(
            modifier = modifier
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
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
}


// try using column in the box
@Composable
private fun ProductDetailsTextAndItem(
    item: Item,
    count: Int,
    onDecreaseClicked: () -> Unit,
    onInCreaseClicked: () -> Unit,
    onFavouriteClicked: (Item) -> Unit,
    like: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        shape = ShapeDefaults.Small,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(modifier = modifier) {
            Text(
                text = item.name.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.dp_15),
                        start = dimensionResource(R.dimen.dp_20),
                        bottom = dimensionResource(R.dimen.dp_5)
                    )
            )
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
            Row {
                Text(
                    text = stringResource(R.string.brand),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.dp_20),
                            bottom = dimensionResource(R.dimen.dp_5)
                        )
                )
                Text(
                    text = item.brand.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.dp_5),
                            bottom = dimensionResource(R.dimen.dp_5)
                        )
                        .weight(1f)
                )
                ProductQualityIncrease(
                    count = count,
                    onDecreaseClicked = onDecreaseClicked,
                    onInCreaseClicked = onInCreaseClicked,
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(R.dimen.dp_5),
                        )
                )
            }
            IconButton(
                onClick = { onFavouriteClicked(item) },
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.dp_20))
            ) {
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_2)),
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = stringResource(R.string.favourite),
                        tint = if (like) Color.Red else Color.White,
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.dp_3))
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailsNote() {

    var expand by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10)),
        shape = ShapeDefaults.Small,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(
                start = dimensionResource(R.dimen.dp_5),
                end = dimensionResource(R.dimen.dp_5),
                bottom = dimensionResource(R.dimen.dp_5)
            )
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier
                .clickable { expand = !expand }
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = stringResource(R.string.open),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.dp_20))
                    .padding(
                        end = dimensionResource(R.dimen.dp_5),
                        start = dimensionResource(R.dimen.dp_5)
                    )
            )
            Text(
                text = stringResource(R.string.deliveryRecommendation),
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Justify,
                modifier = Modifier
            )
        }
        if (expand) {
            Text(
                text = stringResource(R.string.note),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.dp_10))
            )
        }
    }
}

@Composable
private fun ProductDetailsLikeAndShareIcon(
    onClicked: () -> Unit,
    imageVector: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        IconButton(
            onClick = onClicked,
            modifier = Modifier
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
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
    Row(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.dp_15))
    ) {
        CartProductQ(
            onClicked = onDecreaseClicked,
            imageVector = Icons.Default.Remove,
            contentDescription = R.string.decrease,
            isButtonEnable = count > 1,
            modifier = Modifier
                .align(Alignment.CenterVertically),
            iconSize = dimensionResource(R.dimen.dp_30),
        )
        Crossfade(
            targetState = count,
            animationSpec = TweenSpec(9, 5, LinearOutSlowInEasing),
            modifier = Modifier
                .align(Alignment.CenterVertically), label = ""
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
            modifier = Modifier.align(Alignment.CenterVertically),
            iconSize = dimensionResource(R.dimen.dp_30),
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
private fun ProductDetailsImage(
    image: String,
    imageName: String,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_5)),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .width(dimensionResource(R.dimen.dp_300))
            .height(dimensionResource(R.dimen.dp_300))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            contentDescription = imageName,
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailsTopBar(
    onBackClicked: () -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
) {
    TopAppBar(
        modifier = Modifier,
        navigationIcon = {
            ProductDetailsTopAppBarIcons(
                onClicked = onBackClicked,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = R.string.back
            )
        },
        title = {
            Text(
                text = stringResource(R.string.details),
                style = MaterialTheme.typography.titleSmall,
            )
        },
        actions = {
            ProductDetailsTopAppBarIcons(
                onClicked = { onSearchClicked(Search) },
                imageVector = Icons.Default.Search,
                contentDescription = R.string.search
            )
            MyCartIcon(
                onCartClicked = onCartClicked
            )
        }
    )
}

@Composable
private fun ProductDetailsTopAppBarIcons(
    onClicked: () -> Unit,
    imageVector: ImageVector,
    contentDescription: Int,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClicked
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(contentDescription),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier,
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
    item: Item,
    totalPrice: Int,
    onAddToCartClicked: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Card(
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(dimensionResource(R.dimen.dp_50)),
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_10))
                .height(dimensionResource(R.dimen.dp_35))

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.totalPrice),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = dimensionResource(R.dimen.dp_5))
                )
                Icon(
                    painter = painterResource(R.drawable.naira_sign),
                    contentDescription = stringResource(R.string.naira),
                    tint = MaterialTheme.colorScheme.background,
                    modifier = modifier
                        .size(dimensionResource(R.dimen.dp_15))
                )
                Text(
                    text = item.price.times(totalPrice).toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Button(
            onClick = { onAddToCartClicked(item) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_50))
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
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

@Composable
private fun LoadingScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(dimensionResource(R.dimen.dp_50)),
        )
    }
}

@Composable
private fun ErrorScreen(
    message: String?,
    navigateToHome: (StoreDestinations) -> Unit,
    onReloadClicked: () -> Unit
) {

    var showDialog by rememberSaveable {
        mutableStateOf(true)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        if (showDialog) {
            AlertDialog(
                title = {
                    Text(
                        text = "Error",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                },
                text = {
                    if (message != null) {
                        Text(text = message)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            navigateToHome(Home)
                            showDialog = false
                        },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
                confirmButton = {
                    Button(onClick = { onReloadClicked() }) {
                        Text(text = stringResource(R.string.reload))
                    }
                },
                onDismissRequest = { showDialog = false },
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