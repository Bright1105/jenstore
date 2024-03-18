package com.example.jenstore.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductItem(
    @SerialName("image")
    val image: String,
    @SerialName("items")
    val items: ItemsX,
)