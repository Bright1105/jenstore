package com.example.jenstore

import android.app.Application
import com.example.jenstore.data.AppContainer
import com.example.jenstore.data.DefaultAppContainer
import com.example.jenstore.data.model.User


class StoreApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */


    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}