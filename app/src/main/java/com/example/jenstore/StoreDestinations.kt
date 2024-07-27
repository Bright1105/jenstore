package com.example.jenstore

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Downloading
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

interface StoreDestinations {
    val unSelectedIcon: ImageVector
    val selectedIcon: ImageVector
    val route: String
}

object Home : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.Home
    override val selectedIcon: ImageVector = Icons.Filled.Home
    override val route: String = "home"
}

object Splash : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.Downloading
    override val selectedIcon: ImageVector = Icons.Filled.Downloading
    override val route: String = "splash"
}

object Search : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.Search
    override val selectedIcon: ImageVector = Icons.Filled.Search
    override val route: String = "Search"
}

object Feed : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.PlayArrow
    override val selectedIcon: ImageVector = Icons.Filled.PlayArrow
    override val route: String = "feed"
}

object Profile : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.AccountCircle
    override val selectedIcon: ImageVector = Icons.Filled.AccountCircle
    override val route: String = "profile"
}

object ProfileAccount : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.AccountCircle
    override val selectedIcon: ImageVector = Icons.Filled.AccountCircle
    override val route: String = "profileAccount"
}

object Settings : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.Settings
    override val selectedIcon: ImageVector = Icons.Filled.Settings
    override val route: String = "settings"
}

object MyCart : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.ShoppingCart
    override val selectedIcon: ImageVector = Icons.Filled.ShoppingCart
    override val route: String = "myCart"
}


object Login : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.AccountCircle
    override val selectedIcon: ImageVector = Icons.Filled.AccountCircle
    override val route: String = "login"
}

object Register : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.AccountCircle
    override val selectedIcon: ImageVector = Icons.Filled.AccountCircle
    override val route: String = "register"
}

object ForgetPassword : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.AccountCircle
    override val selectedIcon: ImageVector = Icons.Filled.AccountCircle
    override val route: String = "forget"
}

object ProductDetails : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.Info
    override val selectedIcon: ImageVector = Icons.Filled.Info
    override val route: String = "item_details"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object Orders : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.Check
    override val selectedIcon: ImageVector = Icons.Filled.Check
    override val route: String = "orders"
}


object Notifications : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.Notifications
    override val selectedIcon: ImageVector = Icons.Filled.Notifications
    override val route: String = "Notification"
}

object SaveItems : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.Favorite
    override val selectedIcon: ImageVector = Icons.Filled.Favorite
    override val route: String = "saveItems"
}

object Promotions : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.DateRange
    override val selectedIcon: ImageVector = Icons.Filled.DateRange
    override val route: String = "promotion"
}

object Account : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.AccountCircle
    override val selectedIcon: ImageVector = Icons.Filled.AccountCircle
    override val route: String = "account"
}

object Address : StoreDestinations {
    override val unSelectedIcon: ImageVector = Icons.Outlined.LocationOn
    override val selectedIcon: ImageVector = Icons.Filled.LocationOn

    override val route: String = "address"
}

val storeTabRowBottomBar = listOf(Home, Search, Feed, Profile, Settings)