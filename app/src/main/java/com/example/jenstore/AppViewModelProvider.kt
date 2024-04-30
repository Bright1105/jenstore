package com.example.jenstore

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.jenstore.ui.screens.cart.CartViewModel
import com.example.jenstore.ui.screens.feed.FeedViewModel
import com.example.jenstore.ui.screens.home.HomeViewModel
import com.example.jenstore.ui.screens.productDetails.ProductDetailsViewModel
import com.example.jenstore.ui.screens.profile.ProfileVIewModel
import com.example.jenstore.ui.screens.search.SearchViewModel
import retrofit2.create


/**
 * Provides Factory to create instance of viewModel for the entire Jenny Store
 */
object AppViewModelProvider {

    @OptIn(ExperimentalPagingApi::class)
    val Factory = viewModelFactory {

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                repository = storeApplication().container.repository,
            )
        }

        // Initializer for SearchViewModel
        initializer {
            SearchViewModel(
                repository = storeApplication().container.repository,
            )
        }

        // Initializer for ProductDetailsViewModel
        initializer {
            ProductDetailsViewModel(repository = storeApplication().container.repository)
        }

        // Initializer for ProfileViewModel
        initializer {
            ProfileVIewModel(
                repository = storeApplication().container.repository
            )
        }

        // Initializer for CartViewModel
        initializer {
            CartViewModel(
                repository = storeApplication().container.repository
            )
        }

        // Initializer for FeedViewModel
        initializer {
            FeedViewModel(
                repository = storeApplication().container.repository,
               // pager = Pager(
                //                    config = PagingConfig(pageSize = 1),
                //                    remoteMediator = FeedRemoteMediator(
                //                        feedDb = storeApplication().container.storeDatabase,
                //                        feedApi = storeApplication().container.retrofit.create()
                //                    ),
                //                    pagingSourceFactory = {
                //                        storeApplication().container.storeDatabase.feedDao().pagingSource()
                //                    }
                //                )
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of [StoreApplication].
 */
fun CreationExtras.storeApplication(): StoreApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as StoreApplication)