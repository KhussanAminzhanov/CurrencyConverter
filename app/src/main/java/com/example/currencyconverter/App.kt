package com.example.currencyconverter

import android.app.Application
import com.example.currencyconverter.di.mainModule
import com.example.currencyconverter.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(mainModule, networkModule)
        }
    }
}