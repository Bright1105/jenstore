package com.example.jenstore.ui.screens.profile.account.orders

import androidx.lifecycle.ViewModel
import com.example.jenstore.data.local.cart.CheckoutEntity
import com.example.jenstore.data.model.Checkout
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.repository.LocalRepository
import com.example.jenstore.data.service.StorageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class OrderUiState(
    val checkout: Checkout? = null,
    val orderDetails: Boolean = false,
    val makePayment: Boolean = false,
    val cancelAlert: Boolean = false,
    val paymentMade: Boolean = false,
)

class OrdersViewModel(
    private val storageService: StorageService,
    private val firebaseRepository: FirebaseRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _orderUiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _orderUiState

    val orders: Flow<List<Checkout>> = storageService.getCheckout

    val checkoutEntity: Flow<List<CheckoutEntity>> = localRepository.getCheckoutEntity()

    suspend fun updateCanceled(checkout: Checkout) {
        CoroutineScope(Dispatchers.IO).launch {
            localRepository.addToCheckout(
                checkoutEntity = CheckoutEntity(
                    id = checkout.id,
                    title = checkout.ordersEntity.title,
                    image = checkout.ordersEntity.image,
                    dateCreated = checkout.dateCreated?.toDate().toString()
                )
            )
            storageService.deleteCheckout(checkout)
        }
    }

    suspend fun getCheckoutById(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                _orderUiState.update {
                    it.copy(
                        checkout = storageService.getCheckoutById(id)
                    )
                }
                _orderUiState.update {
                    it.copy(
                        orderDetails = true
                    )
                }
            }
        }
    }

    fun back() {
        _orderUiState.update {
            it.copy(
                orderDetails = false,
                makePayment = false,
                cancelAlert = false
            )
        }
    }

    fun madePayment() {
        _orderUiState.update {
            it.copy(makePayment = true)
        }
    }

    fun makePayment() {
        _orderUiState.update {
            it.copy(
                makePayment = true
            )
        }
    }
    fun cancelMakePayment() {
        _orderUiState.update {
            it.copy(
                makePayment = false
            )
        }
    }

    fun alert() {
        _orderUiState.update {
            it.copy(
                cancelAlert = true
            )
        }
    }

    fun cancelAlert() {
        _orderUiState.update {
            it.copy(cancelAlert = false)
        }
    }

    suspend fun getJennyInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                firebaseRepository.getJennyInfo()
            }
        }
    }
}