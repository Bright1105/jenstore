package com.example.jenstore.ui.screens.common

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.Login
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.ui.screens.StoreAppMessage
import com.example.jenstore.ui.screens.profile.loginAccount.LoginViewModel
import com.example.jenstore.ui.theme.Rose1
import com.example.jenstore.ui.theme.Rose7


@Composable
fun LoginAndRegisterCard(
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    confirmPassword: String = "",
    onConfirmPassword: (String) -> Unit = {},
    isRegister: Boolean,
    doNotHaveOrHaveAccount: String,
    signInOrUpText: String,
    createOrLoginText: String,
    signUpWithEmailText: String,
    loginOrSignUpText: String,
    loginOrCreateAccountButton: () -> Unit,
    signUpClicked: () -> Unit,
    signIn: Boolean,
    messages: String? = null,
    currentRoute: StoreDestinations
) {

    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_20)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dp_10), dimensionResource(R.dimen.dp_10))

    ) {
        ConstraintLayout(
            modifier = Modifier

        ) {
            val (text1, text2, message, text2Spacer, textField, text3, text3Spacer,loginButton, text4Spacer, text4) = createRefs()
            createVerticalChain(text1, text2, message, text2Spacer,textField, text3, text3Spacer, loginButton, text4Spacer, text4, chainStyle = ChainStyle.Packed)
            Text(
                text = createOrLoginText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.dp_10),
                        top = dimensionResource(R.dimen.dp_15),
                        bottom = dimensionResource(R.dimen.dp_20)
                    )
                    .constrainAs(text1) {
                        top.linkTo(parent.top, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                    }
            )
            Text(
                text = signUpWithEmailText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(text2) {
                        linkTo(
                            start = parent.start,
                            startMargin = 16.dp,
                            endMargin = 16.dp,
                            end = parent.end,
                            bias = 0f
                        )
                    }
            )
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_10))
                    .constrainAs(text2Spacer) {
                        linkTo(top = text2.bottom, bottom = textField.top)
                    }
            )
            if (messages != null) {
                Text(
                    text = messages,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .constrainAs(message) {
                            linkTo(
                                start = parent.start,
                                startMargin = 16.dp,
                                endMargin = 16.dp,
                                end = parent.end,
                                bias = 0f
                            )
                        }
                )
            }
            LoginAndRegisterTextFields(
                currentRoute = currentRoute,
                email = email,
                onEmailChanged = onEmailChanged,
                password = password,
                onPasswordChanged = onPasswordChanged,
                confirmPassword = confirmPassword,
                onConfirmPassword = onConfirmPassword,
                isRegister = isRegister,
                modifier = Modifier
                    .padding(
                        bottom = dimensionResource(R.dimen.dp_16)
                    )
                    .constrainAs(textField) {
                        linkTo(
                            start = parent.start,
                            startMargin = 16.dp,
                            endMargin = 16.dp,
                            end = parent.end,
                            bias = 0f
                        )
                    }
            )
            if (signIn) {
                Text(
                    text = stringResource(R.string.forget),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = dimensionResource(R.dimen.dp_35)
                        )
                        .constrainAs(text3) {
                            linkTo(
                                start = parent.start,
                                startMargin = 16.dp,
                                endMargin = 16.dp,
                                end = parent.end,
                                bias = 0f
                            )
                        }
                )
            }
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_15))
                    .constrainAs(text3Spacer) {
                        linkTo(top = text3.bottom, bottom = loginButton.top)
                    }
            )
            Button(
                onClick = {
                    loginOrCreateAccountButton()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.dp_40))
                    .padding(end = dimensionResource(R.dimen.dp_35))
                    .constrainAs(loginButton) {
                        linkTo(
                            start = parent.start,
                            startMargin = 16.dp,
                            endMargin = 16.dp,
                            end = parent.end,
                            bias = 0f
                        )

                    }
            ) {
                Text(
                    text = loginOrSignUpText,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_5))
                    .constrainAs(text4Spacer) {
                        linkTo(top = loginButton.bottom, bottom = text4.top)
                    }
            )
            LoginAndRegisterText(
                doNotHaveOrHaveAccount = doNotHaveOrHaveAccount,
                signInOrUpText = signInOrUpText,
                signUpClicked = signUpClicked,
                modifier = Modifier
                    .constrainAs(text4) {
                        linkTo(
                            start = parent.start,
                            startMargin = 16.dp,
                            endMargin = 16.dp,
                            end = parent.end,
                            bias = 0f
                        )
                    }
            )
        }
    }
}

@Composable
fun LoginAndRegisterText(
    doNotHaveOrHaveAccount: String,
    signInOrUpText: String,
    signUpClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = doNotHaveOrHaveAccount,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .alignByBaseline()
        )
        TextButton(
            onClick = signUpClicked,
            modifier = Modifier
                .alignByBaseline()
        ) {
            Text(
                text = signInOrUpText,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}


@Composable
fun LoginAndRegisterTextFields(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    confirmPassword: String,
    onConfirmPassword: (String) -> Unit,
    currentRoute: StoreDestinations,
    isRegister: Boolean,
) {

    var visual by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {
        LoginAndRegisterTextField(
            value = email,
            onValueChanged = { newEmail ->
                onEmailChanged(newEmail)
            },
            label = { Text(text = stringResource(R.string.email)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(R.string.email),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.End)
                )
            }
        )
        Spacer(modifier = Modifier
            .height(dimensionResource(R.dimen.dp_16)))
        LoginAndRegisterTextField(
            value = password,
            onValueChanged = { newPassword ->
                onPasswordChanged(newPassword)
            },
            label = { Text(text = stringResource(R.string.password)) },
            trailingIcon = {
                Icon(
                    imageVector = if (visual) Icons.Outlined.RemoveRedEye else Icons.Filled.RemoveRedEye,
                    contentDescription = stringResource(R.string.password),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            visual = !visual
                        }
                )
            },
            visualTransformation = if (visual) VisualTransformation.None else PasswordVisualTransformation()
        )
        if (isRegister) {
            Spacer(modifier = Modifier
                .height(dimensionResource(R.dimen.dp_16)))
            LoginAndRegisterTextField(
                value = confirmPassword,
                onValueChanged = { newPassword ->
                    onConfirmPassword(newPassword)
                },
                label = { Text(text = stringResource(R.string.confirmPassword)) },
                trailingIcon = {
                    Icon(
                        imageVector = if (visual) Icons.Outlined.RemoveRedEye else Icons.Filled.RemoveRedEye,
                        contentDescription = stringResource(R.string.password),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                visual = !visual
                            }
                    )
                },
                visualTransformation = if (visual) VisualTransformation.None else PasswordVisualTransformation()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAndRegisterTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    label: @Composable () -> Unit,
    trailingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        singleLine = true,
        label = label,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.dp_60))
            .padding(end = dimensionResource(R.dimen.dp_29))
        ,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
        ),
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation
    )
}