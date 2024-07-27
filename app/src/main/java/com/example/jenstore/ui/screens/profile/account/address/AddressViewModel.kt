package com.example.jenstore.ui.screens.profile.account.address

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.jenstore.Splash
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Region
import com.example.jenstore.data.model.UserAddress
import com.example.jenstore.data.model.cities
import com.example.jenstore.data.model.regions
import com.example.jenstore.data.service.AccountService
import com.example.jenstore.data.service.StorageService
import com.example.jenstore.ui.screens.StoreAppViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


data class AddressUiState(
    val editAddress: Boolean = false
)



class AddressViewModel(
    private val accountService: AccountService,
    private val storageService: StorageService
) : StoreAppViewModel() {

    private val _addressUiState = MutableStateFlow(AddressUiState())
    val addressUiState: StateFlow<AddressUiState> = _addressUiState


    val regionList: Region = regions
    val cityList = cities

    val address = MutableStateFlow("")
    val additionalInformation = MutableStateFlow("")
    val region = MutableStateFlow("")
    val city = MutableStateFlow("")


    fun initialize(restartApp: (StoreDestinations) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(Splash)
            }
        }
    }
    val userAddress: Flow<UserAddress?> = storageService.getUserAddress

    fun updateAddress(address: String) {
        this.address.value = address
    }

    fun updateAdditionalInformation(additionalInformation: String) {
        this.additionalInformation.value = additionalInformation
    }

    fun updateRegion(region: String) {
        this.region.value = region
    }

    fun updateCity(city: String) {
        this.city.value = city
    }

    fun editAddress() {
        _addressUiState.update {
            it.copy(
                editAddress = true
            )
        }
    }

    fun saveAddress(popUpScreen: () -> Unit) {
        launchCatching {
            if (address.value.isEmpty() || additionalInformation.value.isEmpty() || region.value.isEmpty() || city.value.isEmpty()) {
                _addressUiState.update {
                    it.copy(
                        editAddress = false
                    )
                }
            } else {
                val userAddress = UserAddress(
                    userId = accountService.currentUserId,
                    address = address.value,
                    additionalInformation = additionalInformation.value,
                    region = region.value,
                    city = city.value
                )
                storageService.createUserAddress(userAddress)
                popUpScreen()
            }
        }
    }


}