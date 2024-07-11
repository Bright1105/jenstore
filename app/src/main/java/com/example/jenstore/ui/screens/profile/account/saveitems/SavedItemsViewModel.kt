package com.example.jenstore.ui.screens.profile.account.saveitems

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import com.example.jenstore.MyCart
import com.example.jenstore.SaveItems
import com.example.jenstore.StoreDestinations
import com.example.jenstore.data.local.cart.OrdersEntity
import com.example.jenstore.data.model.Item
import com.example.jenstore.data.model.SavedItems
import com.example.jenstore.data.repository.LocalRepository
import com.example.jenstore.data.service.AccountService
import com.example.jenstore.data.service.StorageService
import com.example.jenstore.ui.screens.StoreAppViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SavedItemsViewModel(
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val localRepository: LocalRepository
) : StoreAppViewModel() {

    var like = mutableStateOf(false)

    val count = MutableStateFlow(1)

    val savedItems = storageService.getSaveItems


    fun saveItems(item: Item) {
        launchCatching {
            like.value = !like.value
            val savedItem = SavedItems(
                id = item.id,
                productItem = Item(
                    name = item.name,
                    price = item.price,
                    brand = item.brand,
                    description = item.description,
                    imageUri = item.imageUri,
                    itemAvailable = item.itemAvailable,
                    itemType = item.itemType,
                    dateCreated = Timestamp.now()
                ),
                userId = accountService.currentUserId,
                like = like.value
            )
            if (like.value) {
                storageService.createSavedItems(savedItem)
            } else {
                storageService.deleteSavedItems(item.id)
            }
        }
    }

    fun deleteSavedItems(itemId: String) {
        launchCatching {
            storageService.deleteSavedItems(itemId)
        }
    }

    fun onAddToCartClicked(savedItems: SavedItems) {
        CoroutineScope(Dispatchers.IO).launch {

            runCatching {
                localRepository.addToCart(
                    ordersEntity = OrdersEntity(
                        id = savedItems.id,
                        title = savedItems.productItem.name,
                        brand = savedItems.productItem.brand,
                        price = savedItems.productItem.price,
                        description = savedItems.productItem.description,
                        dateCreated = savedItems.productItem.dateCreated.toString(),
                        image = savedItems.productItem.imageUri[0],
                        countItem = count.value,
                        itemType = savedItems.productItem.itemType
                    )
                )
            }
        }
    }

    fun onAddToCartNavigate(openAndPopup: (StoreDestinations, StoreDestinations) -> Unit) {
        openAndPopup(MyCart, SaveItems)
    }

    fun increaseCount() {
        count.value += 1
    }

    fun decreaseCount() {
        count.value -= 1
    }
}
