package com.example.currencyconverter.network

import com.example.currencyconverter.domain.models.currencyapiservice.currency.Currencies
import com.example.currencyconverter.domain.models.currencyapiservice.rate.Rates
import com.example.currencyconverter.domain.models.currencyapiservice.rate.toMap
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import com.example.currencyconverter.domain.models.currencydataapiservice.Currencies as C

private const val API_KEY = "lG4rpOC2exJT8YHlBfOoecCjSDtmFxEGsJN4ijHc"
private const val BASE_URL = "https://api.currencyapi.com/v3/"

interface CurrencyApiService {

    @GET("currencies")
    suspend fun getCurrencies(
        @Header("apiKey") apiKey: String = API_KEY
    ) : Call<Currencies>

    @GET("latest")
    suspend fun getRates(
        @Query("base_currency") source: String = "KZT",
        @Header("apiKey") apiKey: String = API_KEY
    ) : Call<Rates>
}

object CurrencyApiNetworkIml : CurrencyApiNetwork {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    private val api = retrofit.create(CurrencyApiService::class.java)

    override suspend fun getCurrencies(
        onSuccess: (currencies: C) -> Unit,
        onError: (msg: String) -> Unit
    ) {
       TODO("Not yet implemented")
    }

    override suspend fun getRates(
        onSuccess: (rates: Map<String, Double?>) -> Unit,
        onError: (msg: String) -> Unit
    ) {
        val source = "KZT"
        api.getRates(source).enqueue(object : Callback<Rates> {
            override fun onResponse(call: Call<Rates>, response: Response<Rates>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onSuccess(body.data.toMap(source))
                    } else onError("body is null")
                } else onError("response is not successful")
            }

            override fun onFailure(call: Call<Rates>, t: Throwable) = onError("request failure")

        })
    }
}