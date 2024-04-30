package com.example.jenstore.ui.screens.home

import androidx.compose.runtime.State
import androidx.paging.PagingData
import com.example.jenstore.data.model.ProductItem

data class UiState(
    val isShowingHomePage: Boolean = true,
    val itemType: List<ProductItem> = emptyList(),
    var isRefreshing: Boolean = false
)