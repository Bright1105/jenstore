package com.example.jenstore.ui.screens.productDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jenstore.StoreApplication
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.ProductItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductDetailsUiState(
    val currentProduct: ProductItem? = null
)

class ProductDetailsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState


    suspend fun productItemById(id: Int) {
        _uiState.update {
            it.copy(
                currentProduct = repository.getItemById(id)
            )
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this [APPLICATION_KEY] as StoreApplication)
                val repository = application.container.repository
                ProductDetailsViewModel(repository = repository)
            }
        }
    }
}