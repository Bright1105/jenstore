package com.example.jenstore.data.paging

import androidx.compose.animation.core.snap
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jenstore.data.model.Feeds
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FeedPaging(private val firestore: FirebaseFirestore) : PagingSource<DocumentSnapshot, Feeds>() {

    private var lastDocument: DocumentSnapshot? = null
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Feeds>): DocumentSnapshot? {
        return state.anchorPosition?.let {
            lastDocument
        }
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Feeds> {
        val pageSize = params.loadSize
        val query = firestore.collection("feedUri")
        return if (lastDocument == null) {
            // Initial load
            val snapshot = query.limit(pageSize.toLong()).get().await()
            val items = snapshot.toObjects(Feeds::class.java)
            lastDocument = snapshot.documents.lastOrNull()
            LoadResult.Page(
                items,
                prevKey = null,
                nextKey = if (items.size == pageSize) lastDocument else null
            )
        } else {
            // Subsequent loads
            val snapshot = query.startAfter(lastDocument!!).limit(pageSize.toLong()).get().await()
            val items = snapshot.toObjects(Feeds::class.java)
            lastDocument = snapshot.documents.lastOrNull()
            LoadResult.Page(
                items,
                prevKey = null,
                nextKey = if (items.size == pageSize) lastDocument else null
            )
        }
    }
}