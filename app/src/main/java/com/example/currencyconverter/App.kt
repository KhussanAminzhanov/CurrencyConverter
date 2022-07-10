package com.example.currencyconverter

import android.app.Application
import android.content.Context
import com.example.currencyconverter.di.analyticsModule
import com.example.currencyconverter.di.mainModule
import com.example.currencyconverter.di.networkModule
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(mainModule, networkModule, analyticsModule)
        }
    }
}

fun Context.getAnalytics(): Analytics = if (GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
) {
    GMSAnalytics(this)
} else {
//    HMSAnalytics(this)
    GMSAnalytics(this)
}