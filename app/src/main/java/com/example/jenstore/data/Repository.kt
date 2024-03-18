package com.example.jenstore.data

import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.network.StoreApiService

interface Repository {

    suspend fun getItems(): List<ProductItem>

    suspend fun getItemById(id: Int): ProductItem
}

class RepositoryImpl(private val storeApiService: StoreApiService) : Repository {

    override suspend fun getItems(): List<ProductItem> = storeApiService.getItems()

    override suspend fun getItemById(id: Int): ProductItem = storeApiService.getItemById(id)
}