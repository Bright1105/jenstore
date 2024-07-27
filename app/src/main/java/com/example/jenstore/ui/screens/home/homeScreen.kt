package com.example.jenstore.ui.screens.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.Home
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Item
import com.example.jenstore.ui.StoreTopAppBar
import com.example.jenstore.data.model.ItemType
import com.example.jenstore.data.model.Promotions
import com.example.jenstore.ui.screens.common.ItemListScreen
import com.example.jenstore.ui.screens.common.StoreTabRow
import com.example.jenstore.ui.screens.profile.account.promotion.PromotionCard
import com.example.jenstore.ui.theme.JenstoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onCartClicked: (StoreDestinations) -> Unit,
    onItemClicked: (String) -> Unit,
    navigateToCart: (StoreDestinations) -> Unit,
    navigateToSearch: (StoreDestinations) -> Unit
) {

    val uiState: UiState by viewModel.uiState.collectAsState()



    if (uiState.isShowingHomePage) {
        Home(
            homeViewModel = viewModel,
            allScreen = allScreen,
            onTabClicked = onTabClicked,
            currentScreen = currentScreen,
            onCartClicked = onCartClicked,
            onItemClicked = onItemClicked,
            isRefreshing = uiState.isRefreshing,
            onRefresh = {
                viewModel.refreshItems()
                uiState.isRefreshing = false
            },
            homeUiState = viewModel.homeUiState
        )
    } else {
        uiState.item?.map {  paging ->
            paging.filter {
                it.itemType == viewModel.uiState.value.typeOfProduct
            }
        }?.collectAsLazyPagingItems()?.let {
            ItemListScreen(
                items = it,
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
}


@Composable
fun Home(
    homeUiState: HomeUiState,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onCartClicked: (StoreDestinations) -> Unit,
    onItemClicked: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            StoreTopAppBar(
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
                homeViewModel = homeViewModel,
                onItemClicked = onItemClicked,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                homeUiState = homeUiState
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeItem(
    homeUiState: HomeUiState,
    onItemClicked: (String) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {

    val uiState = homeViewModel.uiState.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val promotions = homeViewModel.promotions.collectAsState(initial = emptyList())

    when (homeUiState) {
        is HomeUiState.Success -> {
            Column(
                modifier = modifier
                    .nestedScroll(pullToRefreshState.nestedScrollConnection),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HomePromotionList(
                    promotions = promotions.value,
                )
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
                            onSeeAllClicked = {
                                scope.launch {
                                    homeViewModel.seeHairAllClicked()
                                }
                            },
                        ) {
                            HomeItemList(
                                onItemClicked = onItemClicked,
                                items = homeUiState.item.filter {
                                    it.itemType == "hair"
                                }.sortedByDescending {
                                    it.dateCreated
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_35)))
                        HomeItemTitle(
                            itemType = ItemType.Accessories,
                            onSeeAllClicked = {
                                scope.launch {
                                    homeViewModel.seeAccessoriesClicked()
                                }
                            }
                        ) {
                            HomeItemList(
                                onItemClicked = onItemClicked,
                                items = homeUiState.item.filter {
                                    it.itemType == "Accessories"
                                }.sortedByDescending {
                                    it.dateCreated
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_35)))
                        HomeItemTitle(
                            itemType = ItemType.Makeup,
                            onSeeAllClicked = {
                                scope.launch {
                                    homeViewModel.seeMakeupALlClicked()
                                }
                            }
                        ) {
                            HomeItemList(
                                onItemClicked = onItemClicked,
                                items = homeUiState.item.filter {
                                    it.itemType == "Makeup"
                                }.sortedByDescending {
                                    it.dateCreated
                                }
                            )
                        }
                        Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_35)))
                        HomeItemTitle(
                            itemType = ItemType.Bags,
                            onSeeAllClicked = {
                                scope.launch {
                                    homeViewModel.seeBagALlClicked()
                                }
                            }
                        ) {
                            HomeItemList(
                                onItemClicked = onItemClicked,
                                items = homeUiState.item.filter {
                                    it.itemType == "bag"
                                }.sortedBy {
                                    it.dateCreated
                                }
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
                      //  .align(Alignment.TopCenter)
                )
            }
        }

        is HomeUiState.Loading -> {
            LoadingScreen()

        }

        is HomeUiState.Error -> {
            ErrorScreen(
                message = homeUiState.message,
                retryAction = homeViewModel::getProduct,
                navigateToCart = {}
            )
        }

    }
}

@Composable
private fun HomePromotionList(
    promotions: List<Promotions>
) {
    LazyRow {
        items(promotions, key = {promotion -> promotion.id }) { promotion ->
            PromotionCard(
                promotion = promotion,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_150))
            )
        }
    }
}



@Composable
private fun HomeItemList(
    items: List<Item>,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {

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

            items(items, key = { item -> item.id }) { item ->
                HomeItemAndImage(
                    item = item,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}



@Composable
private fun HomeItemAndImage(
    item: Item,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onItemClicked(item.id) }
            .width(dimensionResource(R.dimen.dp_200))
    ) {
        Column {
            HomeItemImage(
                image = item,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10)))
            Text(
                text = item.name.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
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
                .data(image.imageUri[0])
                .crossfade(true)
                .build(),
            contentDescription = image.name,
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            modifier = Modifier
                .width(200.dp)
                .height(250.dp)
        )
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
fun ErrorScreen(
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
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
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