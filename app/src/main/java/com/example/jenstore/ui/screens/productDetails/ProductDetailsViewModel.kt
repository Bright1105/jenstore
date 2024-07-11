package com.example.jenstore.ui.screens.productDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.repository.LocalRepository
import com.google.firebase.FirebaseError
import com.google.firebase.FirebaseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ProductDetailsUiState(
    val currentProduct: Item? = null,
)

sealed class AddEvent{

    class Info(val message: String): AddEvent()

    class Error(val message: String, val throwable: Throwable): AddEvent()
}

// set he loading and error and success

sealed interface ProductDetails {
    data class Success(val currentProduct: Item? = null) : ProductDetails

    data class Error(val message: String?) : ProductDetails

    object Loading : ProductDetails
}



data class Errors(val message: String? = null)

class ProductDetailsViewModel(
    private val firebaseRepository: FirebaseRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    var productDetails: ProductDetails by mutableStateOf(ProductDetails.Loading)
        private set

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState

    val currentColor = mutableStateOf(Color.Yellow)

    val currentSize = mutableStateOf("S")

    val onFavourite = mutableStateOf(false)

    val countItem = mutableIntStateOf(1)


    private val _addEvent: MutableSharedFlow<AddEvent> = MutableSharedFlow()
    val addEvent: Flow<AddEvent>
        get() = _addEvent

    fun onAddToCartClicked(productItem: Item) {
        CoroutineScope(Dispatchers.IO).launch {

            runCatching {
                withContext(Dispatchers.IO) {
                    localRepository.addToCart(
                        ordersEntity = OrdersEntity(
                            id = productItem.id,
                            title = productItem.name,
                            brand = productItem.brand,
                            price = productItem.price,
                            description = productItem.description,
                            dateCreated = productItem.dateCreated?.toString(),
                            image = productItem.imageUri[0],
                            countItem = countItem.intValue,
                            itemType = productItem.itemType
                        )
                    )
                }
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    _addEvent.emit(AddEvent.Info("Product '${productItem}' successfully added to cart."))
                }
            }.onFailure {
                withContext(Dispatchers.Main) {}
                _addEvent.emit(AddEvent.Error("There was an error while adding the product to cart", it))
            }
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

    fun productItemById(id: String) {
        viewModelScope.launch {
            productDetails = try {
                productDetails = ProductDetails.Loading
                delay(1000)
                ProductDetails.Success(currentProduct = firebaseRepository.getProductId(id))
            } catch (e: Throwable) {
                ProductDetails.Error(e.message)
            } catch (e: FirebaseException) {
                ProductDetails.Error(e.message)
            }
        }
    }

//    fun productItemById(id: String) {
//        viewModelScope.launch {
//            try {
//                _uiState.update {
//                    it.copy(
//                        currentProduct = firebaseRepository.getProductId(id),
//                    )
//                }
//            } catch (e: Throwable) {
//                e.message
//            }
//        }
//    }
}
