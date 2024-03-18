package com.example.jenstore.ui.screens.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.StoreTopAppBar
import com.example.jenstore.data.model.ItemType
import com.example.jenstore.data.model.ItemsX
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.ui.screens.common.ItemListScreen
import com.example.jenstore.ui.screens.common.StoreTabRow
import com.example.jenstore.ui.theme.JenstoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    viewModel: HomeViewModel,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onCartClicked: () -> Unit,
    onItemClicked: (Int) -> Unit
) {
    val uiState: UiState by viewModel.uiState.collectAsState()
    val scope: CoroutineScope = rememberCoroutineScope()

    when (homeUiState) {
        is HomeUiState.Loading -> Loading()
        is HomeUiState.Error -> Error(
            message = homeUiState.message,
            retryAction = viewModel::getItem
        )

        is HomeUiState.Success -> {
            if (uiState.isShowingHomePage) {
                Home(
                    itemsHair = homeUiState.item.filter {
                         it.items.itemType == "hair"
                    }.subList(0, 2),
                    itemsBag = homeUiState.item.filter {
                         it.items.itemType == "bag"
                    },
                    itemsShoe = homeUiState.item.filter {
                         it.items.itemType == "shoe"
                    },
                    itemsClothe = homeUiState.item.filter {
                         it.items.itemType == "clothe"
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
                )
            } else {
                ItemListScreen(
                    value = "",
                    onValueChange = {},
                    onListBackClicked = {
                        viewModel.listBackClicked()
                    },
                    items = uiState.itemType,
                    onBuyClicked = {},
                    onItemClicked = onItemClicked
                )
            }
        }
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
            )
        }
    }
}

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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .heightIn(Dp.Unspecified)
            .widthIn(Dp.Unspecified)
            .verticalScroll(rememberScrollState())
    ) {
        HomeItemTitle(
            itemType = ItemType.Hairs,
            onSeeAllClicked = onHairSeeAllClicked,
        ) {
            HomeItemListHair(
                items = itemsHair,
                onItemClicked = onItemClicked
            )
        }
        Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_5)))
        HomeHorizontalDivider()
        HomeItemTitle(
            itemType = ItemType.Bags,
            onSeeAllClicked = onBagSeeAllClicked,
        ) {
            HomeItemListBag(
                items = itemsBag,
                onItemClicked = onItemClicked
            )
        }
        Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_5)))
        HomeHorizontalDivider()
        HomeItemTitle(
            itemType = ItemType.Shoes,
            onSeeAllClicked = onShoeSeeAllClicked,
        ) {
            HomeItemListShoe(
                items = itemsShoe,
                onItemClicked = onItemClicked
            )
        }
        Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_5)))
        HomeHorizontalDivider()
        HomeItemTitle(
            itemType = ItemType.Clothes,
            onSeeAllClicked = onClotheSeeAllClicked,
        ) {
            HomeItemListClothe(
                items = itemsClothe,
                onItemClicked = onItemClicked
            )
        }
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
private fun HomeItemListClothe(
    items: List<ProductItem>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = dimensionResource(R.dimen.dp_400))
    ) {
        items(items, key = { item -> item.items.id }) {
            HomeItemTextAndImageClothe(
                items = it,
                modifier = modifier
                    .clickable { onItemClicked(it.items.id) }
                    .padding(
                        start = dimensionResource(R.dimen.dp_20),
                        end = dimensionResource(R.dimen.dp_10)
                    )
            )
        }
    }
}

@Composable
private fun HomeItemListBag(
    items: List<ProductItem>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = dimensionResource(R.dimen.dp_450))
        ) {
        items(items, key = { item -> item.items.id }) {
            HomeItemTextAndImageBag(
                items = it,
                modifier = modifier
                    .clickable { onItemClicked(it.items.id) }
                    .padding(
                        start = dimensionResource(R.dimen.dp_20),
                        end = dimensionResource(R.dimen.dp_5),
                        bottom = dimensionResource(R.dimen.dp_5)
                    )
            )
        }
    }
}

