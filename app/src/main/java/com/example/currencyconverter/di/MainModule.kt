package com.example.currencyconverter.di

import com.example.currencyconverter.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
//    factory { database -> CurrenciesRepository(database.get()) }
//    viewModel { (context: Context, repository: CurrenciesRepository) -> CurrencyViewModel(context, repository) }
    viewModel { LoginViewModel(get()) }
}
