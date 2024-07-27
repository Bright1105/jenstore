package com.example.jenstore.ui.screens.search

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.model.PaginationProducts
import com.example.jenstore.ui.screens.common.ItemAndImage
import com.example.jenstore.ui.screens.common.ItemListScreen
import com.example.jenstore.ui.screens.common.StoreTabRow
import com.example.jenstore.ui.screens.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    searchViewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToCart: (StoreDestinations) -> Unit,
    navigateToSearch: (StoreDestinations) -> Unit,
    onItemClicked: (String) -> Unit,
    homeViewModel: HomeViewModel
) {
    val searchUiState by searchViewModel.uiState.collectAsState()

    val scope: CoroutineScope = rememberCoroutineScope()

    val searchText by searchViewModel.searchItem.collectAsState()

    val products = searchViewModel.product.collectAsState(null)

    val error by searchViewModel.error.collectAsState()




    // val searchPagingItems = searchViewModel.product.map {  paging ->
    //        paging.filter {
    //            it.itemType == searchViewModel.itemT.value
    //        }
    //    }.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SearchInputFiled(
                value = searchText,
                onValueChange = searchViewModel::searchQuery,
                searchUiState = searchUiState,
                onSearchBackClicked = {
                    searchViewModel.onSearchBackClicked()
                }
            )
        },
        bottomBar = {
            if (searchUiState.isShowingSearchHome && !searchUiState.searching) {
                StoreTabRow(
                    allScreensBar = allScreen,
                    onTabSelected = onTabClicked,
                    currentScreen = currentScreen
                )
            }
        },

        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            if (searchUiState.isShowingSearchHome && !searchUiState.searching) {
                SearchCategories(
                    onHairClicked = {
                        scope.launch {
                          //  searchViewModel.itemT.value = "hair"
                            searchViewModel.onHairClicked()
                        }
                    },
                    onAccessoriesClicked = {
                        scope.launch {
                            searchViewModel.onAccessoriesClicked()
                        }
                    },
                    onBagClicked = {
                       scope.launch {
                          // searchViewModel.itemT.value = "bag"
                           searchViewModel.onBagClicked()
                       }
                    },
                    onMakeupClicked = {
                        scope.launch {
                          // searchViewModel.itemT.value = "clothe"
                            searchViewModel.onMakeupClicked()
                        }
                    }
                )
            } else if(!searchUiState.isShowingSearchHome && !searchUiState.searching)  {
                searchUiState.item?.map {  paging ->
                    paging.filter { item ->
                        item.itemType == searchViewModel.uiState.value.typeOfProducts
                    }
                }?.let { it1 ->
                    ItemListScreen(
                        onListBackClicked = {
                            searchViewModel.onBackClicked()
                        },
                        items = it1.collectAsLazyPagingItems(),
                        onItemClicked = onItemClicked,
                        navigateToSearch = navigateToSearch,
                        navigateToCart = navigateToCart,
                        currentRoute = currentScreen
                    )
                }
            } else {
                products.value?.collectAsLazyPagingItems().let { it1 ->
                    if (it1 != null) {
                        SearchItemList(
                            items = it1,
                            onItemClicked = onItemClicked,
                            onBuyClicked = { /*TODO*/ }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SearchItemList(
    items: LazyPagingItems<Item>,
    onItemClicked: (String) -> Unit,
    onBuyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = items.loadState) {
        if (items.loadState.refresh is LoadState.Error) {
            Toast.makeText(context, "error:" + (items.loadState.refresh as LoadState.Error).error.message, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        if (items.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                GridCells.Fixed(2),
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.dp_15))
                    .padding(start = dimensionResource(R.dimen.dp_10))

            ) {
                itemsPaging(items, key = { item -> item.id }) { item ->
                    item?.let {
                        SearchProduct(
                            item = item,
                            onItemClicked = onItemClicked,
                            onBuyClicked = onBuyClicked,
                            modifier = modifier
                                .padding(bottom = dimensionResource(R.dimen.dp_10))
                        )
                    }
                }
                item {
                    if (items.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }

            }
        }
    }

}

fun <T : Any> LazyGridScope.itemsPaging(
    items: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = if (key == null) null else { index ->
            val item = items.peek(index)
            if (item == null) {
                com.example.jenstore.ui.screens.common.PagingPlaceholderKey(index)
            } else {
                key(item)
            }
        }
    ) { index ->
        itemContent(items[index])
    }
}

@SuppressLint("BanParcelableUsage")
private data class PagingPlaceholderKey(private val index: Int) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) = parcel.writeInt(index)

    override fun describeContents(): Int = 0

    companion object {
        @Suppress("unused")
        @JvmField
        val CREATOR: Parcelable.Creator<PagingPlaceholderKey> =
            object : Parcelable.Creator<PagingPlaceholderKey> {
                override fun createFromParcel(parcel: Parcel) =
                    PagingPlaceholderKey(parcel.readInt())
                override fun newArray(size: Int) = arrayOfNulls<PagingPlaceholderKey?>(size)
            }
    }
}


@Composable
private fun SearchProduct(
    item: Item,
    onItemClicked: (String) -> Unit,
    onBuyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ItemAndImage(
            item = item,
            onItemClicked = onItemClicked
        )
        Button(
            onClick = onBuyClicked,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(162.dp)
        ) {
            Text(
                text = stringResource(R.string.addToCart),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
private fun SearchCategories(
    onHairClicked: () -> Unit,
    onBagClicked: () -> Unit,
    onMakeupClicked: () -> Unit,
    onAccessoriesClicked: () -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.dp_40),
                    bottom = dimensionResource(R.dimen.dp_15),
                    start = dimensionResource(R.dimen.dp_10)
                )
        )
        SearchCategoriesInfo(
            onHairClicked,
            onBagClicked,
            onMakeupClicked,
            onAccessoriesClicked
        )
    }
}



@Composable
private fun SearchCategoriesInfo(
    onHairClicked: () -> Unit,
    onBagClicked: () -> Unit,
    onAccessoriesClicked: () -> Unit,
    onMakeupClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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
        SearchTextAndImage(
            image = R.drawable.screenshot_20240630_152859,
            text = R.string.hairAccessories,
            onClicked = onAccessoriesClicked
        )
        SearchTextAndImage(
            image = R.drawable.screenshot_20240630_154319,
            text = R.string.makeUp,
            onClicked = onMakeupClicked
        )
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
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dp_10))
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
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onTertiary,
        modifier = modifier
            .padding(start = dimensionResource(R.dimen.dp_10))
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
    Column(modifier = modifier) {
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
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            shape = ShapeDefaults.Large,
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = true,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = if (searchUiState.searching) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            textStyle = TextStyle.Default.copy(
                color = MaterialTheme.colorScheme.tertiary,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,

            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.dp_10))
                .padding(top = dimensionResource(R.dimen.dp_40))

        )
    }
}