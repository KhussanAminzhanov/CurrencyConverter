package com.example.currencyconverter.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.database.CurrencyQuote
import com.example.currencyconverter.domain.models.currencydataapiservice.Currencies
import com.example.currencyconverter.domain.models.currencydataapiservice.Quotes
import com.example.currencyconverter.domain.models.currencydataapiservice.toMap
import com.example.currencyconverter.network.CurrencyDataApiNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CurrenciesRepository(val database: CurrencyDatabase) {

    val currencyQuotesList: LiveData<List<CurrencyQuote>> = database.currencyQuoteDao.getAll()
    val currencyNames = MutableLiveData<Currencies>()
    val currencyRates = MutableLiveData<Map<String, Double?>>()

    private suspend fun getCurrencyNames(
        onSuccess: (currencies: Currencies) -> Unit,
        onError: (msg: String) -> Unit
    ) {
        CurrencyDataApiNetwork.getCurrencyNames(onSuccess, onError)
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

    private suspend fun getCurrencyRates(
        startDate: String,
        endDate: String,
        source: String,
        onSuccess: (quotes: Quotes) -> Unit,
        onError: (msg: String) -> Unit
    ) {
        CurrencyDataApiNetwork.getChange(startDate, endDate, source, onSuccess, onError)
    }

    private fun onCurrencyRatesFetchSuccess(quotes: Quotes) {
        this.currencyRates.value = quotes.toMap()
    }

    private fun onCurrencyRatesFetchError(msg: String) {
        Log.e(TAG, "Error fetching quotes: $msg")
    }

    suspend fun refreshCurrencyRates() {
        withContext(Dispatchers.IO) {
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDateTime.now().format(format)
            val source = "KZT"
            getCurrencyRates(date, date, source, ::onCurrencyRatesFetchSuccess, ::onCurrencyRatesFetchError)
        }
    }

    suspend fun refreshCurrencyQuotes(currencyQuotes: List<CurrencyQuote>) {
        withContext(Dispatchers.IO) {
            database.currencyQuoteDao.insertAll(currencyQuotes)
        }
    }

    companion object {
        const val TAG = "currencyrepositry"
    }
}
