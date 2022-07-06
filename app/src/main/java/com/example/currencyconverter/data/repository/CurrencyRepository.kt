package com.example.currencyconverter.data.repository

import android.util.Log
import com.example.currencyconverter.data.database.CurrencyDatabase
import com.example.currencyconverter.data.database.CurrencyQuote
import com.example.currencyconverter.data.network.CurrencyApiNetwork

class CurrencyRepository(
    val database: CurrencyDatabase,
    private val network: CurrencyApiNetwork
) {

    suspend fun refreshCurrencyQuotes() {
        val currencyNames = network.getCurrencies()?.data ?: return
        val currencyRates = network.getRates()?.data ?: return
        Log.e(this::class.java.simpleName, currencyRates.toString())
        Log.e(this::class.java.simpleName, currencyNames.toString())
        val currencyQuotes = currencyNames.map {
            val ticket = it.key
            val name = it.value.name
            val rate = currencyRates[it.key]?.value ?: 1.0
            CurrencyQuote(name = "$name $ticket", exchangeRate = rate)
        }
        database.currencyQuoteDao.insertAll(currencyQuotes)
    }
}