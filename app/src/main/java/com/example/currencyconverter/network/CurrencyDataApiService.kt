package com.example.currencyconverter.network

import com.example.currencyconverter.di.BASE_URL
import com.example.currencyconverter.domain.models.Change
import com.example.currencyconverter.domain.models.Currencies
import com.example.currencyconverter.domain.models.CurrenciesList
import com.example.currencyconverter.domain.models.Quotes
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val API_KEY = "8x5iX5B7KXhCYzH9EkDgPXWoRqwhOZ8r"

interface CurrencyDataApiService {

    @GET("change")
    fun getChange(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("source") source: String,
        @Header("apiKey") apiKey: String = API_KEY
    ) : Call<Change>

    @GET("list")
    fun getCurrencies(
        @Header("apiKey") apiKey: String = API_KEY
    ) : Call<CurrenciesList>

}

object APILayerNetwork {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    val api = retrofit.create(CurrencyDataApiService::class.java)

    fun getCurrencies(
        onSucces: (currencies: Currencies) -> Unit,
        onError: () -> Unit
    ) {
        api.getCurrencies().enqueue(object : Callback<CurrenciesList> {
            override fun onResponse(call: Call<CurrenciesList>, response: Response<CurrenciesList>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) onSucces(body.currencies) else onError()
                } else onError()
            }

            override fun onFailure(call: Call<CurrenciesList>, t: Throwable) = onError()
        })
    }

    fun getChange(
        startDate: String,
        endDate: String,
        source: String,
        onSuccess: (quotes: Quotes) -> Unit,
        onError: () -> Unit
    ) {
        api.getChange(startDate, endDate, source).enqueue(object : Callback<Change> {
            override fun onResponse(call: Call<Change>, response: Response<Change>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) onSuccess(body.quotes) else onError()
                } else onError()
            }

            override fun onFailure(call: Call<Change>, t: Throwable) = onError()
        })
    }
}