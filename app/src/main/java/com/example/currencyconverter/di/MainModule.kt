package com.example.currencyconverter.di

import android.content.Context
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.network.CurrencyApiNetwork
import com.example.currencyconverter.repository.CurrencyRepository
import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.example.currencyconverter.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    factory { (context: Context) -> CurrencyDatabase.getInstance(context) }
    factory { (database: CurrencyDatabase, network: CurrencyApiNetwork) -> CurrencyRepository(database, network) }
    viewModel { (repository: CurrencyRepository) -> CurrencyViewModel(repository) }
    viewModel { LoginViewModel(get()) }
}
