package com.example.jenstore.data.local.cart

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Orders")
data class OrdersEntity(
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val brand: String = "",
    val countItem: Int = 0,
    val price: Int = 0,
    val description: String? = null,
    val itemType: String? = null,
    val dateCreated: String? = null,
    val image: String = "",
)