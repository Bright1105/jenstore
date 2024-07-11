package com.example.jenstore.ui.screens.profile.loginAccount

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jenstore.Login
import com.example.jenstore.Profile
import com.example.jenstore.Register
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.model.User
import com.example.jenstore.data.service.AccountService
import com.example.jenstore.ui.screens.StoreAppViewModel
import com.example.jenstore.ui.screens.profile.createAccount.RegisterScreen
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


data class LoginAccountMessage(val message: String?)

class LoginViewModel(
    private val accountService: AccountService
) : StoreAppViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState


    fun updateEmail(newEmail: String) {
        this.email.value = newEmail
    }


    fun updatePassword(newPassword: String) {
       this.password.value = newPassword
    }


    fun onSignInClick(openAndPopUp: (StoreDestinations, StoreDestinations) -> Unit) {
        launchCatching {
            runCatching {
                accountService.signIn(email.value, password.value)
                _loginUiState.update {
                    it.copy(
                        loading = true
                    )
                }
                delay(3000)
                openAndPopUp(Profile, Login)
            }.onFailure {  fail ->
                _loginUiState.update {
                    it.copy(
                        loading = false,
                        message = fail.message
                    )
                }
            }
        }
    }

   fun onSignUpClick(openAndPopUp: (StoreDestinations, StoreDestinations) -> Unit) {
       openAndPopUp(Register, Login)
   }
}
