package com.example.galactilist

import android.app.Application
import com.example.galactilist.utils.ConnectivityChecker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GalactiListApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeApp()
    }

    fun initializeApp() {
        ConnectivityChecker.instance.initializeWithApplicationContext(this)
    }
}