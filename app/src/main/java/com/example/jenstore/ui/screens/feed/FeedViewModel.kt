package com.example.jenstore.ui.screens.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.Feeds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedControlState(
    val play: Boolean = false,
    val pause: Boolean = true
)

sealed interface FeedUiState {

    object Loading : FeedUiState
    data class Success(val feed: List<Feeds> ) : FeedUiState

    data class Error(val message: String) : FeedUiState
}

class FeedViewModel(
    private val repository: Repository,
) : ViewModel() {


    private val _feedControlState = MutableStateFlow(FeedControlState())
    val feedControlState: StateFlow<FeedControlState> = _feedControlState

    var feedUiState: FeedUiState by mutableStateOf(FeedUiState.Loading)
        private set
    init {
   //     getFeeds()
    }


    val feeds = repository.getFeedPagination().cachedIn(viewModelScope)

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