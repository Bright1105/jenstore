package com.example.jenstore.data



import android.content.Context
import androidx.paging.PagingConfig
import com.example.jenstore.data.local.StoreDatabase
import com.example.jenstore.data.paging.FeedPagingSource
import com.example.jenstore.data.paging.ProductsPagingSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {

    val repository: Repository


    val storeDatabase: StoreDatabase

    val feedPagingSource: FeedPagingSource

    val productsPagingSource: ProductsPagingSource
}


class DefaultAppContainer(private val context: Context) : AppContainer {


    override val storeDatabase: StoreDatabase = StoreDatabase.getDatabase(context)


    private val db = Firebase.firestore


    private val provideQueryFeed = db
        .collection("feedUri")
       // .orderBy("id")
        .limit(1)

    private val pagingConfig = PagingConfig(
        pageSize = 1
    )

    override val feedPagingSource: FeedPagingSource by lazy {
        FeedPagingSource(queryFeed = provideQueryFeed)
    }

    private val provideProductQuery = db
        .collection("products")
        .limit(4)

    private val productPagingConfig = PagingConfig(
        pageSize = 4
    )

    override val productsPagingSource: ProductsPagingSource by lazy {
        ProductsPagingSource(queryProduct = provideProductQuery)
    }


    override val repository: Repository by lazy {
        RepositoryImpl(
            ordersDao = storeDatabase.ordersDao(),
            db = db,
            source = feedPagingSource,
            config = pagingConfig,
            productSource = productsPagingSource,
            productConfig = productPagingConfig
        )
    }
}