package com.example.jenstore.ui.screens.profile.account.address

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.R
import com.example.jenstore.Search
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.model.UserAddress
import com.example.jenstore.ui.screens.common.MyCartIcon


@Composable
fun AddressScreen(
    addressViewModel: AddressViewModel = viewModel(factory = AppViewModelProvider.Factory),
    restartApp: (StoreDestinations) -> Unit,
    popUpScreen: () -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit
) {

    LaunchedEffect(Unit) {
        addressViewModel.initialize(restartApp)
    }

    val addressUiState: AddressUiState by addressViewModel.addressUiState.collectAsState()
    val address = addressViewModel.address.collectAsState()
    val addressAdditionalInformation = addressViewModel.additionalInformation.collectAsState()
    val addressRegion = addressViewModel.region.collectAsState()
    val addressCity = addressViewModel.city.collectAsState()

    val userAddress = addressViewModel.userAddress.collectAsState(initial = null)

    var isRegionItemsVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val regionList = addressViewModel.regionList


    var isCityItemsVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val cityList = addressViewModel.cityList

    Scaffold(
        topBar = {
            AddressBookTopBar(
                onCartClicked = onCartClicked,
                onSearchClicked = onSearchClicked,
                onBackClicked = popUpScreen,
                editAddress = addressUiState.editAddress
            )
        },
        bottomBar = {
            AddressBottomBar(
                onClick = {
                    addressViewModel.editAddress()
                },
                onSaveClicked = {
                    addressViewModel.saveAddress(popUpScreen)
                },
                editAddress = addressUiState.editAddress || userAddress.value == null,
                isEmpty = address.value.isEmpty() || addressAdditionalInformation.value.isEmpty() || addressRegion.value.isEmpty() || addressCity.value.isEmpty()
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (addressUiState.editAddress || userAddress.value == null) {
                AddressTextField(
                    value = address.value,
                    onValueChanged = { address ->
                        addressViewModel.updateAddress(address)
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.address)
                        )
                    }
                )
                AddressTextField(
                    value = addressAdditionalInformation.value,
                    onValueChanged = { additionalInformation ->
                        addressViewModel.updateAdditionalInformation(additionalInformation)
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.addressAdditionInfo)
                        )
                    }
                )

                DropdownMenu(
                    expanded = isRegionItemsVisible,
                    onDismissRequest = {
                        isRegionItemsVisible = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()

                ) {
                    regionList.region.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            },
                            onClick = {
                                addressViewModel.updateRegion(it)
                                isRegionItemsVisible = false
                            },
                            modifier = Modifier
                        )
                    }
                }
                AddressTextField(
                    value = addressRegion.value,
                    onValueChanged = { region ->
                        addressViewModel.updateRegion(region)
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.region)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isRegionItemsVisible = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.list),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    readOnly = true,
                    modifier = Modifier

                )

                AddressTextField(
                    value = addressCity.value,
                    onValueChanged = { city ->
                        addressViewModel.updateCity(city)
                    },
                    label = {
                        Text(
                            text = stringResource(R.string.addressCity)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isCityItemsVisible = true
                            },
                            modifier = Modifier

                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.list),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    readOnly = true
                )
                DropdownMenu(
                    expanded = isCityItemsVisible,
                    onDismissRequest = {
                        isCityItemsVisible = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    cityList.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.city,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                addressViewModel.updateCity(it.city)
                                isCityItemsVisible = false
                            }
                        )
                    }
                }
            } else {
                AddressBook(
                    address = userAddress.value!!,
                    onAddressDeleteClicked = {

                    }
                )
            }
        }
    }
}

@Composable
private fun AddressTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    label: @Composable (() -> Unit),
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = label,
        trailingIcon = trailingIcon,
        readOnly = readOnly,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dp_10))
    )
}


@Composable
private fun AddressBook(
    address: UserAddress,
    onAddressDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Spacer(modifier = Modifier
            .height(dimensionResource(R.dimen.dp_20))
            .fillMaxWidth()
        )
        Card(
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_4)),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            address.address?.let {
                AddressBookInfo(
                    address = it,
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.dp_10))
                )
            }
            address.city?.let { AddressBookInfo(address = it) }
            address.region?.let { AddressBookInfo(address = it) }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.dp_30))
            )
            AddressButton(
                onClick = onAddressDeleteClicked,
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete),
                modifier = Modifier
                    .align(Alignment.End)
            )
        }
    }
}

@Composable
private fun AddressButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun AddressBookInfo(
    address: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = address,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Justify,
        modifier = modifier
            .padding(dimensionResource(R.dimen.dp_2))
            .padding(
                start = dimensionResource(R.dimen.dp_10)
            )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddressBookTopBar(
    onCartClicked: (StoreDestinations) -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit,
    onBackClicked: () -> Unit,
    editAddress: Boolean
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = if (editAddress) stringResource(R.string.addNewAddress) else  stringResource(R.string.address),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
        },
        actions = {
            IconButton(
                onClick = { onSearchClicked(Search) }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            MyCartIcon(
                onCartClicked = onCartClicked
            )
        }
    )
}

@Composable
private fun AddressBottomBar(
    onClick: () -> Unit,
    onSaveClicked: () -> Unit,
    editAddress: Boolean,
    isEmpty: Boolean,
) {
    if (editAddress) {
        Button(
            onClick = onSaveClicked,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(
                text = if (isEmpty) stringResource(R.string.cancel) else stringResource(R.string.save),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    } else {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.dp_10))
        ) {
            Text(
                text = stringResource(R.string.addAnotherAddress),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}