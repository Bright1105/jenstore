package com.example.jenstore.ui.screens.profile.account.notification

import androidx.lifecycle.ViewModel
import com.example.jenstore.data.model.Notification
import com.example.jenstore.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow

class NotificationViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    val notification: Flow<List<Notification>> = firebaseRepository.getNotification()
}