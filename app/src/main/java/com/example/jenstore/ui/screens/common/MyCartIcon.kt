package com.example.jenstore.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.StoreApplication
import com.example.jenstore.StoreDestinations
import com.example.jenstore.ui.screens.cart.CartViewModel


@Composable
fun MyCartIcon(
    onCartClicked: (StoreDestinations) -> Unit,
    cartViewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val cartUiState = cartViewModel.cartUiState.collectAsState()

    val resource = LocalContext.current.resources
    val itemCount = remember(cartUiState.value.items.size, resource) {
        resource.getQuantityString(
            R.plurals.cart_order_count,
            cartUiState.value.items.size, cartUiState.value.items.size
        )
    }

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(
            onClick = { onCartClicked(MyCart) }
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = stringResource(R.string.cart),
                tint = MaterialTheme.colorScheme.primary
            )
        }
       if (cartUiState.value.items.isNotEmpty()) {
           Card(
               colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
               shape = MaterialTheme.shapes.extraLarge,
               elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.dp_5)),
               modifier = Modifier
                   .width(dimensionResource(R.dimen.dp_22))
                   .height(dimensionResource(R.dimen.dp_22))
                   .padding(
                       top = dimensionResource(R.dimen.dp_5),
                       end = dimensionResource(R.dimen.dp_5)
                   )
           ) {
               Text(
                   text = itemCount,
                   style = MaterialTheme.typography.bodyLarge,
                   textAlign = TextAlign.Center,
                   color = MaterialTheme.colorScheme.inverseOnSurface,
                   modifier = Modifier
                       .padding(
                           start = dimensionResource(R.dimen.dp_5),
                           end = dimensionResource(R.dimen.dp_5)
                       )
               )
           }
       }

    }
}