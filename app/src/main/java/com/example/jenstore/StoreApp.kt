package com.example.jenstore

import androidx.compose.foundation.border
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jenstore.ui.screens.home.HomeViewModel
import com.example.jenstore.ui.screens.productDetails.ProductDetailsViewModel
import com.example.jenstore.ui.theme.JenstoreTheme


@Composable
fun StoreApp() {
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val productDetailsViewModel: ProductDetailsViewModel = viewModel(factory = ProductDetailsViewModel.factory)
    
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
            productDetailsViewModel = productDetailsViewModel
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopAppBar(
    screen: StoreDestinations,
    onCartClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.topBarName),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        },
        actions = {
            IconButton(
                onClick = onCartClicked
            ) {
                Icon(
                    imageVector = screen.icon,
                    contentDescription = screen.route,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
    )
}