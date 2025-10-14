package com.example.parvaznama

import android.app.Application
import com.example.parvaznama.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class ParvazNamaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ParvazNamaApp)
            modules(appModule)
        }
    }
}