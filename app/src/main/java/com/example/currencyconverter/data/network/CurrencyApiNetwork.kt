package com.example.currencyconverter.data.network

import com.example.currencyconverter.domain.models.currencyapi.currency.Currencies
import com.example.currencyconverter.domain.models.currencyapi.rate.Rates
import retrofit2.Retrofit

class CurrencyApiNetwork(retrofit: Retrofit) {

    private val api = retrofit.create(CurrencyApiService::class.java)

    fun getCurrencies(): Currencies? = api.getCurrencies().execute().body()
    fun getRates(): Rates? = api.getRates().execute().body()

}