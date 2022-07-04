package com.example.currencyconverter.domain.models.unsplash

import com.google.gson.annotations.SerializedName

data class SearchPhotoResult(
    @SerializedName("results") var results: List<Photo> = listOf()
)