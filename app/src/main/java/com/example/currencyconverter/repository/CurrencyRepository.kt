package com.example.currencyconverter.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.database.CurrencyQuote
import com.example.currencyconverter.network.CurrencyApiNetwork
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyRepository(
    val database: CurrencyDatabase,
    val network: CurrencyApiNetwork,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val currencyQuotes: LiveData<List<CurrencyQuote>> = database.currencyQuoteDao.getAll()
    val currencyNames = MutableLiveData<Map<String, String>>()
    val currencyRates = MutableLiveData<Map<String, Double?>>()

    //Currency Names
    private fun getCurrencyNames(
        onSuccess: (currencies: Map<String, String>) -> Unit,
        onError: (msg: String) -> Unit
    ) {
        network.getCurrencies(onSuccess, onError)
    }

    private fun onCurrencyNamesFetchSuccess(currencyNames: Map<String, String>) {
        this.currencyNames.value = currencyNames
    }

    private fun onCurrencyNamesFetchError(msg: String) {
        Log.e(TAG, "Error loading currency names: $msg")
    }

    suspend fun refreshCurrencyNames() {
        withContext(ioDispatcher) {
            getCurrencyNames(::onCurrencyNamesFetchSuccess, ::onCurrencyNamesFetchError)
        }
    }

    //Currency Rates
    private fun getCurrencyRates(
        onSuccess: (rates: Map<String, Double?>) -> Unit,
        onError: (msg: String) -> Unit
    ) {
        network.getRates(onSuccess, onError)
    }

    private fun onCurrencyRatesFetchSuccess(rates: Map<String, Double?>) {
        this.currencyRates.value = rates
    }

    private fun onCurrencyRatesFetchError(msg: String) {
        Log.e(TAG, "Error fetching quotes: $msg")
    }

    suspend fun refreshCurrencyRates() {
        withContext(ioDispatcher) {
            getCurrencyRates(::onCurrencyRatesFetchSuccess, ::onCurrencyRatesFetchError)
        }
    }

    //Currency Quotes
    suspend fun refreshCurrencyQuotes(currencyQuotes: List<CurrencyQuote>) {
        withContext(ioDispatcher) {
            database.currencyQuoteDao.insertAll(currencyQuotes)
        }
    }

    companion object {
        const val TAG = "currency_repository"
    }
}
