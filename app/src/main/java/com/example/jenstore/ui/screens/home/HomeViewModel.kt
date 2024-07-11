package com.example.jenstore.ui.screens.home



import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.model.Item
import com.google.firebase.FirebaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data class Success(val item: List<Item>) : HomeUiState
    data class Error(val message: String?) : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState


    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
            private set


    init {
        getProduct()
    }

 //   var type = mutableStateOf("")


    fun getProduct() {
        viewModelScope.launch {
            homeUiState = try {
                HomeUiState.Loading
                HomeUiState.Success(firebaseRepository.getProducts())
            } catch (e: Throwable) {
                HomeUiState.Error(e.message)
            }
        }
    }

    fun refreshItems() {
       CoroutineScope(Dispatchers.IO).launch {
           _uiState.update {
               it.copy(
                   isRefreshing = true
               )
           }
           getProduct()
          delay(5000)
           _uiState.update {
               it.copy(
                   isRefreshing = false
               )
           }
       }
    }


    fun seeHairAllClicked() {
       CoroutineScope(Dispatchers.IO).launch {
//           type.value = "hair"
           try {
               _uiState.update {
                   it.copy(
                       typeOfProduct = "hair",
                       item = firebaseRepository.getProductsPagination(),
                       isShowingHomePage = false
                   )
               }
           } catch (e: Throwable) {
               e.message
           }
       }
    }

    fun seeBagALlClicked() {
        CoroutineScope(Dispatchers.IO).launch {
 //           type.value = "bag"
            try {
                _uiState.update {
                    it.copy(
                        typeOfProduct = "bag",
                        item = firebaseRepository.getProductsPagination(),
                        isShowingHomePage = false
                    )
                }
            } catch (e: Throwable) {
                e.message
            }
        }
    }


    fun seeAccessoriesClicked() {
        CoroutineScope(Dispatchers.IO).launch {
           // type.value = "shoe"
            try {
                _uiState.update {
                    it.copy(
                        typeOfProduct = "Accessories",
                        item = firebaseRepository.getProductsPagination(),
                        isShowingHomePage = false
                    )
                }
            } catch (e: Throwable) {
                e.message
            }
        }
    }

    fun seeMakeupALlClicked() {
        CoroutineScope(Dispatchers.IO).launch {
         //   type.value = "clothe"
            try {
                _uiState.update {
                    it.copy(
                        typeOfProduct = "Makeup",
                        item = firebaseRepository.getProductsPagination(),
                        isShowingHomePage = false
                    )
                }
            } catch (e: Throwable) {
                e.message
            }
        }
    }


    fun listBackClicked() {
        _uiState.update {
            it.copy(
                item = null,
                typeOfProduct = null,
                isShowingHomePage = true,
            )
        }
    }





}