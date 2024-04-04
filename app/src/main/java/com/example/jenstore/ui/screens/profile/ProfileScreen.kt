package com.example.jenstore.ui.screens.profile

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jenstore.Account
import com.example.jenstore.Address
import com.example.jenstore.Inbox
import com.example.jenstore.MyCart
import com.example.jenstore.Notifications
import com.example.jenstore.Orders
import com.example.jenstore.Promotions
import com.example.jenstore.R
import com.example.jenstore.SaveItems
import com.example.jenstore.Search
import com.example.jenstore.StoreDestinations
import com.example.jenstore.ui.screens.common.StoreTabRow

@Composable
fun ProfileScreen(
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    onOrderClicked: (StoreDestinations) -> Unit,
    onInboxClicked: (StoreDestinations) -> Unit,
    onNotifyClicked: (StoreDestinations) -> Unit,
    onSavedItemsClicked: (StoreDestinations) -> Unit,
    onPromotionClicked: (StoreDestinations) -> Unit,
    onAccountClicked: (StoreDestinations) -> Unit,
    onAddressClicked: (StoreDestinations) -> Unit,
    onCartClicked: (StoreDestinations) -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(

        topBar = {
                 ProfileTopAppBar(
                     onCartClicked = onCartClicked,
                     onSearchClicked = onSearchClicked
                 )
        },
        bottomBar = {
            StoreTabRow(
                allScreensBar = allScreen,
                onTabSelected = onTabClicked,
                currentScreen = currentScreen
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileOrdersInfo(
                onOrderClicked = onOrderClicked,
                onInboxClicked = onInboxClicked,
                onNotifyClicked = onNotifyClicked,
                onSavedItemsClicked = onSavedItemsClicked,
                onPromotionClicked = onPromotionClicked
            )
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_15)))
            ProfileAccountSetting(
                onAccountClicked = onAccountClicked,
                onAddressClicked = onAddressClicked,
                onLogOutClicked = {}
            )
        }
    }
}


@Composable
private fun ProfileAccountSetting(
    onAccountClicked: (StoreDestinations) -> Unit,
    onAddressClicked: (StoreDestinations) -> Unit,
    onLogOutClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Text(
            text = stringResource(R.string.mySettings),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    bottom = dimensionResource(R.dimen.dp_5)
                )
        )
        Card(
            shape = ShapeDefaults.Small,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10))
        ) {
            ProfileAccountIconAndInfo(
                text = R.string.account,
                imageVector = Icons.Outlined.AccountCircle,
                description = R.string.account,
                onValueClicked = { onAccountClicked(Account) }
            )
            ProfileAccountIconAndInfo(
                text = R.string.address,
                imageVector = Icons.Outlined.LocationOn,
                description = R.string.address,
                onValueClicked = { onAddressClicked(Address) }
            )
        }
        TextButton(
            onClick = onLogOutClicked,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.logOut),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ProfileOrdersInfo(
    onOrderClicked: (StoreDestinations) -> Unit,
    onInboxClicked: (StoreDestinations) -> Unit,
    onNotifyClicked: (StoreDestinations) -> Unit,
    onSavedItemsClicked: (StoreDestinations) -> Unit,
    onPromotionClicked: (StoreDestinations) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.dp_10))
    ) {
        Text(
            text = stringResource(R.string.myAccount),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    top = dimensionResource(R.dimen.dp_15),
                    bottom = dimensionResource(R.dimen.dp_5)
                )
        )
        Card(
            shape = ShapeDefaults.Small,
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_10))
        ) {
            ProfileAccountIconAndInfo(
                text = R.string.orders,
                imageVector = Icons.AutoMirrored.Outlined.List,
                description = R.string.orders,
                onValueClicked = { onOrderClicked(Orders) },
            )
            ProfileAccountIconAndInfo(
                text = R.string.inbox,
                imageVector = Icons.Outlined.Email,
                description = R.string.inbox,
                onValueClicked = { onInboxClicked(Inbox) }
            )
            ProfileAccountIconAndInfo(
                text = R.string.notification,
                imageVector = Icons.Outlined.Notifications,
                description = R.string.notification,
                onValueClicked = { onNotifyClicked(Notifications) }
            )
            ProfileAccountIconAndInfo(
                text = R.string.savedItem,
                imageVector = Icons.Outlined.FavoriteBorder,
                description = R.string.savedItem,
                onValueClicked = { onSavedItemsClicked(SaveItems) }
            )
            ProfileAccountIconAndInfo(
                text = R.string.promotion,
                imageVector = Icons.Outlined.DateRange,
                description = R.string.promotion,
                onValueClicked = { onPromotionClicked(Promotions) }
            )
        }
    }
}

@Composable
private fun ProfileAccountIconAndInfo(
    @StringRes text: Int,
    imageVector: ImageVector,
    @StringRes description: Int,
    onValueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onValueClicked() }
            .padding(10.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(description),
            modifier = modifier
                .padding(
                    start = dimensionResource(R.dimen.dp_10),
                    end = dimensionResource(R.dimen.dp_5)
                )
        )
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
                .weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowForward,
            contentDescription = stringResource(R.string.arrowForward),
            modifier = modifier
                .padding(end = dimensionResource(R.dimen.dp_10))
        )
    }
}


@Composable
private fun ProfileTopAppBar(
    onCartClicked: (StoreDestinations) -> Unit,
    onSearchClicked: (StoreDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(dimensionResource(R.dimen.dp_150))
            .background(MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = modifier
                .padding(
                    top = dimensionResource(R.dimen.dp_35)
                )
        ) {
            Text(
                text = stringResource(R.string.account),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .weight(1f)
                    .padding(
                        start = dimensionResource(R.dimen.dp_10)
                    )
            )
            IconButton(
                onClick = { onSearchClicked(Search) }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
            IconButton(
                onClick = { onCartClicked(MyCart) }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = stringResource(R.string.cart)
                )
            }
        }
        Spacer(modifier = modifier.height(dimensionResource(R.dimen.dp_3)))
        Text(
            text = "Welcome Daniel",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
                .padding(start = dimensionResource(R.dimen.dp_10))
        )
        Text(
            text = "Daniel11044@gmail.com",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium,
            modifier = modifier
                .padding(start = dimensionResource(R.dimen.dp_10))
        )
    }
}