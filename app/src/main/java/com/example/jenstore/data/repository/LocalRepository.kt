package com.example.jenstore.data.repository

import com.example.jenstore.data.local.cart.OrdersDao
import com.example.jenstore.data.local.cart.OrdersEntity
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun addToCart(ordersEntity: OrdersEntity)

    fun getAllItems(): Flow<List<OrdersEntity>>

    suspend fun delete(ordersEntity: OrdersEntity)

    suspend fun count(ordersEntity: OrdersEntity)

    suspend fun clearCart(ordersEntity: List<OrdersEntity>)

}

class LocalRepositoryImpl(
    private val ordersDao: OrdersDao,
) : LocalRepository {

    override suspend fun addToCart(ordersEntity: OrdersEntity) = ordersDao.insertItem(ordersEntity)

    override fun getAllItems(): Flow<List<OrdersEntity>> = ordersDao.getAllItems()

    override suspend fun delete(ordersEntity: OrdersEntity) = ordersDao.deleteItem(ordersEntity)

    override suspend fun clearCart(ordersEntity: List<OrdersEntity>) = ordersDao.clearCart(ordersEntity)

    override suspend fun count(ordersEntity: OrdersEntity) = ordersDao.count(ordersEntity)
}