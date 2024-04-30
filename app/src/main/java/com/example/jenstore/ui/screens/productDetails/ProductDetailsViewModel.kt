package com.example.jenstore.ui.screens.productDetails

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.jenstore.data.Repository
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.mappers.toProductItem
import com.example.jenstore.data.model.ProductItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductDetailsUiState(
    val currentProduct: ProductItem? = null,
)

class ProductDetailsViewModel(
    private val repository: Repository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    val currentColor = mutableStateOf(Color.Yellow)

    val currentSize = mutableStateOf("S")

    val onFavourite = mutableStateOf(false)

    val countItem = mutableIntStateOf(1)



    fun onAddToCartClicked(productItem: ProductItem) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.addToCart(
                ordersEntity = OrdersEntity(
                    id = productItem.id,
                    title = productItem.title,
                    brand = productItem.brand,
                    price = productItem.price,
                    description = productItem.description,
                    dateCreated = productItem.dateCreated,
                    image = productItem.image,
                    countItem = countItem.intValue
                )
            )
        }
    }

    fun increaseCountItem() {
        countItem.intValue += 1
    }

    fun decreaseCountItem() {
        countItem.intValue -= 1
    }

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
                currentProduct = repository.getItemById(id).toProductItem()
            )
        }
    }

}
