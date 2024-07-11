package com.example.jenstore.ui.screens.profile.account.saveitems

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.jenstore.data.model.SavedItems
import com.example.jenstore.ui.screens.cart.CartQuantitySelector
import com.example.jenstore.ui.screens.common.MyCartIcon
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveItemsScreen(
    onSearchClicked: (StoreDestinations) -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    navigateToHome: (StoreDestinations) -> Unit,
    popUp: () -> Unit,
    onSavedItemClicked: (String) -> Unit,
    savedItemsViewModel: SavedItemsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val savedItems = savedItemsViewModel.savedItems.collectAsState(initial = null)
    val count = savedItemsViewModel.count.collectAsState()

    Scaffold(
        topBar = {
            SavedItemsTopAppBar(
                onSearchClicked = onSearchClicked,
                onCartClicked = onCartClicked,
                onBackClicked = popUp
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            if (savedItems.value?.isEmpty() == true) {
                SavedItemsEmptyList(
                    onShoppingClicked = navigateToHome
                )
            } else {
                savedItems.value?.let { it1 ->
                    SavedItemsList(
                        savedItems = it1,
                        onRemoveClicked = { delete ->
                            savedItemsViewModel.deleteSavedItems(delete)
                        },
                        onSavedItemClicked,
                        navigateToCart = onCartClicked,
                        onAddToCartClicked = { add ->
                            savedItemsViewModel.onAddToCartClicked(add)
                        },
                        count = count.value,
                        decreaseItemCount = {
                            savedItemsViewModel.decreaseCount()
                        },
                        increaseItemCount = {
                            savedItemsViewModel.increaseCount()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SavedItemsEmptyList(
    onShoppingClicked: (StoreDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_50)))
        Card(
            shape = CircleShape,
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = stringResource(R.string.favourite),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.dp_150))
                    .padding(dimensionResource(R.dimen.dp_20))
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
        Text(
            text = stringResource(R.string.dont),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        Text(
            text = stringResource(R.string.found),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.dp_10))
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
        Button(
            onClick = { onShoppingClicked(Home) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(
                text = stringResource(R.string.continueShopping),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
private fun SavedItemsList(
    savedItems: List<SavedItems>,
    onRemoveClicked: (String) -> Unit,
    onSavedItemClicked: (String) -> Unit,
    navigateToCart: (StoreDestinations) -> Unit,
    onAddToCartClicked: (SavedItems) -> Unit,
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit
) {
    LazyColumn {
        items(savedItems.sortedByDescending {
            it.productItem.dateCreated
        }, key = { savedItem -> savedItem.id }) { savedItem ->
            SavedItemCard(
                savedItem = savedItem,
                onRemoveClicked,
                onSavedItemClicked = onSavedItemClicked,
                navigateToCart = navigateToCart,
                onAddToCartClicked = onAddToCartClicked,
                count = count,
                decreaseItemCount = decreaseItemCount,
                increaseItemCount = increaseItemCount
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SavedItemCard(
    savedItem: SavedItems,
    onRemoveClicked: (String) -> Unit,
    onAddToCartClicked: (SavedItems) -> Unit,
    navigateToCart: (StoreDestinations) -> Unit,
    onSavedItemClicked: (String) -> Unit,
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit
) {
    var addToCart by rememberSaveable {
        mutableStateOf(false)
    }
    if (addToCart) {
        BasicAlertDialog(
            onDismissRequest = {
                addToCart = !addToCart
            },
            content = {
                Card(
                    elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_5)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.dp_15)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.dp_10)),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = stringResource(R.string.numberOfItems),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(R.dimen.dp_10))
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
                    CartQuantitySelector(
                        count = count,
                        decreaseItemCount = decreaseItemCount,
                        increaseItemCount = increaseItemCount,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(R.dimen.dp_15)),
                        horizontalArrangement =  Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                addToCart = false
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
                            modifier = Modifier
                                .width(dimensionResource(R.dimen.dp_120))
                                .padding(end = dimensionResource(R.dimen.dp_10))
                                .border(
                                    width = dimensionResource(R.dimen.dp_2),
                                    color = MaterialTheme.colorScheme.error,
                                    shape = RoundedCornerShape(dimensionResource(R.dimen.dp_15))
                                ),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.dp_15))
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Button(
                            onClick = {
                                onAddToCartClicked(savedItem)
                                addToCart = false
                                navigateToCart(MyCart)
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
                            modifier = Modifier
                                .width(dimensionResource(R.dimen.dp_120))
                                .padding(start = dimensionResource(R.dimen.dp_10))
                                .border(
                                    width = dimensionResource(R.dimen.dp_2),
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(dimensionResource(R.dimen.dp_15))
                                ),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.dp_15))
                        ) {
                            Text(
                                text = stringResource(R.string.confirm),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        )
    }
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_5)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Row(
                modifier = Modifier
                    .clickable { onSavedItemClicked(savedItem.id) }
                    .padding(dimensionResource(R.dimen.dp_10))
            ) {
                SavedItemsImage(
                    savedItem = savedItem
                )
                SavedItemsText(
                    savedItem = savedItem,
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.dp_10))
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5)))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5)))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.Start),
            ) {
                TextButton(
                    onClick = { onRemoveClicked(savedItem.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(
                            end = dimensionResource(R.dimen.dp_120)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.remove).replaceFirstChar {
                            it.uppercaseChar()
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(
                    onClick = { addToCart = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(
                            end = dimensionResource(R.dimen.dp_15),
                            bottom = dimensionResource(R.dimen.dp_10)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.addToCart),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedItemsText(
    savedItem: SavedItems,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier

    ) {
        Text(
            text = savedItem.productItem.name,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 15.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(dimensionResource(R.dimen.dp_200))
                .height(dimensionResource(R.dimen.dp_50))
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
        Row(
            modifier = Modifier

        ) {
            Icon(
                painter = painterResource(R.drawable.naira_sign),
                contentDescription = stringResource(R.string.naira),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.dp_15))
            )
            Text(
                text = savedItem.productItem.price.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
private fun SavedItemsImage(
    savedItem: SavedItems
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(savedItem.productItem.imageUri[0])
            .crossfade(true)
            .build(),
        contentDescription = savedItem.productItem.name,
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_img),
        modifier = Modifier
            .width(120.dp)
            .height(150.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SavedItemsTopAppBar(
    onSearchClicked: (StoreDestinations) -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
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
                text = stringResource(R.string.savedItem),
                style = MaterialTheme.typography.titleSmall
            )
        },
        actions = {
            IconButton(
                onClick = { onSearchClicked(Search) }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            MyCartIcon(
                onCartClicked = onCartClicked
            )
        }
    )
}