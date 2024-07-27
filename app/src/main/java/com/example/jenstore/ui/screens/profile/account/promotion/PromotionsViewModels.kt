package com.example.jenstore.ui.screens.profile.account.promotion

import androidx.lifecycle.ViewModel
import com.example.jenstore.data.model.Promotions
import com.example.jenstore.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.Flow

class PromotionsViewModels(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    val promotions: Flow<List<Promotions>> = firebaseRepository.getPromotions()
}