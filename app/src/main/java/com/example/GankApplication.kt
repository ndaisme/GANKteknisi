package com.example

import android.app.Application
import com.example.data.AppContainer

class GankApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
