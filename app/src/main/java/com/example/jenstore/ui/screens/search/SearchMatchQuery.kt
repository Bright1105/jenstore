package com.example.jenstore.ui.screens.search

import com.example.jenstore.data.model.ProductItem


interface SearchMatchQuery {

    fun dosesMatchQuery(query: String): Boolean
}


class SearchMatchQueryImpl(private val item: ProductItem) : SearchMatchQuery {

    override fun dosesMatchQuery(query: String): Boolean {
        val matchCombination = listOf(
            item.items.title,
            "${item.items.title.first()}",
            "${item.items.brand.first()}"
        )
        return matchCombination.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

