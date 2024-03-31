package com.example.jenstore.ui.screens.search

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.ui.screens.common.ItemListScreen
import com.example.jenstore.ui.screens.common.StoreTabRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    searchViewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToCart: (StoreDestinations) -> Unit,
    navigateToSearch: (StoreDestinations) -> Unit
) {
    val searchUiState by searchViewModel.uiState.collectAsState()

    val scope: CoroutineScope = rememberCoroutineScope()

    val searchText by searchViewModel.searchItem.collectAsState()

    val product by searchViewModel.product.collectAsState()

    Scaffold(

        topBar = {
                 SearchInputFiled(
                     value = searchText,
                     onValueChange = searchViewModel::searchQuery,
                     searchUiState = searchUiState,
                     onSearchBackClicked = {
                         searchViewModel.onBackClicked()
                     }
                 )
        },

        bottomBar = {
            if (searchUiState.isShowingSearchHome) {
                StoreTabRow(
                    allScreensBar = allScreen,
                    onTabSelected = onTabClicked,
                    currentScreen = currentScreen
                )
            }
        },
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (searchUiState.isShowingSearchHome && !searchUiState.searching) {
                SearchCategories(
                    onHairClicked = {
                        scope.launch {
                            searchViewModel.onHairClicked()
                        }
                    },
                    onShoeClicked = {
                        scope.launch {
                            searchViewModel.onShoeClicked()
                        }
                    },
                    onBagClicked = {
                        scope.launch {
                            searchViewModel.onBagClicked()
                        }
                    },
                    onClothesClicked = {
                        scope.launch {
                            searchViewModel.onClotheClicked()
                        }
                    }
                )
            } else if (!searchUiState.isShowingSearchHome) {
                ItemListScreen(
                    onListBackClicked = { /*TODO*/ },
                    items = searchUiState.item,
                    onItemClicked = {},
                    navigateToSearch = navigateToSearch,
                    navigateToCart = navigateToCart,
                    currentRoute = currentScreen
                )
            } else {
                SearchItemList(
                    items = product,
                    onItemClicked = {},
                    onBuyClicked = { /*TODO*/ }
                )
            }
        }
    }
}


@Composable
private fun SearchItemList(
    items: List<ProductItem>,
    onItemClicked: (Int) -> Unit,
    onBuyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(items, key = { item -> item.items.id }) {

        }
    }
}

@Composable
private fun SearchCategories(
    onHairClicked: () -> Unit,
    onBagClicked: () -> Unit,
    onShoeClicked: () -> Unit,
    onClothesClicked: () -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.dp_25),
                    bottom = dimensionResource(R.dimen.dp_15),
                    start = dimensionResource(R.dimen.dp_10)
                )
        )
        SearchCategoriesInfo(
            onHairClicked,
            onBagClicked,
            onShoeClicked,
            onClotheClicked = onClothesClicked
        )
    }
}



@Composable
private fun SearchCategoriesInfo(
    onHairClicked: () -> Unit,
    onBagClicked: () -> Unit,
    onShoeClicked: () -> Unit,
    onClotheClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row {
            SearchTextAndImage(
                image = R.drawable.hair1,
                text = R.string.hair,
                onClicked = onHairClicked
            )
            SearchTextAndImage(
                image = R.drawable.bag1,
                text = R.string.bags,
                onClicked = onBagClicked
            )
        }
        Row {
            SearchTextAndImage(
                image = R.drawable.shoes1,
                text = R.string.shoes,
                onClicked = onShoeClicked
            )
            SearchTextAndImage(
                image = R.drawable.clothes4,
                text = R.string.clothes,
                onClicked = onClotheClicked
            )
        }
    }
}


@Composable
fun SearchTextAndImage(
    @DrawableRes image: Int,
    @StringRes text: Int,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        onClick = onClicked,
        modifier = modifier
            .width(dimensionResource(R.dimen.dp_190))
            .padding(dimensionResource(R.dimen.dp_5))
    ) {
        Row {
            SearchText(
                text = text,
                modifier = modifier
                    .align(alignment = Alignment.CenterVertically)
            )
            Spacer(modifier = modifier.weight(1f))
            SearchImage(image = image, text = text)
        }
    }
}

@Composable
private fun SearchImage(
    @DrawableRes image: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(image),
        contentDescription = stringResource(text),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .width(dimensionResource(R.dimen.dp_150))
            .height(dimensionResource(R.dimen.dp_120))
            .clip(
                shape = RoundedCornerShape(
                    topStart = dimensionResource(R.dimen.dp_100),
                    bottomStart = dimensionResource(R.dimen.dp_100)
                )
            )
    )
}


@Composable
private fun SearchText(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(text),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.onTertiary,
        modifier = modifier
            .padding(start = dimensionResource(R.dimen.dp_5))
            .padding(end = dimensionResource(R.dimen.dp_5))
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInputFiled(
    value: String,
    onValueChange: (String) -> Unit,
    searchUiState: SearchUiState,
    onSearchBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
                if (searchUiState.searching) {
                    IconButton(
                        onClick = onSearchBackClicked
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
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
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dp_5))
    )
}