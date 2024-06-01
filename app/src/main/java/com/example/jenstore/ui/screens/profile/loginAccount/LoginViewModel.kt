package com.example.jenstore.ui.screens.profile.loginAccount

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jenstore.data.Repository
import com.example.jenstore.data.model.User


/**
 * Types of UX events triggered by user actions.
 */

//sealed class LoginEvent(val severity: EventSeverity, val message: String) {
//
//}
//
//
///**
// * Severity of the event
// */
//
//enum class EventSeverity {
//    INFO, ERROR
//}
//
///**
// * Users can either create accounts or log in with an existing one.
// */
//enum class LoginAction {
//    LOGIN, CREATE_ACCOUNT
//}
//
///**
// * UI representation of the screen state
// */
//data class LoginState(
//    val action: LoginAction,
//    val username: String = "",
//    val email: String = "",
//    val password: String = "",
//    val firstName: String = "",
//    val lastName: String = "",
//    val isActive: Boolean = true,
//    val isStaff: Boolean = false,
//    val enable: Boolean = true
//) {
//    companion object {
//        /**
//         * Initial Ui State of the login screen
//         */
//        val initialState = LoginState(action = LoginAction.LOGIN)
//    }
//}

class LoginViewModel(
    private val repository: Repository
) : ViewModel() {

    val email = mutableStateOf("")
    val username = mutableStateOf("")
    val password = mutableStateOf("")
    val firstNames = mutableStateOf("")
    val lastNames = mutableStateOf("")
    val phoneNumber = mutableStateOf("")


    fun setEmail(email: String) {
        this.email.value = email
    }

    fun setUsername(username: String) {
        this.username.value = username
    }

    fun setPassword(password: String) {
       this.password.value = password
    }

    fun setFirstName(firstName: String) {
        this.firstNames.value = firstName
    }

    fun setLastName(lastName: String) {
       this.lastNames.value = lastName
    }

    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber.value = phoneNumber
    }

    //fun setIsActive(isActive: Boolean) {
    //        _state.value = state.value
    //    }



    fun createAccount(user: User) {

    }

    fun login(user: User, fromCreation: Boolean = false) {

    }
}
// initiaze the user through database to always the user
// anytime create another account or login another account, the old user in the database will be cleared and new will be store in db
