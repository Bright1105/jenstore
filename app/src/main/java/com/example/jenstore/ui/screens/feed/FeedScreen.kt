package com.example.jenstore.ui.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.ui.screens.common.StoreTabRow

@Composable
fun FeedScreen(
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    viewModel: FeedViewModel = viewModel(factory = FeedViewModel.Factory),
) {

    val feedUiState: FeedUiState = viewModel.feedUiState

    Scaffold(
        bottomBar = {
            StoreTabRow(
                allScreensBar = allScreen,
                onTabSelected = onTabClicked,
                currentScreen = currentScreen
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
           // when (feedUiState) {
            //                is FeedUiState.Loading -> LoadingScreen()
            //                is FeedUiState.Success -> FeedImageList(images = feedUiState.images)
            //                is FeedUiState.Error -> ErrorScreen(text = feedUiState.message)
            //            }

            Text(text = "welcome to feedScreen")
        }
    }
}


//@Composable
//fun FeedImageList(
//    images: List<Store_Image>,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn {
//        items(images, key = { image -> image.image }) {
//            FeedImage(image = it)
//            Spacer(modifier = modifier.height(20.dp))
//        }
//    }
//}

//@Composable
//fun FeedImage(
//    image: Store_Image,
//    modifier: Modifier = Modifier
//) {
//    AsyncImage(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data("https://86gnbdfj-8000.uks1.devtunnels.ms/${image.image}")
//            .crossfade(true)
//            .build(),
//        contentDescription = image.image,
//        contentScale = ContentScale.Crop,
//        error = painterResource(R.drawable.ic_broken_image),
//        placeholder = painterResource(R.drawable.loading_img),
//        modifier = modifier
//            .width(200.dp)
//            .height(200.dp)
//    )
//}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "Loading",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

@Composable
fun ErrorScreen(
    text: String?,
    modifier: Modifier = Modifier
) {
    Column {
        text?.let { Text(text = it) }
        Spacer(modifier = modifier.height(5.dp))
        Image(
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = "error",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}