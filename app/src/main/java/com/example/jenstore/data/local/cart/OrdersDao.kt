package com.example.jenstore.data.local.cart

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface OrdersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(ordersEntity: OrdersEntity)

    @Query("SELECT * FROM orders ORDER BY id ASC")
    fun getAllItems(): Flow<List<OrdersEntity>>

    @Delete
    fun deleteItem(ordersEntity: OrdersEntity)

    @Update
    fun count(ordersEntity: OrdersEntity)


}