package com.example.currencyconverter.network

import com.example.currencyconverter.domain.models.currencydataapiservice.Change
import com.example.currencyconverter.domain.models.currencydataapiservice.Currencies
import com.example.currencyconverter.domain.models.currencydataapiservice.CurrenciesList
import com.example.currencyconverter.domain.models.currencydataapiservice.toMap
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

private const val API_KEY = "8x5iX5B7KXhCYzH9EkDgPXWoRqwhOZ8r"
private const val BASE_URL = "https://api.apilayer.com/currency_data/"

interface CurrencyDataApiService {

    @GET("list")
    suspend fun getCurrencies(
        @Header("apiKey") apiKey: String = API_KEY
    ): Call<CurrenciesList>

    @GET("change")
    suspend fun getChange(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("source") source: String,
        @Header("apiKey") apiKey: String = API_KEY
    ): Call<Change>

}

object CurrencyDataApiNetwork : CurrencyApiNetwork {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    private val api = retrofit.create(CurrencyDataApiService::class.java)

    override suspend fun getCurrencies(
        onSuccess: (currencies: Currencies) -> Unit,
        onError: (message: String) -> Unit
    ) {
        api.getCurrencies().enqueue(object : Callback<CurrenciesList> {
            override fun onResponse(
                call: Call<CurrenciesList>,
                response: Response<CurrenciesList>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) onSuccess(body.currencies) else onError("body is null")
                } else onError("response in not successful")
            }

            override fun onFailure(call: Call<CurrenciesList>, t: Throwable) = onError("request failure")
        })
    }

    override suspend fun getRates(
        onSuccess: (rates: Map<String, Double?>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now().format(format)
        val source = "KZT"
        api.getChange(date, date, source).enqueue(object : Callback<Change> {
            override fun onResponse(call: Call<Change>, response: Response<Change>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) onSuccess(body.quotes.toMap(source)) else onError("body is null")
                } else onError("response is not successful")
            }

            override fun onFailure(call: Call<Change>, t: Throwable) = onError("request failure")
        })
    }
}