package com.example.currencyconverter.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.database.CurrencyTransaction
import com.example.currencyconverter.data.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: CurrencyRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val currencyTransactions = repository.getCurrencyTransactions()

    fun addCurrencyTransaction(transaction: CurrencyTransaction) =
        viewModelScope.launch(ioDispatcher) {
            repository.addCurrencyTransaction(transaction)
        }

    fun deleteCurrencyTransaction(transaction: CurrencyTransaction) =
        viewModelScope.launch(ioDispatcher) {
            repository.deleteCurrencyTransaction(transaction)
        }
}