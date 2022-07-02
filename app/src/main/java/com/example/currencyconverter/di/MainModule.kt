package com.example.currencyconverter.di

import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.repository.CurrencyRepository
import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.example.currencyconverter.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    factory { CurrencyDatabase.getInstance(androidApplication()) }
    factory { CurrencyRepository(get()) }
    viewModel { (repository: CurrencyRepository) -> CurrencyViewModel(repository) }
    viewModel { LoginViewModel(get()) }
}
