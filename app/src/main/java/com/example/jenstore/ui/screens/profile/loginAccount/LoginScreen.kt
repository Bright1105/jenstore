package com.example.jenstore.ui.screens.profile.loginAccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.ui.screens.common.LoginAndRegisterCard
import com.example.jenstore.ui.screens.common.LoginAndRegisterTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    openAndPopUp: (StoreDestinations, StoreDestinations) -> Unit,
    currentRoute: StoreDestinations,
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val email = loginViewModel.email.collectAsState()
    val password = loginViewModel.password.collectAsState()

    val scope = rememberCoroutineScope()
    val uiState = loginViewModel.loginUiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
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
                    loginViewModel.onSignUpClick(openAndPopUp)
                },
                email = email.value,
                onEmailChanged = { newEmail ->
                    loginViewModel.updateEmail(newEmail)
                },
                password = password.value,
                onPasswordChanged = { newPassword ->
                    loginViewModel.updatePassword(newPassword)
                },
                doNotHaveOrHaveAccount = stringResource(R.string.doNotAccount),
                signInOrUpText = stringResource(R.string.signUp),
                createOrLoginText = stringResource(R.string.loginYourAccount),
                signUpWithEmailText = stringResource(R.string.signIn),
                loginOrSignUpText = stringResource(R.string.login),
                loginOrCreateAccountButton = {
                    loginViewModel.onSignInClick(openAndPopUp)
                },
                signIn = true,
                isRegister = false,
                messages = if (uiState.value.message != null) uiState.value.message else null,
                currentRoute = currentRoute
            )
        }
    }

}

