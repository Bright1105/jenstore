package com.example.jenstore.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Item
import com.example.jenstore.ui.StoreTopAppBar
import com.example.jenstore.data.model.ItemType
import com.example.jenstore.ui.screens.common.ItemListScreen
import com.example.jenstore.ui.screens.common.StoreTabRow
import com.example.jenstore.ui.theme.JenstoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    viewModel: HomeViewModel,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onCartClicked: (StoreDestinations) -> Unit,
    onItemClicked: (String) -> Unit,
    navigateToCart: (StoreDestinations) -> Unit,
    navigateToSearch: (StoreDestinations) -> Unit,
) {

    //var itemT by remember{ mutableStateOf("")}


    val uiState: UiState by viewModel.uiState.collectAsState()
    val scope: CoroutineScope = rememberCoroutineScope()


   // val paging = viewModel.products.map {  paging ->
    //        paging.filter {
    //            it.itemType == viewModel.itemType.value
    //        }
    //    }.collectAsLazyPagingItems()


    if (uiState.isShowingHomePage) {
        Home(
            homeUiState = homeUiState,
            homeViewModel = viewModel,
            allScreen = allScreen,
            onTabClicked = onTabClicked,
            currentScreen = currentScreen,
            onHairSeeAllClicked = {
                scope.launch {
                   // viewModel.itemType.value = "hair"
                    viewModel.seeHairAllClicked()
                }
            },
            onShoeSeeAllClicked = {
                scope.launch {
                  //  viewModel.itemType.value = "shoe"
                    viewModel.seeShoeALlClicked()
                }
            },
            onClotheSeeAllClicked = {
                scope.launch {
                  //  viewModel.itemType.value = "clothe"
                    viewModel.seeClotheALlClicked()
                }
            },
            onBagSeeAllClicked = {
                scope.launch {
                  //  viewModel.itemType.value = "bag"
                    viewModel.seeBagALlClicked()
                }
            },
            onCartClicked = onCartClicked,
            onItemClicked = onItemClicked,
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                viewModel.refreshItems()
                uiState.isRefreshing = false
            }
        )
    } else {
        ItemListScreen(
            items = uiState.item.collectAsLazyPagingItems(),
            navigateToSearch = navigateToSearch,
            navigateToCart = navigateToCart,
            onListBackClicked = {
                viewModel.listBackClicked()
            },
            onItemClicked = onItemClicked,
            currentRoute = currentScreen,
        )
    }
}


@Composable
fun Home(
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onHairSeeAllClicked: () -> Unit,
    onShoeSeeAllClicked: () -> Unit,
    onBagSeeAllClicked: () -> Unit,
    onClotheSeeAllClicked: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onItemClicked: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    homeUiState: HomeUiState,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            StoreTopAppBar(
                screen = MyCart,
                onCartClicked = onCartClicked
            )
        },

        bottomBar = {
            StoreTabRow(
                allScreensBar = allScreen,
                onTabSelected = onTabClicked,
                currentScreen = currentScreen
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HomeItem(
                homeUiState = homeUiState,
                homeViewModel = homeViewModel,
                navigateToCart = onCartClicked,
                onHairSeeAllClicked = onHairSeeAllClicked,
                onBagSeeAllClicked = onBagSeeAllClicked,
                onClotheSeeAllClicked = onClotheSeeAllClicked,
                onShoeSeeAllClicked = onShoeSeeAllClicked,
                onItemClicked = onItemClicked,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeItem(
    onHairSeeAllClicked: () -> Unit,
    onShoeSeeAllClicked: () -> Unit,
    onBagSeeAllClicked: () -> Unit,
    onClotheSeeAllClicked: () -> Unit,
    onItemClicked: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    homeUiState: HomeUiState,
    homeViewModel: HomeViewModel,
    navigateToCart: (StoreDestinations) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {

    val pullToRefreshState = rememberPullToRefreshState()
   Box(
       modifier = modifier
           .nestedScroll(pullToRefreshState.nestedScrollConnection)
   ) {
       LazyColumn(
           state = lazyListState,
           modifier = modifier
               .fillMaxSize()
               .heightIn(Dp.Unspecified)
               .widthIn(Dp.Unspecified)

       ) {
           item {
               HomeItemTitle(
                   itemType = ItemType.Hairs,
                   onSeeAllClicked = onHairSeeAllClicked,
               ) {
                   HomeItemList(
                       homeUiState = homeUiState,
                       itemType = "hair",
                       onItemClicked = onItemClicked,
                       homeViewModel = homeViewModel,
                       navigateToCart = navigateToCart,
                   )
               }
               Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_35)))
               HomeItemTitle(
                   itemType = ItemType.Bags,
                   onSeeAllClicked = onBagSeeAllClicked,
               ) {
                   HomeItemList(
                       homeUiState = homeUiState,
                       itemType = "bag",
                       onItemClicked = onItemClicked,
                       homeViewModel = homeViewModel,
                       navigateToCart = navigateToCart
                   )
               }
               Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_35)))
               HomeItemTitle(
                   itemType = ItemType.Shoes,
                   onSeeAllClicked = onShoeSeeAllClicked,
               ) {
                   HomeItemList(
                       homeUiState = homeUiState,
                       itemType = "shoe",
                       onItemClicked = onItemClicked,
                       homeViewModel = homeViewModel,
                       navigateToCart = navigateToCart
                   )
               }
               Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_35)))
               HomeItemTitle(
                   itemType = ItemType.Clothes,
                   onSeeAllClicked = onClotheSeeAllClicked,
               ) {
                   HomeItemList(
                       homeUiState = homeUiState,
                       itemType = "clothe",
                       onItemClicked = onItemClicked,
                       homeViewModel = homeViewModel,
                       navigateToCart = navigateToCart
                   )
               }
           }
       }

       if (pullToRefreshState.isRefreshing) {
           LaunchedEffect(true) {
               onRefresh()
           }
       }

       LaunchedEffect(isRefreshing) {
           if (isRefreshing) {
               pullToRefreshState.startRefresh()
           } else {
               pullToRefreshState.endRefresh()
           }
       }

       PullToRefreshContainer(
           state = pullToRefreshState,
           modifier = modifier
               .align(Alignment.TopCenter)
       )
   }
}



