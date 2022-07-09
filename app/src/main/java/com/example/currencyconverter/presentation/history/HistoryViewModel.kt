package com.example.currencyconverter.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.database.CurrencyTransaction
import com.example.currencyconverter.data.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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

    fun updateAllCurrencyTransactions(transactions: List<CurrencyTransaction>) =
        viewModelScope.launch(ioDispatcher){
            repository.database.transactionDao.updateAll(transactions)
        }

    fun moveCurrencyTransactions(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                currencyTransactions.value?.let { Collections.swap(it, i, i + 1) }
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                currencyTransactions.value?.let { Collections.swap(it, i, i - 1) }
            }
        }
    }

    companion object {
        const val TAG = "HistoryViewModel"
    }
}