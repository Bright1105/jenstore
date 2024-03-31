package com.example.jenstore.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.jenstore.StoreApplication
import com.example.jenstore.data.Repository

class ProfileVIewModel(
    private val repository: Repository
) : ViewModel() {

}