@Composable
private fun HomeItemListShoe(
    items: List<ProductItem>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = dimensionResource(R.dimen.dp_400))
    ) {
        items(items, key = { item -> item.items.id }) {
            HomeItemTextAndImageShoe(
                items = it,
                modifier = modifier
                    .clickable { onItemClicked(it.items.id) }
                    .padding(
                        start = dimensionResource(R.dimen.dp_20),
                        end = dimensionResource(R.dimen.dp_10)
                    )
            )
        }
    }
}

@Composable
private fun HomeItemListHair(
    items: List<ProductItem>,
    onItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = dimensionResource(R.dimen.dp_400))
    ) {
        items(items, key = { item -> item.items.id }) {
            HomeItemTextAndImageHair(
                item = it,
                modifier = modifier
                    .clickable { onItemClicked(it.items.id) }
                    .padding(
                        start = dimensionResource(R.dimen.dp_10),
                        end = dimensionResource(R.dimen.dp_10)
                    )
            )
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
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontFamily = FontFamily.Serif,
                modifier = modifier
                    .weight(1f)
                    .padding(
                        top = dimensionResource(R.dimen.dp_10),
                        bottom = dimensionResource(R.dimen.dp_10),
                        start = dimensionResource(R.dimen.dp_20)
                    )
            )
            TextButton(
                onClick = { onSeeAllClicked() }
            ) {
                Text(
                    text = stringResource(R.string.see),
                    style = MaterialTheme.typography.displayLarge,
                    fontFamily = FontFamily.Serif,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
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
private fun HomeItemTextAndImageClothe(
    items: ProductItem,
    modifier: Modifier = Modifier
) {
    Box {
        Column {
            HomeItemImage(
                image = items,
                modifier = modifier
                    .clip(MaterialTheme.shapes.small)
            )
            Text(
                text = items.items.title,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                modifier = modifier
                    .padding(dimensionResource(R.dimen.dp_10))
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun HomeItemTextAndImageBag(
    items: ProductItem,
    modifier: Modifier = Modifier
) {
    Box {
        Column {
            HomeItemImage(
                image = items,
                modifier = modifier
                    .clip(
                        CircleShape.copy(
                            topStart = MaterialTheme.shapes.small.topStart,
                            bottomStart = MaterialTheme.shapes.small.bottomStart
                        )
                    )
                    .height(dimensionResource(R.dimen.dp_150))
                    .width(dimensionResource(R.dimen.dp_150))
            )
            Text(
                text = items.items.title,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                modifier = modifier
                    .padding(dimensionResource(R.dimen.dp_10))
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun HomeItemTextAndImageShoe(
    items: ProductItem,
    modifier: Modifier = Modifier
) {
    Box {
        Column {
            HomeItemImage(
                image = items,
                modifier = modifier
                    .padding(end = dimensionResource(R.dimen.dp_5))
                    .clip(
                        MaterialTheme.shapes.medium
                    )
            )
            Text(
                text = items.items.title,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                modifier = modifier
                    .padding(dimensionResource(R.dimen.dp_10))
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun HomeItemTextAndImageHair(
    item: ProductItem,
    modifier: Modifier = Modifier
) {
    Box {
        Column {
            HomeItemImage(
                image = item,
                modifier = modifier
                    .clip(CircleShape)
            )
            Text(
                text = item.items.title,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Justify,
                modifier = modifier
                    .padding(dimensionResource(R.dimen.dp_10))
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }

    }
}

@Composable
private fun HomeItemImage(
    image: ProductItem,
    modifier: Modifier = Modifier
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
        modifier = modifier
            .width(200.dp)
            .height(200.dp)
    )
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
        Image(
            painter = painterResource(R.drawable.loading_img),
            contentDescription = "Error",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
        )
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


        //            items = ProductItem(
        //                items = ItemsX(
        //                    brand = "woman hair",
        //                    description = "hair",
        //                    itemType = "hair2",
        //                    title = "hair2",
        //                    price = 200,
        //                    dateCreated = "20",
        //                    id = 1,
        //                ),
        //                image = ""
        //            )

    )
    JenstoreTheme {
        HomeItemListHair(
            items = mockData,
            onItemClicked = {}
        )
    }
}