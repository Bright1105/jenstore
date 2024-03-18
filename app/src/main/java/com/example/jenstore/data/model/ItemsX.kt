package com.example.jenstore.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemsX(
    @SerialName("brand")
    val brand: String,
    @SerialName("dateCreated")
    val dateCreated: String,
    @SerialName("description")
    val description: String,
    @SerialName("itemType")
    val itemType: String,
    @SerialName("price")
    val price: Int,
    @SerialName("title")
    val title: String,
    @SerialName("id")
    val id: Int,
)