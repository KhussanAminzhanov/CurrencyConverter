package com.example.currencyconverter.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.database.CurrencyQuote
import com.example.currencyconverter.domain.models.currencydataapiservice.Currencies
import com.example.currencyconverter.network.CurrencyDataApiNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrenciesRepository(val database: CurrencyDatabase) {

    val currencyQuotesList: LiveData<List<CurrencyQuote>> = database.currencyQuoteDao.getAll()
    val currencyNames = MutableLiveData<Currencies>()
    val currencyRates = MutableLiveData<Map<String, Double?>>()

    //Currency Names
    private suspend fun getCurrencyNames(
        onSuccess: (currencies: Currencies) -> Unit,
        onError: (msg: String) -> Unit
    ) {
        CurrencyDataApiNetwork.getCurrencies(onSuccess, onError)
    }

    private fun onCurrencyNamesFetchSuccess(currencyNames: Currencies) {
        this.currencyNames.value = currencyNames
    }

    private fun onCurrencyNamesFetchError(msg: String) {
        Log.e(TAG, "Error loading currency names: $msg")
    }

    suspend fun refreshCurrencyNames() {
        withContext(Dispatchers.IO) {
            getCurrencyNames(::onCurrencyNamesFetchSuccess, ::onCurrencyNamesFetchError)
        }
    }

    //Currency Rates
    private suspend fun getCurrencyRates(
        onSuccess: (rates: Map<String, Double?>) -> Unit,
        onError: (msg: String) -> Unit
    ) {
        CurrencyDataApiNetwork.getRates(onSuccess, onError)
    }

    private fun onCurrencyRatesFetchSuccess(rates: Map<String, Double?>) {
        this.currencyRates.value = rates
    }

    private fun onCurrencyRatesFetchError(msg: String) {
        Log.e(TAG, "Error fetching quotes: $msg")
    }

    suspend fun refreshCurrencyRates() {
        withContext(Dispatchers.IO) {
            getCurrencyRates(::onCurrencyRatesFetchSuccess, ::onCurrencyRatesFetchError)
        }
    }

    //Currency Quotes
    suspend fun refreshCurrencyQuotes(currencyQuotes: List<CurrencyQuote>) {
        withContext(Dispatchers.IO) {
            database.currencyQuoteDao.insertAll(currencyQuotes)
        }
    }

    companion object {
        const val TAG = "currency_repository"
    }
}
