package com.example.jenstore.ui.screens.common

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.Search
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Feeds
import com.example.jenstore.data.model.Item
import com.example.jenstore.ui.screens.home.HomeItemAndImage
import com.example.jenstore.ui.screens.home.HomeViewModel
import com.example.jenstore.ui.screens.home.UiState
import kotlinx.coroutines.flow.Flow


@Composable
fun ItemListScreen(
    items: LazyPagingItems<Item>,
    navigateToCart: (StoreDestinations) -> Unit,
    navigateToSearch: (StoreDestinations) -> Unit,
    onListBackClicked: () -> Unit,
    currentRoute: StoreDestinations,
    onItemClicked: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            ItemListTopAppBar(
                onListBackClicked = onListBackClicked,
                navigateToSearch = navigateToSearch,
                navigateToCart = navigateToCart,
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
                onItemClicked = onItemClicked,
                modifier = Modifier
            )
        }
    }
}


@Composable
private fun ItemList(
    items: LazyPagingItems<Item>,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    LaunchedEffect(key1 = items.loadState) {
        if (items.loadState.refresh is LoadState.Error) {
            Toast.makeText(context, "error:" + (items.loadState.refresh as LoadState.Error).error.message, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (items.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator()
        } else {
            LazyVerticalGrid(
                GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_15)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_35)),
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.dp_15),
                        start = dimensionResource(R.dimen.dp_10),
                        end = dimensionResource(R.dimen.dp_10),
                        bottom = dimensionResource(R.dimen.dp_15)
                    )
            ) {
                itemsPaging(items = items, key = { item -> item.id }) { item ->
                    item?.let {
                        HomeItemAndImage(
                            item = item,
                            onItemClicked = onItemClicked
                        )
                    }
                }

                item {
                    if (items.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }

                // items(items = items, key = { item -> item.id }) {
                //            HomeItemAndImage(
                //                item = it,
                //                onItemClicked = onItemClicked,
                //                modifier = Modifier
                //            )
                //        }
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
                PagingPlaceholderKey(index)
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListTopAppBar(
    onListBackClicked: () -> Unit,
    navigateToSearch: (StoreDestinations) -> Unit,
    navigateToCart: (StoreDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
                Text(
                    text = stringResource(R.string.store_name),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
        },
        navigationIcon = {
            IconButton(
                onClick = onListBackClicked
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(
                onClick = { navigateToSearch(Search) }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(
                onClick = { navigateToCart(MyCart) }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = stringResource(R.string.cart),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.background),
        modifier = modifier
    )
}



