package com.example.currencyconverter.di

import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.example.currencyconverter.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { context -> CurrencyViewModel(context.get()) }
    viewModel { LoginViewModel(get()) }
}

val modules = listOf(viewModelModule)