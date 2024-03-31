package com.example.jenstore.ui.screens.search

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jenstore.StoreApplication
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.ProductItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SearchUiState(
    val queryItem: List<ProductItem> = emptyList(),
    val searching: Boolean = false,
    val isShowingSearchHome: Boolean = true,
    val item: List<ProductItem> = listOf()
)

class SearchViewModel(
    private val repository: Repository,
    private val searchMatchQuery: SearchMatchQuery
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _searchItem = MutableStateFlow("")
    val searchItem = _searchItem.asStateFlow()


    init {
       // getQueryProduct()
    }

    // val queryItem = MutableStateFlow(QueryItems(
    //        queryItems = repository.searchQuery(_searchItem.value)
    //    )).stateIn(
    //        viewModelScope,
    //        SharingStarted.WhileSubscribed(2000L),
    //        null
    //    )

    private val _product = MutableStateFlow(_uiState.value.queryItem)

    val product = searchItem
        .combine(_product) { text, product ->
            if (text.isBlank()) {
                product
            } else {
                product.filter {
                    it.items.title == text
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _product.value
        )



    fun getQueryProduct() {
       viewModelScope.launch {
           SearchUiState(
               queryItem = repository.searchQuery(_searchItem.value)
           )
       }
    }


    fun searchQuery(query: String) {
        _searchItem.value = query
        _uiState.update {
            it.copy(
                searching = true
            )
        }
    }


    suspend fun onHairClicked() {
        _uiState.update {
            it.copy(
               // item = repository.getItems().filter {
                //                      it.items.itemType == "hair"
                //                },
                isShowingSearchHome = false
            )
        }
    }

    suspend fun onShoeClicked() {
        _uiState.update {
            it.copy(
             //   item = repository.getItems().filter {
                //                    it.items.itemType == "shoe"
                //                },
                isShowingSearchHome = false
            )
        }
    }

    suspend fun onBagClicked() {
        _uiState.update {
            it.copy(
                //item = repository.getItems().filter {
                //                    it.items.itemType == "bag"
                //                },
                isShowingSearchHome = false
            )
        }
    }

    suspend fun onClotheClicked() {
        _uiState.update {
            it.copy(
                //item = repository.getItems().filter {
                //                    it.items.itemType == "clothe"
                //                },
                isShowingSearchHome = false
            )
        }
    }

    fun onBackClicked() {
        _uiState.update {
            it.copy(
                item = emptyList(),
                isShowingSearchHome = true
            )
        }
    }
}