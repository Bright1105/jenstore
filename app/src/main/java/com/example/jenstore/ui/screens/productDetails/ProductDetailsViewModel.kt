package com.example.jenstore.ui.screens.productDetails

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class ProductDetailsUiState(
    val currentProduct: Item? = null,
)

sealed class AddEvent{

    class Info(val message: String): AddEvent()

    class Error(val message: String, val throwable: Throwable): AddEvent()
}


data class Errors(val message: String? = null)

class ProductDetailsViewModel(
    private val repository: Repository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    val currentColor = mutableStateOf(Color.Yellow)

    val currentSize = mutableStateOf("S")

    val onFavourite = mutableStateOf(false)

    val countItem = mutableIntStateOf(1)


    private val _addEvent: MutableSharedFlow<AddEvent> = MutableSharedFlow()
    val addEvent: Flow<AddEvent>
        get() = _addEvent

   // fun onAddToCartClicked(productItem: ProductItem) {
    //        CoroutineScope(Dispatchers.IO).launch {
    //
    //            runCatching {
    //                withContext(Dispatchers.IO) {
    //                    repository.addToCart(
    //                        ordersEntity = OrdersEntity(
    //                            id = productItem.id,
    //                            title = productItem.title,
    //                            brand = productItem.brand,
    //                            price = productItem.price,
    //                            description = productItem.description,
    //                            dateCreated = productItem.dateCreated,
    //                            image = productItem.image,
    //                            countItem = countItem.intValue
    //                        )
    //                    )
    //                }
    //            }.onSuccess {
    //                withContext(Dispatchers.Main) {
    //                    _addEvent.emit(AddEvent.Info("Product '${productItem}' successfully added to cart."))
    //                }
    //            }.onFailure {
    //                withContext(Dispatchers.Main) {}
    //                _addEvent.emit(AddEvent.Error("There was an error while adding the product to cart", it))
    //            }
    //        }
    //    }

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

    suspend fun productItemById(id: String) {
        _uiState.update {
            it.copy(
                currentProduct = repository.getProductId(id),
            )
        }
    }
}
