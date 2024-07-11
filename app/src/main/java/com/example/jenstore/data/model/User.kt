package com.example.jenstore.data.model

import android.net.Uri
import com.google.firebase.firestore.DocumentId
import java.time.Instant
import java.util.Date


private const val TITLE_MAX_SIZE = 30

data class User(
    val id: String = ""
)


data class UserInformation(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val image: String = "",
    val firstName: String = "",
    val middleName: String? = "",
    val lastName: String = "",
    val gender: String = "",
    val phoneNumber: String = "",
)

data class UserAddress(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val address: String = "",
    val additionalInformation: String = "",
    val region: String = "",
    val city: String = ""
)
