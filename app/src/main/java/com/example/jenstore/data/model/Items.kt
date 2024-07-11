package com.example.jenstore.data.model

import android.net.Uri
import com.example.jenstore.R
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
    val region: String
)

val regions = listOf(
    Region("Abia"),
    Region("Akwa Ibom"),
    Region("Anambra"),
    Region("Bauchi"),
    Region("Bayelsa"),
    Region("Benue"),
    Region("Borno"),
    Region("Cross River"),
    Region("Delta"),
    Region("Ebonyi"),
    Region("Edo"),
    Region("Ekiti"),
    Region("Enugu"),
    Region("Lagos"),
    Region("Kano"),
    Region("Niger"),
    Region("Ondo"),
    Region("Kaduna"),
)

data class City(
    val city: String
)

val cities = listOf(
    City("Benin"),

)