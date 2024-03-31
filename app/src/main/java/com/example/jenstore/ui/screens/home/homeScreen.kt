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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.HomeList
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.ui.StoreTopAppBar
import com.example.jenstore.data.model.ItemType
import com.example.jenstore.data.model.ItemsX
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.ui.screens.common.ItemListScreen
import com.example.jenstore.ui.screens.common.StoreTabRow
import com.example.jenstore.ui.screens.feed.ErrorScreen
import com.example.jenstore.ui.screens.feed.LoadingScreen
import com.example.jenstore.ui.theme.JenstoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    viewModel: HomeViewModel,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onCartClicked: () -> Unit,
    onItemClicked: (Int) -> Unit,
    navigateToCart: (StoreDestinations) -> Unit,
    navigateToSearch: (StoreDestinations) -> Unit,
) {


    val uiState: UiState by viewModel.uiState.collectAsState()
    val scope: CoroutineScope = rememberCoroutineScope()

    when (homeUiState) {
        is HomeUiState.Loading -> Loading()
        is HomeUiState.Success -> {
            if (uiState.isShowingHomePage) {
                Home(
                    itemsHair = homeUiState.item.filter {
                        it.items.itemType == "hair"
                    }.subList(0, 4)
                        .sortedBy {
                        it.items.dateCreated
                    },
                    itemsBag = homeUiState.item.filter {
                        it.items.itemType == "bag"
                    }.subList(0, 2)
                        .sortedBy {
                        it.items.dateCreated
                    },
                    itemsShoe = homeUiState.item.filter {
                        it.items.itemType == "shoe"
                    }.subList(0, 2)
                        .sortedBy {
                        it.items.dateCreated
                    },
                    itemsClothe = homeUiState.item.filter {
                        it.items.itemType == "clothe"
                    }.subList(0, 3)
                        .sortedBy {
                        it.items.dateCreated
                    },
                    allScreen = allScreen,
                    onTabClicked = onTabClicked,
                    currentScreen = currentScreen,
                    onHairSeeAllClicked = {
                        scope.launch {
                            viewModel.seeHairAllClicked()
                        }
                    },
                    onShoeSeeAllClicked = {
                        scope.launch {
                            viewModel.seeShoeALlClicked()
                        }
                    },
                    onClotheSeeAllClicked = {
                        scope.launch {
                            viewModel.seeClotheALlClicked()
                        }
                    },
                    onBagSeeAllClicked = {
                        scope.launch {
                            viewModel.seeBagALlClicked()
                        }
                    },
                    onCartClicked = onCartClicked,
                    onItemClicked = onItemClicked,
                    uiState = uiState,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
                        viewModel.refreshItems()
                        uiState.isRefreshing = false
                    }
                )
            } else {
                ItemListScreen(
                    navigateToSearch = navigateToSearch,
                    navigateToCart = navigateToCart,
                    onListBackClicked = {
                        viewModel.listBackClicked()
                    },
                    items = uiState.itemType,
                    onItemClicked = onItemClicked,
                    currentRoute = currentScreen
                )
            }
        }

        is HomeUiState.Error -> Error(
            message = homeUiState.message,
            retryAction = viewModel::getItem
        )
    }
}





