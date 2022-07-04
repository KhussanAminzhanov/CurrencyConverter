package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.database.PhotoDatabase
import com.example.currencyconverter.data.network.UnsplashApiNetwork

class PhotosRepository(
    val database: PhotoDatabase,
    private val network: UnsplashApiNetwork
) {

    fun searchPhotos(query: String) {
        val result = network.searchPhotos(query).results
        database.photoDao.addAll(result)
    }
}