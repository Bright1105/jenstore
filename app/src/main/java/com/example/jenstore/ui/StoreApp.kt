package com.example.jenstore.ui

import androidx.compose.material.Divider
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.Home
import com.example.jenstore.MyCart
import com.example.jenstore.R
import com.example.jenstore.StoreDestinations
import com.example.jenstore.StoreNavHost
import com.example.jenstore.navigateSingleTopTo
import com.example.jenstore.storeTabRowBottomBar
import com.example.jenstore.ui.screens.cart.JenStoreDivider
import com.example.jenstore.ui.screens.feed.FeedViewModel
import com.example.jenstore.ui.screens.home.HomeViewModel
import com.example.jenstore.ui.screens.productDetails.ProductDetailsViewModel
import com.example.jenstore.ui.screens.profile.createAccount.RegisterAccountViewModel
import com.example.jenstore.ui.screens.profile.loginAccount.LoginViewModel
import com.example.jenstore.ui.theme.JenstoreTheme


@Composable
fun StoreApp() {
    val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val productDetailsViewModel: ProductDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val registerAccountViewModel: RegisterAccountViewModel = viewModel(factory = AppViewModelProvider.Factory)
    
    JenstoreTheme {
        val navController = rememberNavController()
        
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = 
            storeTabRowBottomBar.find { it.route == currentDestination?.route } ?: Home
        
        StoreNavHost(
            navController = navController,
            route = currentScreen,
            allScreen = storeTabRowBottomBar,
            onTabClicked = {
                           navController.navigateSingleTopTo(it.route)
            },
            homeViewModel = homeViewModel,
            productDetailsViewModel = productDetailsViewModel,
            feedViewModel = feedViewModel,
            loginViewModel = loginViewModel,
            registerAccountViewModel = registerAccountViewModel
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopAppBar(
    modifier: Modifier = Modifier,
    screen: StoreDestinations,
    onCartClicked: (StoreDestinations) -> Unit,
    pressed: Boolean = false
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (pressed) {
                CircularProgressIndicator()
            }
        },
        title = {
            Text(
                text = stringResource(R.string.topBarName),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        actions = {
            IconButton(
                onClick = { onCartClicked(MyCart) }
            ) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = screen.route,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier
    )
}