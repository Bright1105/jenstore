package com.example.jenstore.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.jenstore.StoreDestinations
import com.example.jenstore.ui.screens.common.StoreTabRow

@Composable
fun ProfileScreen(
    allScreen: List<StoreDestinations>,
    onTabClicked: (StoreDestinations) -> Unit,
    currentScreen: StoreDestinations,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            StoreTabRow(
                allScreensBar = allScreen,
                onTabSelected = onTabClicked,
                currentScreen = currentScreen
            )
        }
    ) {
        Column(modifier = modifier.padding(it)) {
            Text(text = "Welcome to ProfileScreen")
        }
    }
}