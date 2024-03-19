package com.example.jenstore.ui.screens.home


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.serialization.json.Json
import okio.IOException


//val itemBag: List<ProductItem>, val itemShoe: List<ProductItem>, val itemClothe: List<ProductItem>

sealed interface HomeUiState {
    data class Success(val item: List<ProductItem>) : HomeUiState
    data class Error(val message: String?) : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(private val repository: Repository) : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getItem()
    }

    suspend fun seeHairAllClicked() {
        _uiState.update {
            it.copy(
                itemType = repository.getItems().filter {  hair ->
                    hair.items.itemType == "hair"
                },
                isShowingHomePage = false
            )
        }
    }

    suspend fun seeBagALlClicked() {
        _uiState.update {
            it.copy(
                itemType = repository.getItems().filter {  bag ->
                    bag.items.itemType == "bag"
                },
                isShowingHomePage = false

            )
        }
    }

    suspend fun seeShoeALlClicked() {
        _uiState.update {
            it.copy(
                itemType = repository.getItems().filter {  shoe ->
                    shoe.items.itemType == "shoe"
                },
                isShowingHomePage = false
            )
        }
    }

    suspend fun seeClotheALlClicked() {
        _uiState.update {
            it.copy(
                itemType = repository.getItems().filter {  clothe ->
                    clothe.items.itemType == "clothe"
                },
                isShowingHomePage = false
            )
        }
    }

    fun listBackClicked() {
        _uiState.update {
            it.copy(
                isShowingHomePage = true,
                itemType = emptyList()
            )
        }
    }



    fun getItem() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                HomeUiState.Success(repository.getItems())
            } catch (e: IOException) {
                HomeUiState.Error(message = e.message)
            } catch (e: retrofit2.HttpException) {
                HomeUiState.Error(message = e.message)
            } catch (e: Throwable) {
                HomeUiState.Error(message = e.message)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as StoreApplication)
                val repository = application.container.repository
                HomeViewModel(repository = repository)
            }
        }
    }
}