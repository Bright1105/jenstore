package com.example.jenstore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jenstore.ui.screens.cart.CartScreen
import com.example.jenstore.ui.screens.cart.CartViewModel
import com.example.jenstore.ui.screens.feed.FeedScreen
import com.example.jenstore.ui.screens.feed.FeedViewModel
import com.example.jenstore.ui.screens.home.HomeScreen

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
import com.example.jenstore.ui.screens.profile.createAccount.RegisterAccountViewModel
import com.example.jenstore.ui.screens.profile.createAccount.RegisterScreen
import com.example.jenstore.ui.screens.profile.loginAccount.LoginScreen
import com.example.jenstore.ui.screens.profile.loginAccount.LoginViewModel
import com.example.jenstore.ui.screens.search.SearchScreen
import com.example.jenstore.ui.screens.setting.SettingScreen
import com.example.jenstore.ui.screens.splash.SplashScreen




@Composable
fun StoreGraph(
    appState: StoreAppState,
    route: StoreDestinations,
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    homeViewModel: HomeViewModel,
    productDetailsViewModel: ProductDetailsViewModel,
    feedViewModel: FeedViewModel,
    loginViewModel: LoginViewModel,
    registerAccountViewModel: RegisterAccountViewModel,
    cartViewModel: CartViewModel,
) {

    val productUiState: ProductDetailsUiState by productDetailsViewModel.uiState.collectAsState()

    NavHost(
        navController = appState.navController,
        startDestination = Home.route
    ) {
        composable(Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                onCartClicked = {
                    appState.navigate(it.route)
                },
                onItemClicked = {
                    productDetailsViewModel.productItemById(it)
                    appState.navigate("${ProductDetails.route}/$it")
                },
                navigateToCart = {
                    appState.navigate(it.route)
                },
                navigateToSearch = {
                    appState.navigate(it.route)
                }
            )
        }

        composable(Splash.route) {
            SplashScreen(
                openAndPopUp = { route , popUp ->
                    appState.navigateAndPopUp(route.route, popUp.route)
                }
            )
        }

        composable(Search.route) {
            SearchScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                navigateToCart = {
                    appState.navigate(it.route)
                },
                navigateToSearch = {
                    appState.navigate(it.route)
                },
                onItemClicked = {
                    productDetailsViewModel.productItemById(it)
                    appState.navigate("${ProductDetails.route}/$it")
                },
                homeViewModel = homeViewModel
            )
        }
        composable(MyCart.route) {
            CartScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                onItemClicked = {
                    productDetailsViewModel.productItemById(it.toString())
                    appState.navigate("${ProductDetails.route}/$it")
                },
                navigateBack = {
                    appState.popUp()
                },
                cartViewModel = cartViewModel
            )
        }
        composable(Register.route) {
            RegisterScreen(
                openAndPopUp = { route, popUp ->
                    appState.navigateAndPopUp(route.route, popUp.route)
                },
                currentRoute = route
            )
        }

        composable(Login.route) {
            LoginScreen(
                openAndPopUp = { route, popUp ->
                    appState.navigateAndPopUp(route.route, popUp.route)
                },
                currentRoute = route
            )
        }
        composable(Profile.route) {
            ProfileScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                onCartClicked = {
                    appState.navigate(it.route)
                },
                onSearchClicked = {
                    appState.navigate(it.route)
                },
                onOrderClicked = {
                    appState.navigate(it.route)
                },
                onInboxClicked = {
                    appState.navigate(it.route)
                },
                onNotifyClicked = {
                    appState.navigate(it.route)
                },
                onSavedItemsClicked = {
                    appState.navigate(it.route)
                },
                onPromotionClicked = {
                    appState.navigate(it.route)
                },
                onAccountClicked = {
                    appState.navigate("${Account.route}?$STORE_ID=$STORE_DEFAULT_ID")
//                    $NOTE_SCREEN?$NOTE_ID=$NOTE_DEFAULT_ID"
                },
                onAddressClicked = {
                    appState.navigate(it.route)
                },
                openAndPopup = { route, popUp ->
                    appState.navigateAndPopUp(route.route, popUp.route)
                },
                restartApp = { route ->
                    appState.clearAndNavigate(route.route)
                }
            )
        }
        composable(Settings.route) {
            SettingScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route
            )
        }
        composable(Feed.route) {
            FeedScreen(
                allScreen = allScreen,
                onTabClicked = onTabClicked,
                currentScreen = route,
                viewModel = feedViewModel
            )
        }
        composable(
            route = ProductDetails.routeWithArgs,
            arguments = listOf(navArgument(ProductDetails.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            ProductDetailsScreen(
                productDetails = productDetailsViewModel.productDetails,
                route = route,
                onBackClicked = {
                    appState.popUp()
                },
                onCartClicked = {
                    appState.navigate(MyCart.route)
                },
                onSearchClicked = {
                    appState.navigate(it.route)
                },
                navigateToHome = {
                    appState.popUp()
                },
                productDetailsViewModel = productDetailsViewModel
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
            SaveItemsScreen(
                onSearchClicked = {
                    appState.navigate(it.route)
                },
                onCartClicked = {
                    appState.navigate(it.route)
                },
                popUp = {
                    appState.popUp()
                },
                navigateToHome = {
                    appState.navigate(it.route)
                },
                onSavedItemClicked = {
                    productDetailsViewModel.productItemById(it)
                    appState.navigate("${ProductDetails.route}/$it")
                }
            )
        }
        composable(route = Promotions.route) {
            PromotionScreen()
        }
        composable(
            route = "${Account.route}$STORE_ID_ARG",
            arguments = listOf(navArgument(STORE_ID) { defaultValue = STORE_DEFAULT_ID})
        ) {
            AccountScreen(
                onBackClick = {
                    appState.popUp()
                },
                popUpScreen = {
                    appState.popUp()
                },
                restartApp = { route ->
                    appState.clearAndNavigate(route.route)
                },
            )
        }
        composable(route = Address.route) {
            AddressScreen(
                popUpScreen = {
                    appState.popUp()
                },
                restartApp = { route ->
                    appState.clearAndNavigate(route.route)
                },
                onSearchClicked = { route ->
                    appState.navigate(route.route)
                },
                onCartClicked = {
                    appState.navigate(it.route)
                }
            )
        }

    }
}

//@Composable
//fun StoreNavHost(
//    navController: NavHostController,
//    route: StoreDestinations,
//    allScreen: List<StoreDestinations>,
//    onTabClicked: (StoreDestinations) -> Unit,
//    homeViewModel: HomeViewModel,
//    productDetailsViewModel: ProductDetailsViewModel,
//    feedViewModel: FeedViewModel,
//    loginViewModel: LoginViewModel,
//    registerAccountViewModel: RegisterAccountViewModel,
//    cartViewModel: CartViewModel,
//    modifier: Modifier = Modifier
//) {
//    val productUiState: ProductDetailsUiState by productDetailsViewModel.uiState.collectAsState()
//    val scope: CoroutineScope = rememberCoroutineScope()
//
//
//    NavHost(
//        navController = navController,
//        startDestination = Home.route,
//        modifier = modifier
//    ) {
//        composable(route = Home.route) {
//            HomeScreen(
//                viewModel = homeViewModel,
//                allScreen = allScreen,
//                onTabClicked = onTabClicked,
//                currentScreen = route,
//                onCartClicked = {
//                    navController.navigateSingleTopTo(MyCart.route)
//                },
//                onItemClicked = {
//                    scope.launch {
//                        productDetailsViewModel.productItemById(it)
//                        navController.navigate("${ProductDetails.route}/$it")
//                    }
//                },
//                navigateToCart = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                navigateToSearch = {
//                    navController.navigateSingleTopTo(it.route)
//                }
//            )
//        }
//        composable(route = Search.route) {
//            SearchScreen(
//                allScreen = allScreen,
//                onTabClicked = onTabClicked,
//                currentScreen = route,
//                navigateToCart = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                navigateToSearch = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onItemClicked = {
//                    scope.launch {
//                        productDetailsViewModel.productItemById(it)
//                        navController.navigate("${ProductDetails.route}/$it")
//                    }
//                },
//                homeViewModel = homeViewModel
//            )
//        }
//        composable(route = MyCart.route) {
//            CartScreen(
//                allScreen = allScreen,
//                onTabClicked = onTabClicked,
//                currentScreen = route,
//                onItemClicked = {
//                    scope.launch {
//                        productDetailsViewModel.productItemById(it.toString())
//                        navController.navigate("${ProductDetails.route}/$it")
//                    }
//                },
//                cartViewModel = cartViewModel
//            )
//        }
//        composable(route = Profile.route) {
//            ProfileScreen(
//                allScreen = allScreen,
//                onTabClicked = onTabClicked,
//                currentScreen = route,
//                onCartClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onSearchClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onOrderClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onInboxClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onNotifyClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onSavedItemsClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onPromotionClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onAccountClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onAddressClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                onLoginClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                }
//            )
//        }
//        composable(route = ProfileAccount.route) {
//
//        }
//        composable(route = Settings.route) {
//            SettingScreen(
//                allScreen = allScreen,
//                onTabClicked = onTabClicked,
//                currentScreen = route
//            )
//        }
//        composable(route = Feed.route) {
//            FeedScreen(
//                allScreen = allScreen,
//                onTabClicked = onTabClicked,
//                currentScreen = route,
//                feedUiState = feedViewModel.feedUiState,
//                viewModel = feedViewModel
//            )
//        }
//        composable(route = Login.route) {
//            LoginScreen(
//                loginViewModel = loginViewModel
//            )
//        }
//        composable(route = Register.route) {
//            RegisterScreen(registerAccountViewModel = registerAccountViewModel)
//        }
//        composable(route = ForgetPassword.route) {
//
//        }
//        composable(
//            route = ProductDetails.routeWithArgs,
//            arguments = listOf(navArgument(ProductDetails.itemIdArg) {
//                type = NavType.StringType
//            })
//        ) {
//            ProductDetailsScreen(
//                item = productUiState.currentProduct,
//                route = route,
//                onBackClicked = {
//
//                },
//                onCartClicked = {
//                    navController.navigateSingleTopTo(MyCart.route)
//                },
//                onSearchClicked = {
//                    navController.navigateSingleTopTo(it.route)
//                },
//                productDetailsViewModel = productDetailsViewModel
//            )
//        }
//        composable(route = Orders.route) {
//            OrdersScreen()
//        }
//        composable(route = Inbox.route) {
//            InboxScreen()
//        }
//        composable(route = Notifications.route) {
//            NotificationsScreen()
//        }
//        composable(route = SaveItems.route) {
//            SaveItemsScreen()
//        }
//        composable(route = Promotions.route) {
//            PromotionScreen()
//        }
//        composable(route = Account.route) {
//            AccountScreen()
//        }
//        composable(route = Address.route) {
//            AddressScreen()
//        }
//    }
//}


//fun NavHostController.navigateSingleTopTo(route: String) =
//    this.navigate(route) {
//
//        popUpTo(
//          //  this@navigateSingleTopTo.graph.findStartDestination().id
//            this@navigateSingleTopTo.currentBackStackEntry?.destination?.route.orEmpty()
//        ) {
//            saveState = true
//        }
//        launchSingleTop = true
//        restoreState = true
//    }