package com.example.currencyconverter.di

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

//const val BASE_URL = "https://api.apilayer.com/currency_data/"

val networkModule = module {
    factory { GsonBuilder().create() }
    factory {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
//    factory {
//        Retrofit.Builder()
//            .client(get())
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(get()))
//            .build()
//    }
}