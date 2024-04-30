package com.example.jenstore.data

import com.example.jenstore.data.model.ProductFeed
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.local.cart.OrdersDao
import com.example.jenstore.data.model.ProductItem
import com.example.jenstore.data.remote.StoreApiService
import com.example.jenstore.data.remote.feed.FeedDto
import com.example.jenstore.data.remote.homeProduct.ProductDto
import kotlinx.coroutines.flow.Flow

//const val NETWORK_PAGE_SIZE = 25
interface Repository {

    // network database
    suspend fun getItems(): List<ProductDto>

    suspend fun getItemById(id: Int): ProductDto

    suspend fun searchQuery(searchQuery: String?): List<ProductDto>

    suspend fun getFeeds(): List<FeedDto>


    // Room database

    suspend fun addToCart(ordersEntity: OrdersEntity)

    fun getAllItems(): Flow<List<OrdersEntity>>

    suspend fun delete(ordersEntity: OrdersEntity)

}

class RepositoryImpl(
    private val storeApiService: StoreApiService,
    private val ordersDao: OrdersDao
) : Repository {

    override suspend fun getItems(): List<ProductDto> = storeApiService.getItems()

    override suspend fun getItemById(id: Int): ProductDto = storeApiService.getItemById(id)

    override suspend fun searchQuery(searchQuery: String?): List<ProductDto> = storeApiService.searchQuery(searchQuery)
    override suspend fun getFeeds(): List<FeedDto> = storeApiService.getFeeds()

    // room database
    override suspend fun addToCart(ordersEntity: OrdersEntity) = ordersDao.insertItem(ordersEntity)

    override fun getAllItems(): Flow<List<OrdersEntity>> = ordersDao.getAllItems()

    override suspend fun delete(ordersEntity: OrdersEntity) = ordersDao.deleteItem(ordersEntity)


}


// override fun getFeed(): Flow<PagingData<ProductFeed>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = NETWORK_PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = {
//                FeedPagingSource(service = storeApiService)
//            }
//        ).flow
//    }