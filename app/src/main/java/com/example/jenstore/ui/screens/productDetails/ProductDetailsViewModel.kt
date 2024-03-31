package com.example.jenstore.ui.screens.productDetails

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.ProductItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class ProductDetailsUiState(
    val currentProduct: ProductItem? = null,
    val quantity: Int = 1
)

class ProductDetailsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    val currentColor = mutableStateOf(Color.Yellow)

    val currentSize = mutableStateOf("S")

    val onFavourite = mutableStateOf(false)

    fun onFavouriteClicked() {
        onFavourite.value = true
    }
    fun onColorSelected(color: Color) {
        currentColor.value = color
    }

    fun onSizeSelected(size: String) {
        currentSize.value = size
    }

    suspend fun productItemById(id: Int) {
        _uiState.update {
            it.copy(
                currentProduct = repository.getItemById(id)
            )
        }
    }

}