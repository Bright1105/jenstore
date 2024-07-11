package com.example.jenstore.ui.screens.feed


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.Feed
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Feeds
import com.example.jenstore.ui.screens.common.StoreTabRow


@Composable
fun FeedScreen(
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

            FeedList(
                //feeds = pagingFeeds,
                feedViewModel = viewModel,
                currentRoute = currentScreen
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FeedList(
    currentRoute: StoreDestinations,
    feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Feed(
            feedViewModel = feedViewModel,
            feedUiState = feedViewModel.feedUiState,
            currentRoute = currentRoute
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun Feed(
    feedUiState: FeedUiState,
    currentRoute: StoreDestinations,
    modifier: Modifier = Modifier,
    feedViewModel: FeedViewModel,
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

        when (feedUiState) {
            is FeedUiState.Success -> {
                val mContent = LocalContext.current

                val videos: List<Feeds?> = feedUiState.feed



                var isVisible by remember {
                    mutableStateOf(true)
                }



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


                IconButton(
                    onClick = {
                        mExoPlayer.seekToNext()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.next)
                    )
                }
                IconButton(
                    onClick = {
                        mExoPlayer.seekToPrevious()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.previous)
                    )
                }
                videos.forEach {
                    AndroidView(
                        factory = { content ->
                            PlayerView(content).also { player ->
                                player.player = mExoPlayer
                                player.useController = false
                                it?.videoUri?.let { it1 ->
                                    MediaItem.fromUri(
                                        it1
                                    )
                                }?.let { it2 -> player.player?.addMediaItem(it2) }
                                if (currentRoute.route == Feed.route) {
                                    player.player?.playWhenReady = true
                                }
                                player.player?.addListener(feedViewModel.playbackStateListener)
                                player.player?.prepare()

                            }

                        },
                        update = { playerUpdate ->
                            when (lifecycle) {
                                Lifecycle.Event.ON_PAUSE -> {
                                    playerUpdate.onPause()
                                    playerUpdate.player?.pause()
                                   // playerUpdate.player?.removeListener(feedViewModel.playbackStateListener)
                                    playerUpdate.player?.playWhenReady = false
                                }

                                Lifecycle.Event.ON_RESUME -> {
                                    playerUpdate.onResume()
                                }

                                else -> Unit
                            }
                            if (currentRoute.route != Feed.route) {
                                playerUpdate.player?.pause()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(10 / 20f)
                    )
                }

                IconButton(
                    onClick = {
                        if (mExoPlayer.isPlaying) {
                            mExoPlayer.pause()
                            isVisible = false
                        } else {
                            mExoPlayer.play()
                            isVisible = true
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxHeight()
                        .alpha(if (isVisible) 0f else 1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.feed),
                        modifier = Modifier

                    )
                }


            }
            is FeedUiState.Loading -> {
                LoadingFeed()
            }
            is FeedUiState.Error -> Unit
        }

    }

}

@Composable
private fun LoadingFeed(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CircularProgressIndicator()
    }
}



