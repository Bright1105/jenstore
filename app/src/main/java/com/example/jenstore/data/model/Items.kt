package com.example.jenstore.data.model

import com.example.jenstore.R
import kotlinx.serialization.Serializable


enum class ItemType (name: Int) {
    Hairs(name = R.string.hair),
    Shoes(name = R.string.shoes),
    Clothes(name = R.string.clothes),
    Bags(name = R.string.bags)
}


