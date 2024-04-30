package com.example.jenstore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jenstore.ui.screens.cart.CartScreen
import com.example.jenstore.ui.screens.feed.FeedScreen
import com.example.jenstore.ui.screens.feed.FeedViewModel
import com.example.jenstore.ui.screens.home.HomeScreen
import com.example.jenstore.ui.screens.home.HomeUiState
import com.example.jenstore.ui.screens.home.HomeViewModel
import com.example.jenstore.ui.screens.productDetails.ProductDetailsScreen
import com.example.jenstore.ui.screens.productDetails.ProductDetailsUiState
import com.example.jenstore.ui.screens.productDetails.ProductDetailsViewModel
import com.example.jenstore.ui.screens.profile.ProfileScreen
import com.example.jenstore.ui.screens.profile.account.AccountScreen
import com.example.jenstore.ui.screens.profile.account.address.AddressScreen
import com.example.jenstore.ui.screens.profile.account.inbox.InboxScreen
import com.example.jenstore.ui.screens.profile.account.notification.NotificationsScreen
import com.example.jenstore.ui.screens.profile.account.orders.OrdersScreen
import com.example.jenstore.ui.screens.profile.account.promotion.PromotionScreen
import com.example.jenstore.ui.screens.profile.account.saveitems.SaveItemsScreen
import com.example.jenstore.ui.screens.search.SearchScreen
import com.example.jenstore.ui.screens.search.SearchViewModel
import com.example.jenstore.ui.screens.setting.SettingScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@Composable
fun StoreNavHost(
    navController: NavHostController,
    route: StoreDestinations,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    homeViewModel: HomeViewModel,
    productDetailsViewModel: ProductDetailsViewModel,
    feedViewModel: FeedViewModel,
    modifier: Modifier = Modifier
) {
    val productUiState: ProductDetailsUiState by productDetailsViewModel.uiState.collectAsState()
    val scope: CoroutineScope = rememberCoroutineScope()


    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                homeUiState = homeViewModel.homeUiState,
                viewModel = homeViewModel,
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                onCartClicked = {
                    navController.navigateSingleTopTo(MyCart.route)
                },
                onItemClicked = {
                    scope.launch {
                        productDetailsViewModel.productItemById(it)
                        navController.navigate("${ProductDetails.route}/$it")
                    }
                },
                navigateToCart = {
                    navController.navigateSingleTopTo(it.route)
                },
                navigateToSearch = {
                    navController.navigateSingleTopTo(it.route)
                },
            )
        }
        composable(route = Search.route) {
            SearchScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                navigateToCart = {
                    navController.navigateSingleTopTo(it.route)
                },
                navigateToSearch = {
                    navController.navigateSingleTopTo(it.route)
                },
                onItemClicked = {
                    scope.launch {
                        productDetailsViewModel.productItemById(it)
                        navController.navigate("${ProductDetails.route}/$it")
                    }
                }
            )
        }
        composable(route = MyCart.route) {
            CartScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
            )
        }
        composable(route = Profile.route) {
            ProfileScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                onCartClicked = {
                    navController.navigateSingleTopTo(it.route)
                },
                onSearchClicked = {
                    navController.navigateSingleTopTo(it.route)
                },
                onOrderClicked = {
                    navController.navigateSingleTopTo(it.route)
                },
                onInboxClicked = {
                    navController.navigateSingleTopTo(it.route)
                },
                onNotifyClicked = {
                    navController.navigateSingleTopTo(it.route)
                },
                onSavedItemsClicked = {
                    navController.navigateSingleTopTo(it.route)
                },
                onPromotionClicked = {
                    navController.navigateSingleTopTo(it.route)
                },
                onAccountClicked = {
                    navController.navigateSingleTopTo(it.route)
                },
                onAddressClicked = {
                    navController.navigateSingleTopTo(it.route)
                }
            )
        }
        composable(route = ProfileAccount.route) {

        }
        composable(route = Settings.route) {
            SettingScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route
            )
        }
        composable(route = Feed.route) {
            FeedScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                feedUiState = feedViewModel.feedUiState,
                viewModel = feedViewModel
            )
        }
        composable(route = Login.route) {

        }
        composable(route = Register.route) {

        }
        composable(route = ForgetPassword.route) {

        }
        composable(
            route = ProductDetails.routeWithArgs,
            arguments = listOf(navArgument(ProductDetails.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ProductDetailsScreen(
                item = productUiState.currentProduct,
                route = route,
                onBackClicked = {

                },
                onCartClicked = {
                    navController.navigateSingleTopTo(MyCart.route)
                },
            )
        }
        composable(route = Orders.route) {
            OrdersScreen()
        }
        composable(route = Inbox.route) {
            InboxScreen()
        }
        composable(route = Notifications.route) {
            NotificationsScreen()
        }
        composable(route = SaveItems.route) {
            SaveItemsScreen()
        }
        composable(route = Promotions.route) {
            PromotionScreen()
        }
        composable(route = Account.route) {
            AccountScreen()
        }
        composable(route = Address.route) {
            AddressScreen()
        }
    }
}


fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {

        popUpTo(
          //  this@navigateSingleTopTo.graph.findStartDestination().id
            this@navigateSingleTopTo.currentBackStackEntry?.destination?.route.orEmpty()
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }