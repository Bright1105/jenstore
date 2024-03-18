package com.example.jenstore

import android.app.Application
import com.example.jenstore.data.AppContainer
import com.example.jenstore.data.DefaultAppContainer


class StoreApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}