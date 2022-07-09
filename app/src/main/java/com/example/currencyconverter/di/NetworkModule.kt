package com.example.currencyconverter.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.currencyconverter.data.network.CurrencyApiNetwork
import com.example.currencyconverter.data.network.UnsplashApiNetwork
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CURRENCY_API_BASE_URL = "https://api.currencyapi.com/v3/"
private const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"
private const val NAME_CURRENCY_RETROFIT = "CURRENCY_RETROFIT"
private const val NAME_PHOTO_RETROFIT = "PHOTO_RETROFIT"

val networkModule = module {
    factory { GsonBuilder().create() }
    factory {
        OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(androidApplication())
                    .collector(ChuckerCollector(androidApplication()))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    // Currency RatesData Api Dependencies
    factory(named(NAME_CURRENCY_RETROFIT)) {
        Retrofit.Builder()
            .client(get())
            .baseUrl(CURRENCY_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    single { CurrencyApiNetwork(get(named(NAME_CURRENCY_RETROFIT))) }

    // Unsplash Photos Api Dependencies
    factory(named(NAME_PHOTO_RETROFIT)) {
        Retrofit.Builder()
            .client(get())
            .baseUrl(UNSPLASH_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    single { UnsplashApiNetwork(get(named(NAME_PHOTO_RETROFIT))) }
}