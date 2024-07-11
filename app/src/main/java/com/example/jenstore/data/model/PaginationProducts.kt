package com.example.jenstore.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class PaginationProducts(
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
            "${name.first()}",
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
