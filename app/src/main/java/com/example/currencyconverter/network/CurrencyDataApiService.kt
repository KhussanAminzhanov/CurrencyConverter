package com.example.currencyconverter.network

import com.example.currencyconverter.domain.models.Change
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val API_KEY = "8x5iX5B7KXhCYzH9EkDgPXWoRqwhOZ8r"

interface APILayerService {

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
    ) : Call<List<Response>>

}