@Composable
fun Home(
    itemsHair: List<ProductItem>,
    itemsShoe: List<ProductItem>,
    itemsBag: List<ProductItem>,
    itemsClothe: List<ProductItem>,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onHairSeeAllClicked: () -> Unit,
    onShoeSeeAllClicked: () -> Unit,
    onBagSeeAllClicked: () -> Unit,
    onClotheSeeAllClicked: () -> Unit,
    onCartClicked: () -> Unit,
    onItemClicked: (Int) -> Unit,
    uiState: UiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
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
                itemsHair,
                itemsShoe,
                itemsBag,
                itemsClothe,
                onHairSeeAllClicked = onHairSeeAllClicked,
                onBagSeeAllClicked = onBagSeeAllClicked,
                onClotheSeeAllClicked = onClotheSeeAllClicked,
                onShoeSeeAllClicked = onShoeSeeAllClicked,
                onItemClicked = onItemClicked,
                uiState = uiState,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeItem(
    itemsHair: List<ProductItem>,
    itemsShoe: List<ProductItem>,
    itemsBag: List<ProductItem>,
    itemsClothe: List<ProductItem>,
    onHairSeeAllClicked: () -> Unit,
    onShoeSeeAllClicked: () -> Unit,
    onBagSeeAllClicked: () -> Unit,
    onClotheSeeAllClicked: () -> Unit,
    onItemClicked: (Int) -> Unit,
    uiState: UiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
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
                   uiState = uiState,
               ) {
                   HomeItemList(
                       items = itemsHair,
                       onItemClicked = onItemClicked
                   )
               }
               Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_35)))
               HomeItemTitle(
                   itemType = ItemType.Bags,
                   onSeeAllClicked = onBagSeeAllClicked,
                   uiState = uiState,
               ) {
                   HomeItemList(
                       items = itemsBag,
                       onItemClicked = onItemClicked
                   )
               }
               Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_35)))
               HomeItemTitle(
                   itemType = ItemType.Shoes,
                   onSeeAllClicked = onShoeSeeAllClicked,
                   uiState = uiState,
               ) {
                   HomeItemList(
                       items = itemsShoe,
                       onItemClicked = onItemClicked
                   )
               }
               Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_35)))
               HomeItemTitle(
                   itemType = ItemType.Clothes,
                   onSeeAllClicked = onClotheSeeAllClicked,
                   uiState = uiState,
               ) {
                   HomeItemList(
                       items = itemsClothe,
                       onItemClicked = onItemClicked,
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
private fun HomeHorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        thickness = dimensionResource(R.dimen.dp_5),
        modifier = modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiaryContainer
    )
}


@Composable
private fun HomeItemList(
    items: List<ProductItem>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_15)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_35)),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = dimensionResource(R.dimen.dp_400))
            .padding(
                start = dimensionResource(R.dimen.dp_10),
                end = dimensionResource(R.dimen.dp_10)
            )
    ) {
        items(items, key = { item -> item.items.id }) {
           HomeItemAndImage(
               item = it,
               onItemClicked = onItemClicked
           )
        }
    }
}

@Composable
fun HomeItemAndImage(
    item: ProductItem,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box() {
        Column {
            HomeItemImage(
                image = item,
                modifier = modifier
                    .clickable { onItemClicked(item.items.id) }
            )
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_10)))
            Text(
                text = item.items.title.uppercase(),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                modifier = modifier
                    .padding(
                        bottom = dimensionResource(R.dimen.dp_2)
                    )
                    .align(alignment = Alignment.CenterHorizontally)
                    .clickable { onItemClicked(item.items.id) }
            )
            Text(
                text = item.items.brand.uppercase(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraLight,
                modifier = modifier
                    .padding(
                        bottom = dimensionResource(R.dimen.dp_2)
                    )
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Row(modifier = modifier.align(alignment = Alignment.CenterHorizontally)) {
                Icon(
                    painter = painterResource(R.drawable.naira_sign),
                    contentDescription = stringResource(R.string.naira),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier
                        .size(dimensionResource(R.dimen.dp_15))
                )
                Text(
                    text = item.items.price.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
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
    uiState: UiState,
    onSeeAllClicked: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Row {
            Text(
                text = itemType.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = FontFamily.Serif,
                modifier = modifier
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
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier
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
    image: ProductItem,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
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
            filterQuality = FilterQuality.High,
            modifier = modifier
                .width(162.dp)
                .height(250.dp)
        )
    }
}

@Composable
fun Error(
    message: String?,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

        CircularProgressIndicator()
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

    val mockData = listOf(
        ProductItem(
            items = ItemsX(
                brand = "woman hair",
                description = "hair",
                itemType = "hair",
                title = "hair1",
                price = 200,
                dateCreated = "20",
                id = 1,
            ),

            image = "",
        ),

    )
    JenstoreTheme {
       // HomeItemListHair(
        //            items = mockData,
        //            onItemClicked = {}
        //        )
    }
}

// Tasks Tommorrow before anything


//    set the details screen