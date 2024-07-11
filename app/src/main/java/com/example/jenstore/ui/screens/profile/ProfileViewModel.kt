package com.example.jenstore.ui.screens.profile

import com.example.jenstore.Login
import com.example.jenstore.Splash
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.UserInformation
import com.example.jenstore.data.service.AccountService
import com.example.jenstore.data.service.StorageService
import com.example.jenstore.ui.screens.StoreAppViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

class ProfileViewModel(
    private val accountService: AccountService,
    private val storageService: StorageService
) : StoreAppViewModel() {


    val email: String? = Firebase.auth.currentUser?.email

    var userInfo: Flow<UserInformation?>? = null

    fun initialize(restartApp: (StoreDestinations) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(Splash) else userInfo = storageService.userInfo
            }
        }
    }

    fun onSignOutClick(openAndPopup: (StoreDestinations, StoreDestinations) -> Unit) {
        launchCatching {
            userInfo = null
            accountService.signOut()
            openAndPopup(Login, Splash)
        }
    }

    fun onDeleteAccountClick() {
        launchCatching {
            accountService.deleteAccount()
        }
    }
}