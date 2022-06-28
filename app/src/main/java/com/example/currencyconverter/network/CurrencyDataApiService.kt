package com.example.currencyconverter.network

import android.util.Log
import com.example.currencyconverter.database.CurrencyQuote
import com.example.currencyconverter.di.BASE_URL
import com.example.currencyconverter.domain.models.*
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.memberProperties

private const val API_KEY = "8x5iX5B7KXhCYzH9EkDgPXWoRqwhOZ8r"

interface CurrencyDataApiService {

    @GET("change")
    fun getChange(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("source") source: String,
        @Header("apiKey") apiKey: String = API_KEY
    ): Call<Change>

    @GET("list")
    fun getCurrencies(
        @Header("apiKey") apiKey: String = API_KEY
    ): Call<CurrenciesList>

}

object APILayerNetwork {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    private val api = retrofit.create(CurrencyDataApiService::class.java)

    fun getCurrenciesNames(
        onSuccess: (currencies: Currencies) -> Unit,
        onError: () -> Unit
    ) {
        api.getCurrencies().enqueue(object : Callback<CurrenciesList> {
            override fun onResponse(
                call: Call<CurrenciesList>,
                response: Response<CurrenciesList>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) onSuccess(body.currencies) else onError()
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

    fun getCurrenciesQuotes(onError: () -> Unit) : List<CurrencyQuote> {
        var currencyQuotes = listOf<CurrencyQuote>()
        api.getCurrencies().enqueue(object : Callback<CurrenciesList> {
            override fun onResponse(
                call: Call<CurrenciesList>,
                response: Response<CurrenciesList>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) currencyQuotes = onCurrenciesListFetched(
                        body.currencies,
                        onError
                    ) else onError()
                } else onError()
            }

            override fun onFailure(call: Call<CurrenciesList>, t: Throwable) = onError()
        })
        return currencyQuotes
    }

    private fun onCurrenciesListFetched(
        currencies: Currencies,
        onError: () -> Unit
    ): List<CurrencyQuote> {
        var currencyQuotes = listOf<CurrencyQuote>()
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(format)
        val source = "KZT"

        api.getChange(date, date, source).enqueue(object : Callback<Change> {
            override fun onResponse(call: Call<Change>, response: Response<Change>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val list = currencies::class.memberProperties.map { member ->
                            val ticket = member.name
                            val name = member.call(currencies)
                            val change = body.quotes.toMap()["KZT$ticket"] ?: 1.0
                            CurrencyQuote(name = "$name $ticket", exchangeRate = change)
                        }
                        currencyQuotes = list
                    } else onError()
                } else onError()
            }

            override fun onFailure(call: Call<Change>, t: Throwable) = onError()
        })

        return currencyQuotes
    }

    private fun onError(text: String) {
        Log.i("api", text)
    }
}