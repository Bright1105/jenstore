package com.example.jenstore.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jenstore.data.model.Feeds
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.tasks.await

class FeedPagingSource(
    private val queryFeed: Query
) : PagingSource<QuerySnapshot, Feeds>() {

    private val source = Source.SERVER

    private var querySnapshot: QuerySnapshot? = null

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Feeds>): QuerySnapshot? = querySnapshot

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Feeds> = try {
        val currentPage = params.key ?: queryFeed.get(source).await()
        val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
        querySnapshot = currentPage
        val nextPage = queryFeed.startAfter(lastVisibleProduct).get().await()
        LoadResult.Page(
            data = currentPage.toObjects(Feeds::class.java),
            prevKey = null,
            nextKey = nextPage
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override val keyReuseSupported: Boolean = true
}