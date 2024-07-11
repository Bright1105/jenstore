package com.example.jenstore.ui.screens.profile.account

import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope

import com.example.jenstore.STORE_DEFAULT_ID
import com.example.jenstore.Splash
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Gender
import com.example.jenstore.data.model.UserInformation
import com.example.jenstore.data.model.genderItem
import com.example.jenstore.data.service.AccountService
import com.example.jenstore.data.service.StorageService
import com.example.jenstore.ui.screens.StoreAppViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class AccountViewModel(
    private val accountService: AccountService,
    private val storageService: StorageService
) : StoreAppViewModel() {

    private val _accountUiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _accountUiState

    val firstName = MutableStateFlow("")
    val middleName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val gender = MutableStateFlow("")
    val phoneNumber = MutableStateFlow("")

    val image = MutableStateFlow<Uri?>(null)

    val items: List<Gender> = genderItem

    var userInfo: Flow<UserInformation?> = storageService.userInfo


    fun initialize(restartApp: (StoreDestinations) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(Splash)
            }
        }
    }


    val email: String? = Firebase.auth.currentUser?.email

    fun saveImage(uri: Uri?) {
        launchCatching {
            storageService.addImageToStorage(uri!!)
        }
    }

    fun saveUser(popUpScreen: () -> Unit) {
        launchCatching {
            val userInformation = UserInformation(
                userId = accountService.currentUserId,
                firstName = firstName.value,
                middleName = middleName.value,
                lastName = lastName.value,
                gender = gender.value,
                phoneNumber = phoneNumber.value
            )
            storageService.createUserInformation(userInformation)
        }
        popUpScreen()
    }

//    fun cancel() {
//        image.value = null
//        firstName.value = ""
//        middleName.value = ""
//        lastName.value = ""
//    }

    fun updateFirstName(firstName: String) {
        this.firstName.value = firstName
    }

    fun updateMiddleName(middleName: String) {
        this.middleName.value = middleName
    }

    fun updateLastName(lastName: String) {
        this.lastName.value = lastName
    }

    fun updateGender(gender: String) {
        this.gender.value = gender
    }

    fun updatePhoneNumber(phoneNumber: String) {
        this.phoneNumber.value = phoneNumber
    }

    fun notEditProfile() {
        firstName.value = ""
        middleName.value = ""
        lastName.value = ""
        gender.value = ""
        phoneNumber.value = ""
        image.value = null
    }

    fun editProfile() {
        _accountUiState.update {
            it.copy(
                editProfile = true
            )
        }
    }

    companion object {
        private val DEFAULT_ACC = UserInformation(id = STORE_DEFAULT_ID)
    }
}