package com.example.jenstore.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.local.cart.OrdersDao


@Database(entities = [OrdersEntity::class], version = 11, exportSchema = false)
abstract class StoreDatabase : RoomDatabase() {

    abstract fun ordersDao(): OrdersDao

    companion object {
        @Volatile
        private var Instance: StoreDatabase? = null

        fun getDatabase(context: Context): StoreDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, StoreDatabase::class.java, "store_database")
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
            }
        }
    }
}