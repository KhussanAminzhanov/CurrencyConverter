package com.example.currencyconverter.data.network

import com.example.currencyconverter.domain.models.currencyapi.currency.Currencies
import com.example.currencyconverter.domain.models.currencyapi.rate.Rates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val API_KEY = "lG4rpOC2exJT8YHlBfOoecCjSDtmFxEGsJN4ijHc"

interface CurrencyApiService {

    @GET("currencies")
    fun getCurrencies(
        @Header("apiKey") apiKey: String = API_KEY
    ): Call<Currencies>

    @GET("latest")
    fun getRates(
        @Query("base_currency") source: String = "KZT",
        @Header("apiKey") apiKey: String = API_KEY
    ): Call<Rates>
}

