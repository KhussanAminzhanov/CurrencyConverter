package com.example.currencyconverter.repository

import com.example.currencyconverter.domain.models.Currencies
import com.example.currencyconverter.domain.models.Quotes
import com.example.currencyconverter.network.APILayerNetwork

class CurrenciesRepository {

    fun getCurrencies(onSuccess: (currencies: Currencies) -> Unit, onError: () -> Unit) {
        APILayerNetwork.getCurrencies(onSuccess, onError)
    }

    fun getQuotes(startDate: String, endDate: String, source: String, onSuccess: (quotes: Quotes) -> Unit, onError: () -> Unit) {
        APILayerNetwork.getChange(startDate, endDate, source, onSuccess, onError)
    }

    companion object {
        const val TAG = "currency_repository"
    }
}
