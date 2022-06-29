package com.example.currencyconverter.network


interface CurrencyApiNetwork {
    fun getCurrencies(onSuccess: (currencies: Map<String, String>) -> Unit, onError: (msg: String) -> Unit)
    fun getRates(onSuccess: (rates: Map<String, Double?>) -> Unit, onError: (msg: String) -> Unit)
}