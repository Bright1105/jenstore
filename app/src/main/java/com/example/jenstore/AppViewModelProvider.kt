package com.example.jenstore

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.ExperimentalPagingApi
import com.example.jenstore.ui.screens.cart.CartViewModel
import com.example.jenstore.ui.screens.feed.FeedViewModel
import com.example.jenstore.ui.screens.home.HomeViewModel
import com.example.jenstore.ui.screens.productDetails.ProductDetailsViewModel
import com.example.jenstore.ui.screens.profile.ProfileViewModel
import com.example.jenstore.ui.screens.profile.account.AccountViewModel
import com.example.jenstore.ui.screens.profile.account.address.AddressViewModel
import com.example.jenstore.ui.screens.profile.account.notification.NotificationViewModel
import com.example.jenstore.ui.screens.profile.account.orders.OrdersViewModel
import com.example.jenstore.ui.screens.profile.account.promotion.PromotionsViewModels
import com.example.jenstore.ui.screens.profile.account.saveitems.SavedItemsViewModel
import com.example.jenstore.ui.screens.profile.createAccount.RegisterAccountViewModel
import com.example.jenstore.ui.screens.profile.loginAccount.LoginViewModel
import com.example.jenstore.ui.screens.search.SearchViewModel
import com.example.jenstore.ui.screens.splash.SplashViewModel


/**
 * Provides Factory to create instance of viewModel for the entire Jenny Store
 */
object AppViewModelProvider {

    @OptIn(ExperimentalPagingApi::class)
    val Factory = viewModelFactory {

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                firebaseRepository = storeApplication().container.firebaseRepository,
            )
        }

        // Initializer for SearchViewModel
        initializer {
            SearchViewModel(
                firebaseRepository = storeApplication().container.firebaseRepository,
            )
        }

        // Initializer for ProductDetailsViewModel
        initializer {
            ProductDetailsViewModel(
                firebaseRepository = storeApplication().container.firebaseRepository,
                localRepository = storeApplication().container.localRepository
            )
        }

        // Initializer for ProfileViewModel
        initializer {
            ProfileViewModel(
                accountService = storeApplication().container.accountService,
                storageService = storeApplication().container.storageService
            )
        }

        // Initializer for CartViewModel
        initializer {
            CartViewModel(
                localRepository = storeApplication().container.localRepository,
                storageService = storeApplication().container.storageService,
                accountService = storeApplication().container.accountService
            )
        }

        // Initializer for FeedViewModel
        initializer {
            FeedViewModel(
                firebaseRepository = storeApplication().container.firebaseRepository,
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

        initializer {
            LoginViewModel(accountService = storeApplication().container.accountService)
        }

        initializer {
            RegisterAccountViewModel(accountService = storeApplication().container.accountService)
        }

        initializer {
            SplashViewModel(accountService = storeApplication().container.accountService)
        }

        initializer {
            AccountViewModel(
                accountService = storeApplication().container.accountService,
                storageService = storeApplication().container.storageService
            )
        }

        initializer {
            AddressViewModel(
                accountService = storeApplication().container.accountService,
                storageService = storeApplication().container.storageService
            )
        }

        initializer {
            SavedItemsViewModel(
                accountService = storeApplication().container.accountService,
                storageService = storeApplication().container.storageService,
                localRepository = storeApplication().container.localRepository
            )
        }

        initializer {
            PromotionsViewModels(
                firebaseRepository = storeApplication().container.firebaseRepository
            )
        }

        initializer {
            NotificationViewModel(
                firebaseRepository = storeApplication().container.firebaseRepository
            )
        }

        initializer {
            OrdersViewModel(
                storageService = storeApplication().container.storageService,
                firebaseRepository = storeApplication().container.firebaseRepository,
                localRepository = storeApplication().container.localRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of [StoreApplication].
 */
fun CreationExtras.storeApplication(): StoreApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as StoreApplication)