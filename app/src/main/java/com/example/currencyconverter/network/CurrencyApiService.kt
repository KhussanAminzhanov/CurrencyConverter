package com.example.currencyconverter.network

import com.example.currencyconverter.domain.models.currencyapiservice.rate.Rates
import com.example.currencyconverter.domain.models.currencydataapiservice.Currencies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val API_KEY = "lG4rpOC2exJT8YHlBfOoecCjSDtmFxEGsJN4ijHc"
private const val BASE_URL = "https://api.currencyapi.com/v3/"

interface CurrencyApiService {

    @GET("currencies")
    suspend fun getCurrencies(
        @Header("apiKey") apiKey: String = API_KEY
    ) : Call<Currencies>

    @GET("latest")
    suspend fun getRates(
        @Query("base_currency") baseCurrency: String = "KZT",
        @Header("apiKey") apiKey: String = API_KEY
    ) : Call<Rates>
}