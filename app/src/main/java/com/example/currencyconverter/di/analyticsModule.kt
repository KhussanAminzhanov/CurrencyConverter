package com.example.currencyconverter.di

import android.content.Context
import com.example.currencyconverter.Analytics
import com.example.currencyconverter.getAnalytics
import org.koin.dsl.module

val analyticsModule = module {
    single<Analytics> { get<Context>().getAnalytics() }
}