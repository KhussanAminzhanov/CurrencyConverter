package com.example.currencyconverter.currencyscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.database.CurrencyDao

class CurrencyViewModelFactory(private val dao: CurrencyDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}