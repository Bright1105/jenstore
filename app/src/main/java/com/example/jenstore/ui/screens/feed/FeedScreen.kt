package com.example.jenstore.ui.screens.feed

import android.widget.Toast
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.ProductFeed
import com.example.jenstore.ui.screens.common.StoreTabRow


@Composable
fun FeedScreen(
    feedUiState: FeedUiState,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    viewModel: FeedViewModel
) {


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
                .fillMaxSize()
        ) {
            when (feedUiState) {
                is FeedUiState.Success -> {
                    FeedList(
                        feeds = feedUiState.feed,
                        feedViewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is FeedUiState.Loading -> {
                    LoadingFeed(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FeedList(
    feeds: List<ProductFeed>,
    feedViewModel: FeedViewModel,
    modifier: Modifier = Modifier
) {
    val feedControlState: FeedControlState by feedViewModel.feedControlState.collectAsState()

   // val context = LocalContext.current
    //    LaunchedEffect(key1 = feeds.loadState) {
    //        if (feeds.loadState.refresh is LoadState.Error) {
    //            Toast.makeText(
    //                context,
    //                "Error: " + (feeds.loadState.refresh as LoadState.Error).error.message,
    //                Toast.LENGTH_LONG
    //            ).show()
    //        }
    //    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dp_35)),
            flingBehavior = ScrollableDefaults.flingBehavior()
        ) {
            items(feeds, key = { feed -> feed.id }) { feed ->
                Feed(
                    feed = feed,
                    feedViewModel = feedViewModel,
                    feedControlState = feedControlState,
                )
            }
        }
    }
}
// if (feeds.loadState.refresh is LoadState.Loading) {
//            CircularProgressIndicator(
//                modifier = Modifier.align(Alignment.Center)
//            )
//        } else {
//
//                item {
//                    if (feeds.loadState.append is LoadState.Loading) {
//                        CircularProgressIndicator()
//                    }
//                }
//            }
//        }

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun Feed(
    modifier: Modifier = Modifier,
    feed: ProductFeed,
    feedViewModel: FeedViewModel,
    feedControlState: FeedControlState,
) {


    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val mContent = LocalContext.current

        val mExoPlayer = remember(mContent) {
            ExoPlayer.Builder(mContent)
                .build()
                .also {
                    it.trackSelectionParameters = it.trackSelectionParameters
                        .buildUpon()
                        .setMaxVideoSizeSd()
                        .build()
                }
        }

        AndroidView(
            factory = { content ->
                PlayerView(content).also { player ->
                    player.player = mExoPlayer
                    player.player?.addMediaItem(MediaItem.fromUri("https://86gnbdfj-8000.uks1.devtunnels.ms/${feed.videos}")) //check
                    player.useController = true
                    player.player?.prepare()

                }

            },
            update = { playerUpdate ->
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        playerUpdate.onPause()
                        playerUpdate.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        playerUpdate.onResume()
                    }

                    else -> Unit
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(10 / 20f)
        )
       //if (!feedControlState.play) {
        //           IconButton(
        //               onClick = {
        //                   mExoPlayer.play()
        //                   feedViewModel.playFeed()
        //               },
        //               modifier = Modifier
        //                   .align(Alignment.Center)
        //           ) {
        //               Icon(
        //                   imageVector = Icons.Default.PlayArrow,
        //                   contentDescription = "play",
        //                   tint = MaterialTheme.colorScheme.background,
        //                   modifier = Modifier
        //                       .width(dimensionResource(R.dimen.dp_100))
        //                       .height(dimensionResource(R.dimen.dp_100))
        //
        //               )
        //           }
        //       } else {
        //           IconButton(
        //               onClick = {
        //                   mExoPlayer.pause()
        //                   feedViewModel.pauseFeed()
        //               },
        //               modifier = Modifier
        //                   .align(Alignment.Center)
        //           ) {
        //
        //           }
        //       }
    }

}


@Composable
private fun LoadingFeed(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CircularProgressIndicator()
    }
}



