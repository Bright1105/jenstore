package com.example.jenstore.ui.screens.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.map
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.ProductFeed
import com.example.jenstore.data.mappers.toFeed
import com.example.jenstore.data.local.feed.FeedEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class FeedControlState(
    val play: Boolean = false,
    val pause: Boolean = true
)

sealed interface FeedUiState {

    object Loading : FeedUiState
    data class Success(val feed: List<ProductFeed> ) : FeedUiState
}

class FeedViewModel(
    private val repository: Repository,
) : ViewModel() {


    private val _feedControlState = MutableStateFlow(FeedControlState())
    val feedControlState: StateFlow<FeedControlState> = _feedControlState

    var feedUiState: FeedUiState by mutableStateOf(FeedUiState.Loading)
        private set
    init {
        getFeeds()
    }



    //val feedPagingFlow = pager
    //        .flow
    //        .map {  pagingData ->
    //            pagingData.map { it.toFeed() }
    //        }
    //        .cachedIn(viewModelScope)

   // fun getFeed() {
    //        feedUiState = FeedUiState.Success(
    //            pager
    //                .flow
    //                .map { pagingData ->
    //                    pagingData.map { it.toFeed() }
    //                }
    //                .cachedIn(viewModelScope)
    //        )
    //    }

    fun getFeeds() {
            viewModelScope.launch {
                feedUiState = FeedUiState.Success(
                    repository.getFeeds().map {
                        it.toFeed()
                    }
                )
            }
        }


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

    //override fun onCleared() {
    //        super.onCleared()
    //        player.release()
    //    }

    // var feedUiState: FeedUiState by mutableStateOf(FeedUiState)
    //        private set

    init {
       // getImage()
    }

    //fun getImage() {
    //        viewModelScope.launch {
    //            feedUiState = FeedUiState.Loading
    //            feedUiState = try {
    //                FeedUiState.Success(repository.getImage())
    //            } catch (e: IOException) {
    //                FeedUiState.Error(message = e.message)
    //            } catch (e: HttpException) {
    //                FeedUiState.Error(e.message)
    //            }
    //        }
    //    }


}