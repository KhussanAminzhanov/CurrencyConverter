package com.example.currencyconverter.repository

import com.example.currencyconverter.domain.models.Currencies
import com.example.currencyconverter.network.APILayerNetwork

class CurrenciesRepository {

//    lateinit var currencies: Currencies

//    suspend fun getCurrencies() {
//        APILayerNetwork.getCurrencies(
//            onSucces = ::onCurrenciesFetchSuccess,
//            onError = ::onCurrenciesFetchError
//        )
//    }

//    private fun onCurrenciesFetchSuccess(currencies: Currencies) {
//        this.currencies = currencies
//    }
//
//    private fun onCurrenciesFetchError() {
//        Log.e(TAG, "Error fetching currencies")
//    }

    suspend fun getCurrencies(onSuccess: (currencies: Currencies) -> Unit, onError: () -> Unit) {
        APILayerNetwork.getCurrencies(onSuccess, onError)
    }

    companion object {
        const val TAG = "currency_repository"
    }
}
