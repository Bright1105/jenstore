package com.example.jenstore.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
data class ProductItem(
   // @SerialName("image")

   // @SerialName("items")
    val id: Int,
    val imageId: Int,
    val title: String,
    val price: Int,
    val image: String,
    val itemType: String,
    val itemAvailable: Int,
    val description: String,
    val dateCreated: String,
    val brand: String,
)