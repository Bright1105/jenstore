package com.example.jenstore.data.repository

import androidx.paging.DiffingChangePayload
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.local.cart.OrdersDao
import com.example.jenstore.data.model.Feeds
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.model.PaginationProducts
import com.example.jenstore.data.paging.FeedPaging
import com.example.jenstore.data.paging.FeedPagingSource
import com.example.jenstore.data.paging.ProductsPagingSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


interface FirebaseRepository {

    suspend fun getProducts(): List<Item>

    fun getProductsPagination(): Flow<PagingData<Item>>

    suspend fun getProductId(id: String): Item

    suspend fun searchProduct(query: String): List<Item>

    fun getFeedPagination(): Flow<PagingData<Feeds>>

    suspend fun getFeed(): List<Feeds?>

    fun getPaging(): Flow<PagingData<Feeds>>

    fun searchProductPagination(): Flow<PagingData<Item>>
}

class FirebaseRepositoryImpl(
    private val db: FirebaseFirestore,
    private val source: FeedPagingSource,
    private val config: PagingConfig,
    private val productSource: ProductsPagingSource,
    private val productConfig: PagingConfig,
    private val searchSource: ProductsPagingSource,
    private val feedPaging: FeedPaging,
    private val feedConfig: PagingConfig
) : FirebaseRepository {


    override fun searchProductPagination(): Flow<PagingData<Item>>  {

        return Pager(
            config = productConfig
        ) {
           searchSource
        }.flow
    }

    override fun getProductsPagination(): Flow<PagingData<Item>> = Pager(
        config = productConfig
    ) {
        productSource
    }.flow

    override fun getFeedPagination() = Pager(
        config = config
    ) {
        source
    }.flow

    override fun getPaging(): Flow<PagingData<Feeds>> = Pager(
        config = feedConfig
    ) {
        feedPaging
    }.flow

    override suspend fun getProducts(): List<Item> {

        val source = Source.SERVER
        return Firebase.firestore.collection("products")
            .get(source)
            .await()
            .toObjects(Item::class.java)
    }

    override suspend fun getFeed(): List<Feeds?> {

        return Firebase.firestore
            .collection("feedUri")
            .get()
            .await()
            .toObjects(Feeds::class.java)
    }

    //    override suspend fun getProductShoe(): List<Item> {
//        val source = Source.SERVER
//        return withContext(Dispatchers.IO) {
//            db.collection("products")
//                .limit(6)
//                .whereEqualTo("itemType", "shoe")
//                .get(source)
//                .await()
//                .toObjects(Item::class.java)
//        }
//    }
//
//    override suspend fun getProductBag(): List<Item> {
//        val source = Source.SERVER
//        return withContext(Dispatchers.IO) {
//            db.collection("products")
//                .limit(6)
//                .whereEqualTo("itemType", "bag")
//                .get(source)
//                .await()
//                .toObjects(Item::class.java)
//        }
//    }
//
//    override suspend fun getProductClothe(): List<Item> {
//        val source = Source.SERVER
//        return withContext(Dispatchers.IO) {
//            db.collection("products")
//                .limit(6)
//                .whereEqualTo("itemType", "clothe")
//                .get(source)
//                .await()
//                .toObjects(Item::class.java)
//        }
//    }

    override suspend fun getProductId(id: String): Item {


        return withContext(Dispatchers.IO) {
            val source = Source.SERVER

            db.collection("products").document(id)
                .get(source)
                .await()
                .toObject(Item::class.java)!!
        }
    }


    override suspend fun searchProduct(query: String): List<Item> {

        val source = Source.SERVER

        return withContext(Dispatchers.IO) {
            db.collection("products").where(Filter.greaterThanOrEqualTo("name", query))
                .get(source)
                .await()
                .toObjects(Item::class.java)
        }
    }

}
