package com.example.jenstore.ui.screens.home

import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.example.jenstore.data.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class UiState(
    val isShowingHomePage: Boolean = true,
    val item: Flow<PagingData<Item>> = flowOf(),
    var isRefreshing: Boolean = false
)