package com.example.currencyconverter.di

import androidx.work.WorkManager
import com.example.currencyconverter.data.database.CurrencyDatabase
import com.example.currencyconverter.data.database.PhotoDatabase
import com.example.currencyconverter.data.repository.CurrencyRepository
import com.example.currencyconverter.data.repository.PhotosRepository
import com.example.currencyconverter.presentation.history.HistoryViewModel
import com.example.currencyconverter.presentation.converter.CurrencyViewModel
import com.example.currencyconverter.presentation.login.LoginViewModel
import com.example.currencyconverter.presentation.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    // Create and Convert Currency Screen Dependencies
    factory { CurrencyDatabase.getInstance(androidApplication()) }
    factory { CurrencyRepository(get(), get()) }
    viewModel { CurrencyViewModel(get()) }

    // Search and Download Photo Screen Dependencies
    factory { WorkManager.getInstance(androidApplication()) }
    factory { PhotoDatabase.getInstance(androidApplication()) }
    factory { PhotosRepository(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }

    viewModel { HistoryViewModel(get()) }

    viewModel { LoginViewModel(get()) }
}
