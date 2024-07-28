package com.example.jenstore.data.model

import android.net.Uri
import com.example.jenstore.R
import com.example.jenstore.data.local.cart.OrdersEntity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable


enum class ItemType (name: Int) {
    Hairs(name = R.string.hair),
    Accessories(name = R.string.hairAccessories),
    Makeup(name = R.string.makeUp),
    Bags(name = R.string.bags)
}

data class SavedItems(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val productItem: Item = Item(),
    val like: Boolean = false
)

data class Checkout(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val itemName: String = "",
    val itemImage: String = "",
    val orderPending: Boolean = true,
    val orderReceived: Boolean = false,
    val cancel: Boolean = false,
    val dateCreated: Timestamp? = null
)


data class CheckoutCancel(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val itemName: String = "",
    val itemImage: String = "",
    val cancel: Boolean = true,
    val dateCreated: Timestamp? = null
)


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
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
           // "${name.first()}",
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

data class Feeds(
    @DocumentId
    val id: String = "",
    val videoUri: String = ""
)

data class Gender(
    val gender: String
)

val genderItem = listOf(
    Gender("Male"),
    Gender("Female")
)

data class Region(
    val region: List<String>
)

val regions = Region(
    listOf(
        "Abia State",
        "Adamawa State",
        "Akwa Ibom State",
        "Anambra State",
        "Bauchi State",
        "Bayelsa State",
        "Benue State" ,
        "Borno State",
        "Cross River State" ,
        "Delta State",
        "Ebonyi State",
        "Edo State"
    )
)

data class City(
    val city: String
)

val cities = listOf(
    City("Benin"),

)