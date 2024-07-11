package com.example.jenstore.ui.screens.home

import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.model.PaginationProducts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class UiState(
    val isShowingHomePage: Boolean = true,
    val typeOfProduct: String? = null,
    val item: Flow<PagingData<Item>>? = null,
    var isRefreshing: Boolean = false,
//    val productHair: List<Item> = listOf(),
//    val productShoe: List<Item> = listOf(),
//    val productClothe: List<Item> = listOf(),
//    val productBag: List<Item> = listOf()
)