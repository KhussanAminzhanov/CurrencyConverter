package com.example.currencyconverter.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.database.CurrencyQuote
import com.example.currencyconverter.domain.models.Currencies
import com.example.currencyconverter.domain.models.Quotes
import com.example.currencyconverter.network.APILayerNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrenciesRepository(val database: CurrencyDatabase) {

//    val currencyQuotesList: LiveData<List<CurrencyQuote>> = database.currencyQuoteDao.getAll()
    val currencyQuotesList = MutableLiveData<List<CurrencyQuote>>()
    val currencyNames = MutableLiveData<Currencies>()
    val quotes = MutableLiveData<Map<String, Double?>>()

    private fun getCurrenciesQuotesNames(
        onSuccess: (currencies: Currencies) -> Unit,
        onError: () -> Unit
    ) {
        APILayerNetwork.getCurrenciesNames(onSuccess, onError)
    }

    private fun getQuotes(
        startDate: String,
        endDate: String,
        source: String,
        onSuccess: (quotes: Quotes) -> Unit,
        onError: () -> Unit
    ) {
        APILayerNetwork.getChange(startDate, endDate, source, onSuccess, onError)
    }

    fun getCurrencyQuotes(
        onError: () -> Unit
    ) : List<CurrencyQuote> {
        return APILayerNetwork.getCurrenciesQuotes(onError)
    }

    suspend fun refreshCurrencyQuotes() {
        withContext(Dispatchers.IO) {
            val currencyQuotes = getCurrencyQuotes(::onCurrencyQuotesFetchError)
            currencyQuotes.forEach { Log.i(TAG, "|${it.name}|") }
            database.currencyQuoteDao.insertAll(currencyQuotes)
        }
    }

    private suspend fun onCurrencyQuotesFetchSuccess(currencyQuotes: List<CurrencyQuote>) {
        withContext(Dispatchers.IO) {
            database.currencyQuoteDao.insertAll(currencyQuotes)
        }
    }

    private fun onCurrencyQuotesFetchError() {
        Log.e(TAG, "Error fetching currency quotes")
    }

//    suspend fun refreshCurrencyQuotes() {
//        withContext(Dispatchers.IO) {
//            val currencyQuotes = getCurrencyQuotes()
//            database.currencyQuoteDao.insertAll(currencyQuotes)
//        }
//    }
//
//    private fun getCurrencyQuotes() : List<CurrencyQuote> {
//        return currencyNames.value!!::class.memberProperties.map { member ->
//            val ticket = member.name
//            val name = member.call(currencyNames.value!!)
//            val change = quotes.value?.get("KZT$ticket") ?: 1.0
//            CurrencyQuote(name = "$name $ticket", exchangeRate = change)
//        }
//    }

//    suspend fun refreshCurrencyQuotesNames() {
//        withContext(Dispatchers.IO) {
//            getCurrenciesQuotesNames(
//                onSuccess = ::onCurrenciesFetchSuccess,
//                onError = ::onCurrenciesFetchError
//            )
//        }
//    }
//
//    private fun onCurrenciesFetchSuccess(currencies: Currencies) {
//        this.currencyNames.value = currencies
//        Log.i(TAG, "Success fetching ${currencies.AED}")
//    }
//
//    private fun onCurrenciesFetchError() {
//        Log.e(TAG, "Error fetching currencies")
//    }

//    suspend fun refreshQuotes() {
//        withContext(Dispatchers.IO) {
//            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//            val date = LocalDateTime.now().format(format)
//            val source = "KZT"
//            getQuotes(date, date, source, ::onQuotesFetchSuccess, ::onQuotesFetchError)
//        }
//    }
//
//    private fun onQuotesFetchSuccess(quotes: Quotes) {
//        this.quotes.value = quotes.toMap()
//    }
//
//    private fun onQuotesFetchError() {
//        Log.e(TAG, "Error fetching quotes")
//    }

    companion object {
        const val TAG = "currency_repository"
    }
}
