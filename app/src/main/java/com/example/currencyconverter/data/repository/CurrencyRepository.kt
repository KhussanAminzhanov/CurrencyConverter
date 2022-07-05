package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.database.CurrencyDatabase
import com.example.currencyconverter.data.database.CurrencyQuote
import com.example.currencyconverter.data.network.CurrencyApiNetwork
import com.example.currencyconverter.domain.models.currencyapi.rate.toMap
import com.example.example.toMap

class CurrencyRepository(
    val database: CurrencyDatabase,
    private val network: CurrencyApiNetwork
) {

    suspend fun refreshCurrencyQuotes() {
        val source = "KZT"
        val currencyNames = network.getCurrencies()?.data?.toMap() ?: return
        val currencyRates = network.getRates()?.data?.toMap(source) ?: return
        val currencyQuotes = currencyNames.map {
            val ticket = it.key
            val name = it.value
            val rate = currencyRates["KZT$ticket"] ?: 1.0
            CurrencyQuote(name = "$name $ticket", exchangeRate = rate)
        }
        database.currencyQuoteDao.insertAll(currencyQuotes)
    }
}