package com.example.jenstore.data.remote

import com.example.jenstore.data.model.ProductFeed
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.data.remote.feed.FeedDto
import com.example.jenstore.data.remote.homeProduct.ProductDto
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApiService {

    @GET("items/")
    suspend fun getItems(): List<ProductDto>

    @GET("products/feeds/")
    suspend fun getFeeds(): List<FeedDto>


    @GET("products/{id}")
    suspend fun getItemById(@Path(value = "id", encoded = true) id: Int): ProductDto

    @GET("products/items/{query}")
    suspend fun searchQuery(@Path("query") query: String?): List<ProductDto>
}

// @GET("items/")
//    suspend fun getProduct(
//        @Query("page") page: Int,
//        @Query("per_page") pageCount: Int
//    ): List<ProductDto>

// @GET("products/feeds/")
//    suspend fun getFeeds1(
//        @Query("page") page: Int,
//        @Query("per_page") pageCount: Int
//    ): List<FeedDto>