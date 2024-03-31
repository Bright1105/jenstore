package com.example.jenstore.ui.screens.home



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.ProductItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import okhttp3.internal.wait
import okio.IOException



sealed interface HomeUiState {
    data class Success(val item: List<ProductItem> = listOf()) : HomeUiState
    data class Error(val message: String?) : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(
    private val repository: Repository,
) : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState


    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
            private set



    init {
        getItem()
    }

    fun refreshItems() {
        _uiState.update {
            it.copy(
                isRefreshing = true
            )
        }
        getItem()
    }

    fun getItem() {
       viewModelScope.launch {
           homeUiState = try {
               HomeUiState.Success(repository.getItems())
           } catch (e: IOException) {
               HomeUiState.Error(message = e.message)
           } catch (e: retrofit2.HttpException) {
               HomeUiState.Error(message = e.message)
           } catch (e: Throwable) {
               HomeUiState.Error(message = e.message)
           }
       }
    }


    suspend fun seeHairAllClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            _uiState.update {
                it.copy(
                    isShowingHomePage = false,
                    itemType = repository.getItems().filter {  hair ->
                        hair.items.itemType == "hair"
                    }
                )
            }
        }
    }

    suspend fun seeBagALlClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            _uiState.update {
                it.copy(
                    isShowingHomePage = false,
                    itemType = repository.getItems().filter {  bag ->
                        bag.items.itemType == "bag"
                    }
                )
            }
        }
    }

    suspend fun seeShoeALlClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            _uiState.update {
                it.copy(
                    isShowingHomePage = false,
                    itemType = repository.getItems().filter { shoe ->
                        shoe.items.itemType == "shoe"
                    }
                )
            }
        }
    }

    suspend fun seeClotheALlClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            _uiState.update {
                it.copy(
                    isShowingHomePage = false,
                    itemType = repository.getItems().filter { clothe ->
                        clothe.items.itemType == "clothe"
                    }
                )
            }
        }
    }

    fun listBackClicked() {
        _uiState.update {
            it.copy(
                isShowingHomePage = true,
                itemType = emptyList()
            )
        }
    }





}