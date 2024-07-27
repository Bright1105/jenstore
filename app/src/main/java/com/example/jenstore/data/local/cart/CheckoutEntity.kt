package com.example.jenstore.data.local.cart

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "Checkout")
data class CheckoutEntity(
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val dateCreated: String? = null,
    val image: String = "",
)
