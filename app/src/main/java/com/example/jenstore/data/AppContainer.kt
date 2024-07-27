package com.example.jenstore.data



import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.PagingConfig
import com.example.jenstore.data.local.StoreDatabase
import com.example.jenstore.data.paging.FeedPaging
import com.example.jenstore.data.paging.FeedPagingSource
import com.example.jenstore.data.paging.ProductsPagingSource
import com.example.jenstore.data.repository.FirebaseRepository
import com.example.jenstore.data.repository.FirebaseRepositoryImpl
import com.example.jenstore.data.repository.LocalRepository
import com.example.jenstore.data.repository.LocalRepositoryImpl
import com.example.jenstore.data.service.AccountService
import com.example.jenstore.data.service.AccountServiceImpl
import com.example.jenstore.data.service.StorageService
import com.example.jenstore.data.service.StorageServiceImpl
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

interface AppContainer {

    val firebaseRepository: FirebaseRepository

    val localRepository: LocalRepository

    val storeDatabase: StoreDatabase

    val feedPagingSource: FeedPagingSource

    val productsPagingSource: ProductsPagingSource

    val accountService: AccountService

    val storageService: StorageService


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

    private val feedPaging: FeedPaging by lazy {
        FeedPaging(db)
    }

    private val feedConfig = PagingConfig(
        pageSize = 1
    )

    private val provideProductQuery = db
        .collection("products")
        .limit(4)

    private val productPagingConfig = PagingConfig(
        pageSize = 4
    )

    override val productsPagingSource: ProductsPagingSource by lazy {
        ProductsPagingSource(queryProduct = provideProductQuery)
    }


    override val firebaseRepository: FirebaseRepository by lazy {
        FirebaseRepositoryImpl(
            db = db,
            source = feedPagingSource,
            config = pagingConfig,
            productSource = productsPagingSource,
            productConfig = productPagingConfig,
            searchSource = productsPagingSource,
            feedPaging = feedPaging,
            feedConfig = feedConfig
        )
    }

    override val localRepository: LocalRepository by lazy {
        LocalRepositoryImpl(
            ordersDao = storeDatabase.ordersDao(),
            checkoutDao = storeDatabase.checkoutDao()
        )
    }

    override val accountService: AccountService by lazy {
        AccountServiceImpl()
    }

    override val storageService: StorageService by lazy {
        StorageServiceImpl(
            accountService = accountService
        )
    }


}