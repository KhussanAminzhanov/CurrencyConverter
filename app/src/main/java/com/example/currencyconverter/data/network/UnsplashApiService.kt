package com.example.currencyconverter.data.network

import com.example.currencyconverter.domain.models.unsplash.SearchPhotoResult
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "0zaBL_XXgeFHTbY73QN740CU19aJOn_XsAmnpM2bMwo"

interface UnsplashApiService {

    @GET("/search/photos")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30,
        @Query("client_id") clientId: String = API_KEY
    ) : SearchPhotoResult
}

class UnsplashApiNetwork(retrofit: Retrofit) {
    private val api: UnsplashApiService = retrofit.create(UnsplashApiService::class.java)

    fun searchPhotos(query: String) = api.searchPhotos(query)
}