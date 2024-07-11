package com.example.jenstore.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.repository.LocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(

    private val localRepository: LocalRepository
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

}

data class CartUiState(
    val items: List<OrdersEntity> = listOf()
)