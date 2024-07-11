package com.example.jenstore.data.local.cart

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Orders")
data class OrdersEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val brand: String,
    val countItem: Int,
    val price: Int,
    val description: String,
    val itemType: String,
    val dateCreated: String? = null,
    val image: String,
    // val itemAvailable: Int,
)