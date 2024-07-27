package com.example.jenstore.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.model.Checkout
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.model.UserAddress
import com.example.jenstore.data.model.UserInformation
import com.example.jenstore.data.repository.LocalRepository
import com.example.jenstore.data.service.AccountService
import com.example.jenstore.data.service.StorageService
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val storageService: StorageService,
    private val localRepository: LocalRepository,
    private val accountService: AccountService
) : ViewModel() {

    val cartUiState: StateFlow<CartUiState> =
        localRepository.getAllItems().map { CartUiState(it) }
    /**
     * Use the stateIn operator to convert the Flow into a StateFlow.
     * The StateFlow is the observable API for UI state, which enables the UI to update itself.
     */
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000L),
                CartUiState()
            )


//    val userInformation: Flow<UserInformation?> = storageService.userInfo
//    val userAddress = storageService.getUserAddress

    fun increaseCount(orders: OrdersEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            localRepository.count(orders.copy(
                countItem = orders.countItem + 1
            ))
        }
    }

    fun decreaseCount(orders: OrdersEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            localRepository.count(orders.copy(
                countItem = orders.countItem - 1
            ))
        }
    }

    fun deleteItem(orders: OrdersEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            localRepository.delete(orders)
        }
    }

    fun clearCart(orders: List<OrdersEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            localRepository.clearCart(orders)
        }
    }

    suspend fun onCheckout(ordersEntity: List<OrdersEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (orders in ordersEntity) {
                val checkout = Checkout(
                    userId = accountService.currentUserId,
                    ordersEntity = orders,
                    dateCreated = Timestamp.now()
                )
                storageService.checkout(checkout)
            }
        }
    }

}

data class CartUiState(
    val items: List<OrdersEntity> = listOf()
)