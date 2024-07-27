package com.example.jenstore.ui.screens.profile.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.Gender
import com.example.jenstore.ui.screens.common.MyCartIcon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    popUpScreen: () -> Unit,
    restartApp: (StoreDestinations) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val firstName = accountViewModel.firstName.collectAsState()
    val middleName = accountViewModel.middleName.collectAsState()
    val lastName = accountViewModel.lastName.collectAsState()
    val gender = accountViewModel.gender.collectAsState()
    val phoneNumber = accountViewModel.phoneNumber.collectAsState()

    val image = accountViewModel.image.collectAsState()

    val uiState = accountViewModel.uiState.collectAsState()

    val selectImage = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()
    ) {
        accountViewModel.image.value = it
        accountViewModel.saveImage(it)
    }

    val userInformation = accountViewModel.userInfo.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        accountViewModel.initialize(restartApp)
    }

    var isGenderMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }


    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current



    Scaffold(
        topBar = {
            AccountTopAppBar(
                onBackClick = onBackClick,
                isEditProfile = uiState.value.editProfile
            )
        },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(it)
                .padding(dimensionResource(R.dimen.dp_10))
                .onSizeChanged {
                    itemHeight = with(density) { it.height.toDp() }
                }
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.value.editProfile || userInformation.value == null) {
                AccountImage(
                    image = image.value.toString(),
                    onImageClicked = { selectImage.launch("image/*")},
                    enable = uiState.value.editProfile || userInformation.value == null
                )
            } else {
                userInformation.value?.image?.let { it1 ->
                    AccountImage(
                        image = it1,
                        enable = uiState.value.editProfile
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_15))
                    .fillMaxWidth()
            )
            ProfileTextField(
                value = firstName.value,
                onValueChanged = { firstName ->
                    accountViewModel.updateFirstName(firstName)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                label = {
                    if (uiState.value.editProfile || userInformation.value == null) {
                        Text(
                            text = stringResource(R.string.firstName),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        userInformation.value?.firstName?.let { it1 ->
                            Text(
                                text = it1
                            )
                        }
                    }
                },
                enable = uiState.value.editProfile || userInformation.value == null,
                readOnly = false
            )
            ProfileTextField(
                value = middleName.value,
                onValueChanged = { middleName ->
                    accountViewModel.updateMiddleName(middleName)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                label = {
                    if (uiState.value.editProfile || userInformation.value == null) {
                        Text(
                            text = stringResource(R.string.middleName),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        userInformation.value?.middleName?.let {  middleName ->
                            Text(
                                text = middleName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                },
                enable = uiState.value.editProfile || userInformation.value == null,
                readOnly = false
            )
            ProfileTextField(
                value = lastName.value,
                onValueChanged = { lastName ->
                    accountViewModel.updateLastName(lastName)
                },
                label = {
                    if (uiState.value.editProfile || userInformation.value == null) {
                        Text(
                            text = stringResource(R.string.lastName),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        userInformation.value?.lastName?.let {  lastName ->
                            Text(
                                text = lastName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                enable = uiState.value.editProfile || userInformation.value == null,
                readOnly = false
            )
            ExposedDropdownMenuBox(
                expanded = isGenderMenuVisible,
                onExpandedChange = {
                   //isGenderMenuVisible = !isGenderMenuVisible
                }
            ) {
                ProfileTextField(
                    value = gender.value,
                    onValueChanged = {  gender ->
                        accountViewModel.updateGender(gender)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isGenderMenuVisible = true
                            },
                            enabled = uiState.value.editProfile || userInformation.value == null,
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.list),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                    label = {
                        if (uiState.value.editProfile || userInformation.value == null) {
                            Text(
                                text = stringResource(R.string.gender),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            userInformation.value?.gender?.let {  gender ->
                                Text(
                                    text = gender,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    },
                    enable = uiState.value.editProfile || userInformation.value == null,
                    readOnly = true
                )
                ExposedDropdownMenu(
                    expanded = isGenderMenuVisible,
                    onDismissRequest = {
                        isGenderMenuVisible = false
                    }
                ) {
                    accountViewModel.items.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(text = it.gender)
                            },
                            onClick = {
                                accountViewModel.updateGender(it.gender)
                                isGenderMenuVisible = false
                            }
                        )
                    }
                }
            }
            ProfileTextField(
                value = phoneNumber.value,
                onValueChanged = { phoneNumber ->
                    accountViewModel.updatePhoneNumber(phoneNumber)
                },
                label = {
                   if (uiState.value.editProfile || userInformation.value == null) {
                       Text(
                           text = stringResource(R.string.phoneNumber),
                           style = MaterialTheme.typography.bodyLarge
                       )
                   } else {
                       userInformation.value?.phoneNumber?.let {  phoneNumber ->
                           Text(
                               text = phoneNumber,
                               style = MaterialTheme.typography.bodyLarge
                           )
                       }
                   }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                enable = uiState.value.editProfile || userInformation.value == null,
                readOnly = false
            )

            if (uiState.value.editProfile || userInformation.value == null) {
                Button(
                    onClick = { accountViewModel.saveUser(popUpScreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            dimensionResource(R.dimen.dp_10),
                            dimensionResource(R.dimen.dp_10)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Button(
                    onClick = { accountViewModel.notEditProfile() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            dimensionResource(R.dimen.dp_10),
                            dimensionResource(R.dimen.dp_10)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            } else  {
                TextButton(
                    onClick = { accountViewModel.editProfile() }
                ) {
                    Text(
                        text = stringResource(R.string.editProfile),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
        }
    }
}

@Composable
fun GenderItem(
    gender: String,
    items: List<Gender>,
    modifier: Modifier = Modifier,
    isGenderVisible: Boolean,
    onItemClicked: (Gender) -> Unit
) {

    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val density = LocalDensity.current

    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_4)),
        modifier = modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
    ) {

    }

}

@Composable
private fun AccountImage(
    modifier: Modifier = Modifier,
    image: String,
    onImageClicked: () -> Unit = {},
    enable: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
       Column {
           AsyncImage(
               model = ImageRequest.Builder(LocalContext.current)
                   .data(image)
                   .crossfade(true)
                   .build(),
               contentDescription = image.toString(),
               contentScale = ContentScale.Crop,
               error = painterResource(R.drawable.profileimage),
               placeholder = painterResource(R.drawable.profileimage),
               modifier = Modifier
                   .width(dimensionResource(R.dimen.dp_100))
                   .height(dimensionResource(R.dimen.dp_100))
                   .clip(CircleShape)
                   .clickable(
                       onClick = { onImageClicked() },
                       enabled = enable
                   )
           )
           if (enable) {
               Text(
                   text = stringResource(R.string.changePhoto),
                   modifier = Modifier
               )
           }
       }
    }
}


@Composable
fun ProfileTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    label: @Composable() (() -> Unit),
    trailingIcon: @Composable() (() -> Unit)? = null,
    readOnly: Boolean,
    keyboardOptions: KeyboardOptions,
    enable: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = label,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        enabled = enable,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.tertiary,
            disabledTrailingIconColor = Color.Blue
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                dimensionResource(R.dimen.dp_10),
                dimensionResource(R.dimen.dp_10)
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountTopAppBar(
    onBackClick: () -> Unit,
    isEditProfile: Boolean,
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        title = {
            Text(
                text = if (!isEditProfile) stringResource(R.string.accountDetails) else stringResource(R.string.editAccount),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
    )
}