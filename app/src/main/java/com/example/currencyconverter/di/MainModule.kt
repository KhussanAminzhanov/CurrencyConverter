package com.example.currencyconverter.di

import com.example.currencyconverter.data.database.CurrencyDatabase
import com.example.currencyconverter.data.repository.CurrencyRepository
import com.example.currencyconverter.presentation.converter.CurrencyViewModel
import com.example.currencyconverter.presentation.login.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    factory { CurrencyDatabase.getInstance(androidApplication()) }
    factory { CurrencyRepository(get(), get()) }
    viewModel { CurrencyViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}
