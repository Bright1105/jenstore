package com.example.jenstore.data.model

import android.net.Uri
import com.example.jenstore.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable


enum class ItemType (name: Int) {
    Hairs(name = R.string.hair),
    Shoes(name = R.string.shoes),
    Clothes(name = R.string.clothes),
    Bags(name = R.string.bags)
}




data class Item(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val brand: String = "",
    val description: String = "",
    val itemAvailable: Int = 0,
    val itemType: String = "",
    val dateCreated: Timestamp? = null,
    val imageUri: List<String> = listOf()
)

data class Feeds(
    @DocumentId
    val id: String = "",
    val videoUri: String = ""
)
