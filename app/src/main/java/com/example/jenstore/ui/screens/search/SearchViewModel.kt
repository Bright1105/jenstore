package com.example.jenstore.ui.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import androidx.paging.map
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.model.PaginationProducts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SearchUiState(
    val searching: Boolean = false,
    val isShowingSearchHome: Boolean = true,
    val item: Flow<PagingData<Item>>? = null,
    val typeOfProducts: String? = null
)


data class Errors(val message: String? = null)

class SearchViewModel(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {


    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _error = MutableStateFlow(Errors())
    val error: StateFlow<Errors> = _error

    private val _searchItem = MutableStateFlow("")

    val searchItem = _searchItem.asStateFlow()


    private val _products = MutableStateFlow(firebaseRepository.getProductsPagination().cachedIn(viewModelScope))

    val product = searchItem
        .combine(_products) { text, product ->
            if (text.isBlank()) {
                product
            } else {
                product.map {  paging ->
                    delay(1000)
                    paging.filter {
                        it.doesMatchSearchQuery(text)
                    }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)

 //   var type = mutableStateOf("")



    fun searchQuery(query: String) {
        viewModelScope.launch {
            try {
                _searchItem.value = query
                _uiState.update {
                    it.copy(
                        searching = true,
                    )
                }
            } catch (e: Exception) {
                e.message
            } catch (e: Throwable) {
                e.message
            }
        }
    }





    fun onHairClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _uiState.update {
                    it.copy(
                        typeOfProducts = "hair",
                        item = firebaseRepository.getProductsPagination(),
                        isShowingSearchHome = false
                    )
                }
            } catch (e: Throwable) {
                e.message
            } catch (e: Exception) {
                e.message
            }
        }

    }

    fun onAccessoriesClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _uiState.update {
                    it.copy(
                        typeOfProducts = "Accessories",
                        item = firebaseRepository.getProductsPagination(),
                        isShowingSearchHome = false
                    )
                }
            } catch (e: Throwable) {
                e.message
            } catch (e: Exception) {
                e.message
            }
        }
    }

    fun onMakeupClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _uiState.update {
                    it.copy(
                        typeOfProducts = "Makeup",
                        item = firebaseRepository.getProductsPagination(),
                        isShowingSearchHome = false
                    )
                }
            } catch (e: Throwable) {
                e.message
            } catch (e: Exception) {
                e.message
            }
        }
    }

    fun onBagClicked() {
        CoroutineScope(Dispatchers.IO).launch {
           try {
               _uiState.update {
                   it.copy(
                       typeOfProducts = "bag",
                       item = firebaseRepository.getProductsPagination(),
                       isShowingSearchHome = false
                   )
               }
           } catch (e: Throwable) {
               e.message
           } catch (e: Exception) {
               e.message
           }
        }
    }

    fun onSearchBackClicked() {
        _uiState.update {
            it.copy(
                searching = false
            )
        }
       // _product.value = emptyList()
        _searchItem.value = ""
    }
    fun onBackClicked() {
        _uiState.update {
            it.copy(
                typeOfProducts = null,
                item = null,
                isShowingSearchHome = true
            )
        }
    }
}