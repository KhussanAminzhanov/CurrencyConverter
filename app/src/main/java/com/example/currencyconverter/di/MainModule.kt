package com.example.currencyconverter.di

import com.example.currencyconverter.repository.CurrenciesRepository
import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.example.currencyconverter.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    factory { CurrenciesRepository() }
    viewModel { context -> CurrencyViewModel(context.get(), get()) }
    viewModel { LoginViewModel(get()) }
}
