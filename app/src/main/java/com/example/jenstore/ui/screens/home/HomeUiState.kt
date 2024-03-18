package com.example.jenstore.ui.screens.home

import com.example.jenstore.data.model.ProductItem

data class UiState(
    val isShowingHomePage: Boolean = true,
    val itemType: List<ProductItem> = emptyList()
)