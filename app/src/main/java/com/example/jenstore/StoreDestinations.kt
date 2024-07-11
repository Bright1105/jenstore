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
import androidx.compose.ui.graphics.vector.ImageVector

interface StoreDestinations {
    val icon: ImageVector
    val route: String
}

object Home : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Home
    override val route: String = "home"
}

object Splash : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Downloading
    override val route: String = "splash"
}
object HomeList : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Home
    override val route: String = "homeOne"
}

object Search : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Search
    override val route: String = "Search"
}

object Feed : StoreDestinations {
    override val icon: ImageVector = Icons.Default.PlayArrow
    override val route: String = "feed"
}

object Profile : StoreDestinations {
    override val icon: ImageVector = Icons.Default.AccountCircle
    override val route: String = "profile"
}

object ProfileAccount : StoreDestinations {
    override val icon: ImageVector = Icons.Default.AccountCircle
    override val route: String = "profileAccount"
}

object Settings : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Settings
    override val route: String = "settings"
}

object MyCart : StoreDestinations {
    override val icon: ImageVector = Icons.Default.ShoppingCart
    override val route: String = "myCart"
}

object ItemDetails : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Info
    override val route: String = "item_details"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object Login : StoreDestinations {
    override val icon: ImageVector = Icons.Default.AccountCircle
    override val route: String = "login"
}

object Register : StoreDestinations {
    override val icon: ImageVector = Icons.Default.AccountCircle
    override val route: String = "register"
}

object ForgetPassword : StoreDestinations {
    override val icon: ImageVector = Icons.Default.AccountCircle
    override val route: String = "forget"
}

object ProductDetails : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Info
    override val route: String = "item_details"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object Orders : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Check
    override val route: String = "orders"
}

object Inbox : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Person
    override val route: String = "Inbox"
}

object Notifications : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Notifications
    override val route: String = "Notification"
}

object SaveItems : StoreDestinations {
    override val icon: ImageVector = Icons.Default.Favorite
    override val route: String = "saveItems"
}

object Promotions : StoreDestinations {
    override val icon: ImageVector = Icons.Default.DateRange
    override val route: String = "promotion"
}

object Account : StoreDestinations {
    override val icon: ImageVector = Icons.Default.AccountCircle
    override val route: String = "account"
}

object Address : StoreDestinations {
    override val icon: ImageVector = Icons.Default.LocationOn
    override val route: String = "address"
}


val storeTabRowBottomBar = listOf(Home, Search, Feed, Profile, Settings)