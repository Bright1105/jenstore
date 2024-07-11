package com.example.jenstore.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.example.jenstore.Login
import com.example.jenstore.Profile
import com.example.jenstore.Splash
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.service.AccountService

class SplashViewModel(
    private val accountService: AccountService
) : ViewModel() {

    fun onProfile(openAndPopUp: (StoreDestinations, StoreDestinations) -> Unit) {
        if (accountService.hasUser()) openAndPopUp(Profile, Splash)
        else openAndPopUp(Login, Splash)
    }
}