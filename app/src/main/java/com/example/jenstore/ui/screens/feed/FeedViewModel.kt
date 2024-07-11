package com.example.jenstore.ui.screens.feed

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.cachedIn
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.model.Feeds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "PlayerActivity"
data class FeedControlState(
    val play: Boolean = false,
    val pause: Boolean = true
)

interface FeedUiState {

    object Loading : FeedUiState
    data class Success(val feed: List<Feeds?>) : FeedUiState

    data class Error(val message: String) : FeedUiState
}

class FeedViewModel(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {


    private val _feedControlState = MutableStateFlow(FeedControlState())
    val feedControlState: StateFlow<FeedControlState> = _feedControlState

    val feed = firebaseRepository.getFeedPagination().cachedIn(viewModelScope)

    val feedPaging = firebaseRepository.getPaging().cachedIn(viewModelScope)

    var feedUiState: FeedUiState by mutableStateOf(FeedUiState.Loading)
        private set

    val playbackStateListener: Player.Listener = playbackStateListener()

    var playWhenReady: Boolean = true
    var mediaItemIndex: Int = 0
    var playbackPosition: Long = 0L


    init {
        getFeeds()
    }



    fun getFeeds() {
        viewModelScope.launch {
            feedUiState = try {
                FeedUiState.Success(firebaseRepository.getFeed())
            } catch (e: Throwable) {
               FeedUiState.Error(e.message.toString())
            }
        }
    }



    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val stateString: String = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE    _"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING _"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY _"
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED _"
                else -> "UNKNOWN_STATE _"
            }
            Log.d(TAG, "changed state to $stateString")
        }
    }

    val feeds = firebaseRepository.getFeedPagination().cachedIn(viewModelScope)

   //

    fun playFeed() {
        _feedControlState.update {
            it.copy(
                play = true
            )
        }
    }

    fun pauseFeed() {
        _feedControlState.update {
            it.copy(
                play = false
            )
        }
    }

}