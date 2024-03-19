package com.example.jenstore.data



import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.network.StoreApiService
import com.example.jenstore.ui.screens.search.SearchMatchQuery
import com.example.jenstore.ui.screens.search.SearchMatchQueryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {

    val repository: Repository

    val searchMatchQuery: SearchMatchQuery
}


class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://86gnbdfj-8000.uks1.devtunnels.ms/"
    //https://bc95-129-205-124-176.ngrok-free.app/

    private val contentType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    //GsonConverterFactory.create()

    private val retrofitService: StoreApiService by lazy {
        retrofit.create(StoreApiService::class.java)
    }

    override val repository: Repository by lazy {
        RepositoryImpl(retrofitService)
    }

    override val searchMatchQuery: SearchMatchQuery by lazy {
        SearchMatchQueryImpl(item = ProductItem())
    }
}