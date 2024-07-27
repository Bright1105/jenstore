package com.example.jenstore.ui.screens.profile.createAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.ui.screens.common.LoginAndRegisterCard



@Composable
fun RegisterScreen(
    openAndPopUp: (StoreDestinations, StoreDestinations) -> Unit,
    currentRoute: StoreDestinations,
    registerAccountViewModel: RegisterAccountViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val email = registerAccountViewModel.email.collectAsState()
    val password = registerAccountViewModel.password.collectAsState()
    val confirmPassword = registerAccountViewModel.confirmPassword.collectAsState()

    val uiState = registerAccountViewModel.registerUiState.collectAsState()

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.jenny),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20)))
                LoginAndRegisterCard(
                    signUpClicked = {
                        registerAccountViewModel.onSignInClick(openAndPopUp)
                    },
                    email = email.value,
                    onEmailChanged = { newEmail ->
                        registerAccountViewModel.updateEmail(newEmail)
                    },
                    password = password.value,
                    onPasswordChanged = { password ->
                        registerAccountViewModel.updatePassword(password)
                    },
                    doNotHaveOrHaveAccount = stringResource(R.string.haveAccount),
                    signInOrUpText = stringResource(R.string.signin),
                    createOrLoginText = stringResource(R.string.create),
                    signUpWithEmailText = stringResource(R.string.signUpWithEmail),
                    loginOrSignUpText = stringResource(R.string.signUp),
                    loginOrCreateAccountButton = {
                        registerAccountViewModel.onSignUpClick(openAndPopUp)
                    },
                    signIn = false,
                    isRegister = true,
                    confirmPassword = confirmPassword.value,
                    onConfirmPassword = { newPassword ->
                        registerAccountViewModel.updateConfirmPassword(newPassword)
                    },
                    messages = if (uiState.value.message != null) uiState.value.message else null,
                    currentRoute = currentRoute
                )
            }
        }
    )
}