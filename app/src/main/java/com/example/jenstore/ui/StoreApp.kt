package com.example.jenstore.ui


import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jenstore.AppViewModelProvider
import com.example.jenstore.Home
import com.example.jenstore.R
import com.example.jenstore.StoreAppState
import com.example.jenstore.StoreDestinations
import com.example.jenstore.StoreGraph
import com.example.jenstore.storeTabRowBottomBar
import com.example.jenstore.ui.screens.cart.CartViewModel
import com.example.jenstore.ui.screens.common.MyCartIcon
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
    val cartViewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory)

    JenstoreTheme {
        val appState = rememberAppState()
        
        val currentBackStack by appState.navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = 
            storeTabRowBottomBar.find { it.route == currentDestination?.route } ?: Home

        StoreGraph(
            appState = appState,
            route = currentScreen,
            allScreen = storeTabRowBottomBar,
            onTabClicked = {
                appState.navigate(it.route)
            },
            homeViewModel = homeViewModel,
            productDetailsViewModel = productDetailsViewModel,
            feedViewModel = feedViewModel,
            loginViewModel = loginViewModel,
            registerAccountViewModel = registerAccountViewModel,
            cartViewModel = cartViewModel
        )


//        StoreNavHost(
//            navController = navController,
//            route = currentScreen,
//            allScreen = storeTabRowBottomBar,
//            onTabClicked = {
//                           navController.navigateSingleTopTo(it.route)
//            },
//            homeViewModel = homeViewModel,
//            productDetailsViewModel = productDetailsViewModel,
//            feedViewModel = feedViewModel,
//            loginViewModel = loginViewModel,
//            registerAccountViewModel = registerAccountViewModel,
//            cartViewModel = cartViewModel
//        )
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        StoreAppState(navController)
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopAppBar(
    onCartClicked: (StoreDestinations) -> Unit,
) {
    CenterAlignedTopAppBar(
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
           MyCartIcon(
               onCartClicked = onCartClicked
           )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier
    )
}