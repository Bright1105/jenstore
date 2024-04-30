package com.example.jenstore.ui.screens.home



import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import com.example.jenstore.data.Repository
import com.example.jenstore.data.local.home.ProductEntity
import com.example.jenstore.data.mappers.toProductItem
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.data.remote.homeProduct.ProductDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    val pressed = mutableStateOf(false)


    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
            private set


  //  val productPagingFlow = pager
    //        .flow
    //        .map {  pagingData ->
    //            pagingData.map {
    //                it.toProductX()
    //            }
    //        }
    //        .cachedIn(viewModelScope)

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
        _uiState.update {
            it.copy(isRefreshing = false)
        }
    }

    fun getItem() {
       viewModelScope.launch {
           homeUiState = HomeUiState.Loading
           homeUiState = try {
               HomeUiState.Success(
                   item = repository.getItems().map {
                       it.toProductItem()
                   }
               )
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
                    itemType = repository.getItems().map {  dto ->
                        dto.toProductItem()
                    }.filter { hair ->
                        hair.itemType == "hair"
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
                    itemType = repository.getItems().map {  dto ->
                        dto.toProductItem()
                    }.filter { bag ->
                        bag.itemType == "bag"
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
                    itemType = repository.getItems().map {  dto ->
                        dto.toProductItem()
                    }.filter { shoe ->
                        shoe.itemType == "shoe"
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
                    itemType = repository.getItems().map {  dto ->
                        dto.toProductItem()
                    }.filter { clothe ->
                        clothe.itemType == "clothe"
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