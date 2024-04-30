package com.example.jenstore.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jenstore.data.Repository
import com.example.jenstore.data.mappers.toProductItem
import com.example.jenstore.data.model.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SearchUiState(
    val searching: Boolean = false,
    val isShowingSearchHome: Boolean = true,
    val item: List<ProductItem> = listOf()
)


class SearchViewModel(
    private val repository: Repository,
) : ViewModel() {


    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState


    private val _searchItem = MutableStateFlow("")

    val searchItem = _searchItem.asStateFlow()



    private val _product = MutableStateFlow<List<ProductItem>>(emptyList())
    val products: StateFlow<List<ProductItem>> = _product
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    fun getQueryProduct() {
        viewModelScope.launch {
            val response = repository.searchQuery(_searchItem.value).map { dto ->
                dto.toProductItem()
            }
            _product.value = response
        }
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




    suspend fun onHairClicked() {
        _uiState.update {
            it.copy(
                item = repository.getItems().filter { filter ->
                    filter.items.itemType == "hair"
                }.map {  dto ->
                      dto.toProductItem()
                },
                isShowingSearchHome = false
            )
        }
    }

    suspend fun onShoeClicked() {
        _uiState.update {
            it.copy(
                item = repository.getItems().filter {shoe ->
                    shoe.items.itemType == "shoe"
                }.map { dto ->
                    dto.toProductItem()
                },
                isShowingSearchHome = false
            )
        }
    }

    suspend fun onBagClicked() {
        _uiState.update {
            it.copy(
                item = repository.getItems().filter { bag ->
                    bag.items.itemType == "bag"
                }.map {  dto ->
                    dto.toProductItem()
                },
                isShowingSearchHome = false
            )
        }
    }

    suspend fun onClotheClicked() {
        _uiState.update {
            it.copy(
                item = repository.getItems().filter { clothe ->
                    clothe.items.itemType == "clothe"
                }.map {  dto ->
                    dto.toProductItem()
                },
                isShowingSearchHome = false
            )
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
        _uiState.update {
            it.copy(
                item = emptyList(),
                isShowingSearchHome = true
            )
        }
    }
}