@Composable
private fun HomeItemList(
    onItemClicked: (String) -> Unit,
    itemType: String,
    homeUiState: HomeUiState,
    homeViewModel: HomeViewModel,
    navigateToCart: (StoreDestinations) -> Unit,
    modifier: Modifier = Modifier
) {

   // val context = LocalContext.current
    //    LaunchedEffect(key1 = items.loadState) {
    //        if (items.loadState.refresh is LoadState.Error) {
    //            Toast.makeText(
    //                context,
    //                "Error: " + (items.loadState.refresh as LoadState.Error).error.message,
    //                Toast.LENGTH_LONG
    //            ).show()
    //        }
    //    }

    Box(modifier = modifier.fillMaxSize()) {

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_15)),
            modifier = Modifier
                .fillMaxSize()
                .heightIn(max = dimensionResource(R.dimen.dp_400))
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_10)
                )
        ) {


            when (homeUiState) {
                is HomeUiState.Loading -> {
                    item {
                        Loading(modifier = Modifier.fillMaxSize())
                    }
                }
                is HomeUiState.Success -> {
                    items( homeUiState.item.filter {
                        it.itemType == itemType
                    }.sortedByDescending {
                        it.dateCreated
                    }, key = {item -> item.id }) { item ->
                        HomeItemAndImage(
                            item = item,
                            onItemClicked,
                            modifier = Modifier
                        )
                    }
                }
                is HomeUiState.Error -> {
                    item {
                        Error(
                            message = homeUiState.message,
                            retryAction = homeViewModel::getProduct,
                            navigateToCart = navigateToCart,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }

        }

    }


}



@Composable
fun HomeItemAndImage(
    item: Item,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onItemClicked(item.id) }
    ) {
        Column {
            HomeItemImage(
                image = item,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
            Text(
                text = item.name.uppercase(),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(
                        bottom = dimensionResource(R.dimen.dp_2)
                    )
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Text(
                text = item.brand.uppercase(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraLight,
                modifier = Modifier
                    .padding(
                        bottom = dimensionResource(R.dimen.dp_2)
                    )
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                Icon(
                    painter = painterResource(R.drawable.naira_sign),
                    contentDescription = stringResource(R.string.naira),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.dp_15))
                )
                Text(
                    text = item.price.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(R.dimen.dp_2)
                        )

                )
            }
        }
    }
}

@Composable
private fun HomeItemTitle(
    itemType: ItemType,
    onSeeAllClicked: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Row {
            Text(
                text = itemType.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontFamily.Serif,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        top = dimensionResource(R.dimen.dp_10),
                        bottom = dimensionResource(R.dimen.dp_10),
                        start = dimensionResource(R.dimen.dp_10)
                    )
            )
            TextButton(
                onClick = onSeeAllClicked
            ) {
                Text(
                    text = stringResource(R.string.viewAll),
                    style = MaterialTheme.typography.displayLarge,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(R.dimen.dp_10),
                            bottom = dimensionResource(R.dimen.dp_10)
                        )
                )
            }

        }
        content()
    }
}

@Composable
private fun HomeItemImage(
    image: Item,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.imageUri?.get(0))
                .crossfade(true)
                .build(),
            contentDescription = image.name,
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            modifier = Modifier
                .width(162.dp)
                .height(250.dp)
        )
    }
}

@Composable
fun Error(
    message: String?,
    retryAction: () -> Unit,
    navigateToCart: (StoreDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { navigateToCart(MyCart) }
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "cart"
            )
        }
        message?.let { Text(text = it) }
        Image(
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = "Error",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
        )
        Spacer(modifier = modifier.height(10.dp))
        Button(onClick = retryAction) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        //Image(
        //            painter = painterResource(R.drawable.loading_img),
        //            contentDescription = "Error",
        //            contentScale = ContentScale.Crop,
        //            modifier = modifier
        //                .fillMaxWidth()
        //        )
    }
}


@Preview(showBackground = true,

)
@Composable
fun GreetingPreview() {

    //val mockData = listOf(
    //        ProductItem(
    //            items = ItemsX(
    //                brand = "woman hair",
    //                description = "hair",
    //                itemType = "hair",
    //                title = "hair1",
    //                price = 200,
    //                dateCreated = "20",
    //                id = 1,
    //            ),
    //
    //            image = "",
    //        ),
    //
    //    )
    JenstoreTheme {
       // HomeItemListHair(
        //            items = mockData,
        //            onItemClicked = {}
        //        )
    }
}

// Tasks Tommorrow before anything


//    set the details screen