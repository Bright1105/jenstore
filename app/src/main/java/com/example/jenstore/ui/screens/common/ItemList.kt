package com.example.jenstore.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.Search
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.ui.screens.home.HomeItemAndImage


@Composable
fun ItemListScreen(
    navigateToCart: (StoreDestinations) -> Unit,
    navigateToSearch: (StoreDestinations) -> Unit,
    onListBackClicked: () -> Unit,
    items: List<ProductItem>,
    currentRoute: StoreDestinations,
    onItemClicked: (Int) -> Unit,
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
                    .padding()
            )
        }
    }
}


@Composable
private fun ItemList(
    items: List<ProductItem>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
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
        items(items, key = { item -> item.id }) {
            HomeItemAndImage(
                item = it,
                onItemClicked = onItemClicked
            )
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



