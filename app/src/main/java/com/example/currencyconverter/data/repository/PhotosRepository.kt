package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.database.PhotoDatabase
import com.example.currencyconverter.data.network.UnsplashApiNetwork
import com.example.currencyconverter.domain.models.unsplash.toDatabasePhoto

class PhotosRepository(
    val database: PhotoDatabase,
    private val network: UnsplashApiNetwork
) {

    fun searchPhotos(query: String) {
        val result = network.searchPhotos(query).execute().body()?.results ?: return
        database.photoDao.addAll(result.map { it.toDatabasePhoto() })
    }
}