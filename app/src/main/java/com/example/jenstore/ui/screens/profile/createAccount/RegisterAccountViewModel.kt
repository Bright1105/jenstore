package com.example.jenstore.ui.screens.profile.createAccount

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jenstore.Login
import com.example.jenstore.Profile
import com.example.jenstore.Register
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.service.AccountService
import com.example.jenstore.ui.screens.StoreAppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RegisterAccountViewModel(
    private val accountService: AccountService
): StoreAppViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    fun updateEmail(newEmail: String) {
        this.email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        this.password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassWord: String) {
        this.confirmPassword.value = newConfirmPassWord
    }


    fun onSignUpClick(openAndPopUp: (StoreDestinations, StoreDestinations) -> Unit) {
        launchCatching {
            if (email.value.isEmpty() || password.value.isEmpty()) {
                _registerUiState.update {
                    it.copy(
                        message = "Input your Information",
                    )
                }
            } else {
                if (password.value == confirmPassword.value) {
                    if (password.value.length < 6) {
                        _registerUiState.update {
                            it.copy(
                                message = "password must not be less than 6 characters",
                            )
                        }
                    } else {
                        runCatching {
                            accountService.signUp(email.value, password.value)
                            openAndPopUp(Profile, Register)
                        }.onFailure { fail ->
                            _registerUiState.update {
                                it.copy(
                                    message = fail.message,
                                )
                            }
                        }
                    }
                } else {
                    _registerUiState.update {
                        it.copy(
                            message = "Password does not match",
                        )
                    }
                }
            }
        }
    }

    fun onSignInClick(openAndPopUp: (StoreDestinations, StoreDestinations) -> Unit) {
        openAndPopUp(Login, Register)
    }
}