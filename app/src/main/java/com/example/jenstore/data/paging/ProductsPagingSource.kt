package com.example.jenstore.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.model.PaginationProducts
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class ProductsPagingSource(
    private val queryProduct: Query,
) : PagingSource<QuerySnapshot, Item>() {

    private val source = Source.SERVER

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Item>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Item> = try {
        val currentPage = params.key ?: queryProduct.get(source).await()
        val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
        val nextPage = queryProduct.startAfter(lastVisibleProduct).get().await()
        LoadResult.Page(
            data = currentPage.toObjects(Item::class.java),
            prevKey = null,
            nextKey = nextPage
        )
    } catch(e: Exception) {
        LoadResult.Error(e)
    }

    override val keyReuseSupported: Boolean = true

}