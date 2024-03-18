package com.example.jenstore.ui.screens.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jenstore.StoreApplication
import com.example.jenstore.data.Repository


sealed interface FeedUiState {
 //   data class Success(val images: List<Store_Image>) : FeedUiState
    data class Error(val message: String?) : FeedUiState
    object Loading : FeedUiState
}

class FeedViewModel(private val repository: Repository) : ViewModel() {

    var feedUiState: FeedUiState by mutableStateOf(FeedUiState.Loading)
        private set

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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as StoreApplication)
                val repository = application.container.repository
                FeedViewModel(repository)
            }
        }
    }
}