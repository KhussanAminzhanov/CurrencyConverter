package com.example.currencyconverter.data.network

import com.example.currencyconverter.domain.models.currency.Currencies
import com.example.currencyconverter.domain.models.rate.Rates
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.currencyapi.com/v3/"

object CurrencyApiNetwork {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    private val api = retrofit.create(CurrencyApiService::class.java)

    fun getCurrencies(): Currencies? = api.getCurrencies().execute().body()
    fun getRates(): Rates? = api.getRates().execute().body()

}