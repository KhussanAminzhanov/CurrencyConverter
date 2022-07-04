package com.example.currencyconverter.di

import com.example.currencyconverter.data.network.CurrencyApiNetwork
import com.example.currencyconverter.data.network.UnsplashApiNetwork
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CURRENCY_API_BASE_URL = "https://api.currencyapi.com/v3/"
private const val UNSPLASH_API_BASE_URL = "https://api.unsplash.com/"

val networkModule = module {
    factory { GsonConverterFactory.create(GsonBuilder().create()) }
    factory {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    factory(named("CurrencyRetrofit")) {
        Retrofit.Builder()
            .client(get())
            .baseUrl(CURRENCY_API_BASE_URL)
            .addConverterFactory(get())
            .build()
    }
    single { CurrencyApiNetwork(get()) }
    factory(named("UnsplashRetrofit")) {
        Retrofit.Builder()
            .client(get())
            .baseUrl(UNSPLASH_API_BASE_URL)
            .addConverterFactory(get())
            .build()
    }
    single { UnsplashApiNetwork(get()) }
}