package com.example.jenstore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.local.cart.OrdersDao
import com.example.jenstore.data.model.Feeds
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.paging.FeedPagingSource
import com.example.jenstore.data.paging.ProductsPagingSource
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


interface Repository {

    // Room database

    suspend fun addToCart(ordersEntity: OrdersEntity)

    fun getAllItems(): Flow<List<OrdersEntity>>

    suspend fun delete(ordersEntity: OrdersEntity)

    suspend fun count(ordersEntity: OrdersEntity)


    // firebase
    suspend fun getProduct(): List<Item>

    fun getProductsPagination(): Flow<PagingData<Item>>

    suspend fun getProductId(id: String): Item

    suspend fun searchProduct(query: String): List<Item>


   // suspend fun getFeed(): List<Feeds>

    fun getFeedPagination(): Flow<PagingData<Feeds>>
}

class RepositoryImpl(
    private val ordersDao: OrdersDao,
    private val db: FirebaseFirestore,
    private val source: FeedPagingSource,
    private val config: PagingConfig,
    private val productSource: ProductsPagingSource,
    private val productConfig: PagingConfig
) : Repository {


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

    override suspend fun getProduct(): List<Item> {

        val source = Source.DEFAULT

        return withContext(Dispatchers.IO) {
            db.collection("products")
                .limit(10)
                .get(source)
                .await()
                .toObjects(Item::class.java)
        }


    }

    //override suspend fun getFeed(): List<Feeds> {
    //
    //        val source = Source.SERVER
    //
    //        return withContext(Dispatchers.IO) {
    //            db.collection("feedUri")
    //                .get(source)
    //                .await()
    //                .toObjects(Feeds::class.java)
    //        }
    //    }

    override suspend fun getProductId(id: String): Item {


        return withContext(Dispatchers.IO) {
            val source = Source.DEFAULT

            db.collection("products").document(id)
                .get(source)
                .await()
                .toObject(Item::class.java)!!


            //db.collection("products").document(id)
            //                .get()
            //                .await()
            //                .toObject(Item::class.java)!!


//            //.document(id)
//            docRef.get().result.toObject(Item::class.java)!!
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
    // room database
    override suspend fun addToCart(ordersEntity: OrdersEntity) = ordersDao.insertItem(ordersEntity)

    override fun getAllItems(): Flow<List<OrdersEntity>> = ordersDao.getAllItems()

    override suspend fun delete(ordersEntity: OrdersEntity) = ordersDao.deleteItem(ordersEntity)


    override suspend fun count(ordersEntity: OrdersEntity) = ordersDao.count(ordersEntity)

}
