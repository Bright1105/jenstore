package com.example.jenstore.ui.screens.profile.loginAccount

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.jenstore.R
import com.example.jenstore.ui.screens.common.LoginAndRegisterTextField


@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {

    Scaffold(
        content = {
            Column(modifier = Modifier.padding(it)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.25f)
                ) {
                    Text(
                        text = stringResource(R.string.app_name)
                    )
                }

                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .fillMaxWidth()
                ) {
                    Column {
                        LoginAndRegisterTextField(
                            value = loginViewModel.username.value,
                            onValueChanged = { username ->
                                loginViewModel.setUsername(username)
                            },
                            enabled = true,
                            label = { Text(text = stringResource(R.string.userName))}
                        )

                        LoginAndRegisterTextField(
                            value = loginViewModel.email.value,
                            onValueChanged = { email ->
                                 loginViewModel.setEmail(email)
                            },
                            enabled = true,
                            label = { Text(text = stringResource(R.string.email))}
                        )
                        LoginAndRegisterTextField(
                            value = loginViewModel.firstNames.value,
                            onValueChanged = { firstName ->
                                loginViewModel.setFirstName(firstName)
                            },
                            enabled = true,
                            label = { Text(text = stringResource(R.string.firstName))}
                        )
                        LoginAndRegisterTextField(
                            value = loginViewModel.lastNames.value,
                            onValueChanged = { lastName ->
                                loginViewModel.setLastName(lastName)
                            },
                            enabled = true,
                            label = { Text(text = stringResource(R.string.lastName))}
                        )
                        LoginAndRegisterTextField(
                            value = loginViewModel.phoneNumber.value,
                            onValueChanged = { phoneNumber ->
                                loginViewModel.setPhoneNumber(phoneNumber)
                            },
                            enabled = true,
                            label = { Text(text = stringResource(R.string.phoneNumber))}
                        )
                        LoginAndRegisterTextField(
                            value = loginViewModel.password.value,
                            onValueChanged = { password ->
                                loginViewModel.setPassword(password)
                            },
                            enabled = true,
                            label = { Text(text = stringResource(R.string.password))}
                        )
                    }
                }
            }
        }
    )
}


