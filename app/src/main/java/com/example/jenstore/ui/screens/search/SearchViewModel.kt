package com.example.jenstore.ui.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SearchUiState(
    val searching: Boolean = false,
    val isShowingSearchHome: Boolean = true,
    val item: Flow<PagingData<Item>> = flowOf(),
)


data class Errors(val message: String? = null)

class SearchViewModel(
    private val repository: Repository,
) : ViewModel() {


    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private val _error = MutableStateFlow(Errors())
    val error: StateFlow<Errors> = _error

    private val _searchItem = MutableStateFlow("")

    val searchItem = _searchItem.asStateFlow()


    private val _product = MutableStateFlow<List<Item>>(listOf())
    val products: StateFlow<List<Item>> = _product
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())



    //val product = repository.getProductsPagination().cachedIn(viewModelScope)

  //  var itemT = mutableStateOf("")


    private var type = mutableStateOf("")

    fun getQueryProduct() {
        viewModelScope.launch {
            val response = repository.searchProduct(_searchItem.value)

            _product.value = response
        }


        //repository.searchQuery(_searchItem.value).map { dto ->
        //                dto.toProductItem()
    }


    fun searchQuery(query: String) {
        viewModelScope.launch {
            _searchItem.value = query
            _uiState.update {
                it.copy(
                    searching = true,
                )
            }
            if (searchItem.value.isNotBlank()) {
                getQueryProduct()
            }
        }
    }





    fun onHairClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            type.value = "hair"
            _uiState.update {
                it.copy(
                    item = repository.getProductsPagination().map {  paging ->
                        paging.filter {  item ->
                            item.itemType == type.value
                        }
                    },
                    isShowingSearchHome = false
                )
            }
        }

    }

    fun onShoeClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            type.value = "shoe"
            _uiState.update {
                it.copy(
                    item = repository.getProductsPagination().map {  paging ->
                        paging.filter {  item ->
                            item.itemType == type.value
                        }
                    },
                    isShowingSearchHome = false
                )
            }
        }
    }

    fun onBagClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            type.value = "bag"
            _uiState.update {
                it.copy(
                    item = repository.getProductsPagination().map {  paging ->
                        paging.filter {  item ->
                            item.itemType == type.value
                        }
                    },
                    isShowingSearchHome = false
                )
            }
        }
    }

    fun onClotheClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            type.value = "clothe"
            _uiState.update {
                it.copy(
                    item = repository.getProductsPagination().map {  paging ->
                        paging.filter {  item ->
                            item.itemType == type.value
                        }
                    },
                    isShowingSearchHome = false
                )
            }
        }
    }

    fun onSearchBackClicked() {
        _uiState.update {
            it.copy(
                searching = false
            )
        }
        _product.value = emptyList()
        _searchItem.value = ""
    }
    fun onBackClicked() {
        type.value = ""
        _uiState.update {
            it.copy(
                item = flowOf(),
                isShowingSearchHome = true
            )
        }
    }
}