package com.example.jenstore.data.service

import android.net.Uri
import com.example.jenstore.data.model.Checkout
import com.example.jenstore.data.model.CheckoutCancel
import com.example.jenstore.data.model.SavedItems
import com.example.jenstore.data.model.UserAddress
import com.example.jenstore.data.model.UserInformation
import com.google.firebase.Firebase
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



interface StorageService {
    suspend fun createUserInformation(userInformation: UserInformation)
    suspend fun createUserAddress(userAddress: UserAddress)
    suspend fun updateUserInformation(userInformation: UserInformation)
    suspend fun updateUserAddress(userAddress: UserAddress)
    suspend fun addImageToStorage(imageUri: Uri)
    suspend fun createSavedItems(savedItems: SavedItems)
    suspend fun deleteSavedItems(itemId: String)

    suspend fun checkout(checkout: Checkout)
    suspend fun updateCancel(checkout: Checkout)
    suspend fun getCheckoutById(id: String): Checkout?
    suspend fun deleteCheckout(checkout: Checkout)
    suspend fun checkoutCanceled(checkout: CheckoutCancel)

    val userInfo: Flow<UserInformation?>
    val getUserAddress: Flow<UserAddress?>
    val getSaveItems: Flow<List<SavedItems>>
    val getCheckout: Flow<List<Checkout>>
    val getCheckoutCancel: Flow<List<CheckoutCancel>>

}


class StorageServiceImpl(
    private val accountService: AccountService
) : StorageService {

    private var downloadImageUri: Uri? = null

    override suspend fun getCheckoutById(id: String): Checkout? {
        return withContext(Dispatchers.IO) {
            Firebase.firestore
                .collection(CHECKOUT)
                .document(id)
                .get()
                .await()
                .toObject()
        }
    }

    override suspend fun checkout(checkout: Checkout) {
        Firebase.firestore
            .collection(CHECKOUT)
            .add(checkout)
            .await()
    }

    override suspend fun checkoutCanceled(checkout: CheckoutCancel) {
        Firebase.firestore
            .collection(CHECKOUTCANCELED)
            .add(checkout)
            .await()
    }

    override suspend fun deleteCheckout(checkout: Checkout) {

        return withContext(Dispatchers.IO) {
            Firebase.firestore
                .collection(CHECKOUT)
                .document(checkout.id)
                .delete()
        }
    }

    override suspend fun updateCancel(checkout: Checkout) {
        Firebase.firestore
            .collection(CHECKOUT)
            .document(checkout.id)
            .set(checkout)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userInfo: Flow<UserInformation?>
        = accountService.currentUser.flatMapLatest {  user ->
           if (user?.id != null) {
               Firebase.firestore
                   .collection(USER_INFORMATION)
                   .document(user.id)
                   .dataObjects()
           }  else {
               flowOf(UserInformation())
           }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val getCheckout: Flow<List<Checkout>> =
        accountService.currentUser.flatMapLatest {  user ->
            if (user?.id != null) {
                Firebase.firestore
                    .collection(CHECKOUT)
                    .whereEqualTo(USER_ID_FIELD, user.id)
                    .dataObjects()
            } else {
               flowOf()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val getCheckoutCancel: Flow<List<CheckoutCancel>> =
        accountService.currentUser.flatMapLatest { user ->
            if (user?.id != null) {
                Firebase.firestore
                    .collection(CHECKOUTCANCELED)
                    .whereEqualTo(USER_ID_FIELD, user.id)
                    .dataObjects()
            } else {
                flowOf()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val getUserAddress: Flow<UserAddress?> =
        accountService.currentUser.flatMapLatest {  address ->
            if (address?.id != null) {
                Firebase.firestore
                    .collection(USER_ADDRESS)
                    .document(address.id)
                    .dataObjects()
            } else {
                flowOf(UserAddress())
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val getSaveItems: Flow<List<SavedItems>>
        get() = accountService.currentUser.flatMapLatest {  user ->
            Firebase.firestore
                .collection(USER_SAVED_ITEMS)
                .whereEqualTo(USER_ID_FIELD, user?.id)
                .dataObjects()
        }


    //    @OptIn(ExperimentalCoroutinesApi::class)
//    override val userAddress: Flow<List<UserAddress?>?>
//        = accountService.currentUser.flatMapLatest { user ->
//            if (user?.id != null) {
//                Firebase.firestore
//                    .collection(USER_ADDRESS)
//                    .whereEqualTo(USER_ID_FIELD, user.id)
//                    .dataObjects()
//            } else {
//                flowOf()
//            }
//    }

    override suspend fun addImageToStorage(imageUri: Uri) {

        withContext(Dispatchers.IO) {
            val storageRef = Firebase.storage.reference.child("user/images").child(accountService.currentUserId)
            val uploadTask = storageRef.putFile(imageUri)

            val download = uploadTask.continueWithTask {
                if (!it.isSuccessful) {
                    throw it.exception!!
                }
                it.result.storage.downloadUrl
            }.await()

            downloadImageUri = download
        }
    }

    override suspend fun createUserInformation(userInformation: UserInformation) {
        val user = userInformation.copy(
            image = downloadImageUri.toString()
        )
        Firebase.firestore
            .collection(USER_INFORMATION)
            .document(accountService.currentUserId)
            .set(user).await()
    }

    override suspend fun createSavedItems(savedItems: SavedItems) {
        Firebase.firestore
            .collection(USER_SAVED_ITEMS)
            .document(savedItems.id)
            .set(savedItems).await()
    }

    override suspend fun deleteSavedItems(itemId: String) {
        Firebase.firestore
            .collection(USER_SAVED_ITEMS)
            .document(itemId)
            .delete()
    }

    //    override suspend fun getUserInformation(): List<UserInformation> {
//
//        return withContext(Dispatchers.IO) {
//            Firebase.firestore
//                .collection(USER_INFORMATION)
//                .whereEqualTo(USER_ID_FIELD, accountService.currentUserId)
//                .get()
//                .await()
//                .toObjects(UserInformation::class.java)
//        }
//    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    override val user: Flow<List<UserInformation>>
//        get() =
//            accountService.currentUser.flatMapLatest { user ->
//                Firebase.firestore
//                    .collection(USER_INFORMATION)
//                    .whereEqualTo(USER_ID_FIELD, user?.id)
//                    .dataObjects()
//            }


    override suspend fun updateUserInformation(userInformation: UserInformation) {
      //  val user = userInformation.copy(
        //            image = downloadImageUri
        //        )
        userInformation.id?.let {
            Firebase.firestore
                .collection(USER_INFORMATION)
                .document(it)
                .set(userInformation).await()
        }
    }

    override suspend fun updateUserAddress(userAddress: UserAddress) {
        withContext(Dispatchers.IO) {
            userAddress.id?.let {
                Firebase.firestore
                    .collection("address")
                    .document(it)
                    .set(userAddress)
            }
        }
    }

    override suspend fun createUserAddress(userAddress: UserAddress) {
        withContext(Dispatchers.IO) {
            Firebase.firestore
                .collection(USER_ADDRESS)
                .document(accountService.currentUserId)
                .set(userAddress).await()
        }
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val USER_INFORMATION = "userInformation"
        private const val USER_ADDRESS = "userAddress"
        private const val USER_SAVED_ITEMS = "userSavedItems"
        private const val PRODUCTS = "products"
        private const val FEED_URI = "feedUri"
        private const val CHECKOUT = "checkout"
        private const val CHECKOUTCANCELED = "checkoutCanceled"
    }
}