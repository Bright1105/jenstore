package com.example.jenstore.network

import com.example.jenstore.data.model.ProductItem
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url

interface StoreApiService {

    @GET("items/")
    suspend fun getItems(): List<ProductItem>

    @GET("products/{id}")
    suspend fun getItemById(@Path(value = "id", encoded = true) id: Int): ProductItem
}