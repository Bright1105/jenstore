package com.example.jenstore.data



import android.content.Context
import com.example.jenstore.data.local.StoreDatabase
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.data.remote.StoreApiService
import com.example.jenstore.ui.screens.search.SearchMatchQuery
import com.example.jenstore.ui.screens.search.SearchMatchQueryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {

    val repository: Repository

    val retrofit: Retrofit

    val storeDatabase: StoreDatabase
}


class DefaultAppContainer(private val context: Context) : AppContainer {

    private val baseUrl = "https://86gnbdfj-8000.uks1.devtunnels.ms/"
    override val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()


    override val storeDatabase: StoreDatabase = StoreDatabase.getDatabase(context)



    private val retrofitService: StoreApiService by lazy {
        retrofit.create(StoreApiService::class.java)
    }

    override val repository: Repository by lazy {
        RepositoryImpl(
            storeApiService = retrofitService,
            ordersDao = storeDatabase.ordersDao()
        )
    }
}