package com.example.jenstore.ui.screens.home



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data class Success(val item: List<Item>) : HomeUiState
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


    init {
        getProduct()
    }

    //val products = repository.getProductsPagination().cachedIn(viewModelScope)

  //  val itemType = mutableStateOf("")

    private var type = mutableStateOf("")
    fun getProduct() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Success(repository.getProduct())
        }
    }
    fun refreshItems() {
        _uiState.update {
            it.copy(
                isRefreshing = true
            )
        }
       // getItem()
       // _uiState.update {
        //            it.copy(isRefreshing = false)
        //        }
    }


    fun seeHairAllClicked() {
       CoroutineScope(Dispatchers.IO).launch {
           type.value = "hair"
           _uiState.update {
               it.copy(
                   item = repository.getProductsPagination().map {  paging ->
                       paging.filter {  hair ->
                           hair.itemType == type.value
                       }
                   },
                   isShowingHomePage = false

                   //itemType =  repository.getProductsPagination()
                   //                        .map { paging ->
                   //                            paging.filter {   hair ->
                   //                                hair.itemType == "hair"
                   //                            }
                   //                        }
                   //                        .cachedIn(viewModelScope),


                   //repository.getProduct().filter { hair ->
                   //                  hair.itemType == "hair"
                   //            }
               )
           }
       }
    }

    fun seeBagALlClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            type.value = "bag"
            _uiState.update {
                it.copy(
                    item = repository.getProductsPagination().map {  paging ->
                        paging.filter {  hair ->
                            hair.itemType == type.value
                        }
                    },
                    isShowingHomePage = false

                        //repository.getProductsPagination()
                    //                        .map { paging ->
                    //                            paging.filter {   bag ->
                    //                                bag.itemType == "bag"
                    //                            }
                    //                        }
                    //                        .cachedIn(viewModelScope)
                )
            }
        }
    }


    fun seeShoeALlClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            type.value = "shoe"
            _uiState.update {
                it.copy(
                    item = repository.getProductsPagination().map {  paging ->
                        paging.filter {  hair ->
                            hair.itemType == type.value
                        }
                    },
                    isShowingHomePage = false,

                        //repository.getProductsPagination()
                    //                        .map { paging ->
                    //                            paging.filter {   shoe ->
                    //                                shoe.itemType == "shoe"
                    //                            }
                    //                        }
                    //                        .cachedIn(viewModelScope)
                )
            }
        }
    }

    fun seeClotheALlClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            type.value = "clothe"
            _uiState.update {
                it.copy(
                    item = repository.getProductsPagination().map {  paging ->
                        paging.filter {  hair ->
                            hair.itemType == type.value
                        }
                    },
                    isShowingHomePage = false


                    //repository.getProductsPagination()
                    //                        .map { paging ->
                    //                            paging.filter {   clothe ->
                    //                                clothe.itemType == "clothe"
                    //                            }
                    //                        }
                    //                        .cachedIn(viewModelScope)
                )
            }
        }
    }


    fun listBackClicked() {
        type.value = ""
        _uiState.update {
            it.copy(
                item = flowOf(),
                isShowingHomePage = true,
            )
        }
    }





}