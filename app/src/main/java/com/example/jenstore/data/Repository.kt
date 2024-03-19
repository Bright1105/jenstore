package com.example.jenstore.data

import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.network.StoreApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Repository {

    suspend fun getItems(): List<ProductItem>

    suspend fun getItemById(id: Int): ProductItem

    suspend fun searchQuery(searchQuery: String): List<ProductItem>
}

class RepositoryImpl(private val storeApiService: StoreApiService) : Repository {

    override suspend fun getItems(): List<ProductItem> = storeApiService.getItems()

    override suspend fun getItemById(id: Int): ProductItem = storeApiService.getItemById(id)

    override suspend fun searchQuery(searchQuery: String): List<ProductItem> = storeApiService.searchQuery(searchQuery)
}