package com.example.currencyconverter.network


import com.example.currencyconverter.domain.models.currencydataapiservice.Currencies

interface CurrencyApiNetwork {
    suspend fun getCurrencies(onSuccess: (currencies: Currencies) -> Unit, onError: (msg: String) -> Unit)
    suspend fun getRates(onSuccess: (rates: Map<String, Double?>) -> Unit, onError: (msg: String) -> Unit)
}