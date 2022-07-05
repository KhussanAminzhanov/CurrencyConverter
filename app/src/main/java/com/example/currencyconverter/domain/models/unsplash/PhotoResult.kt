package com.example.currencyconverter.domain.models.unsplash

import com.example.currencyconverter.data.database.Photo
import com.google.gson.annotations.SerializedName

data class PhotoResult(
    @SerializedName("id") var id: String? = null,
    @SerializedName("urls") var urls: PhotoUrls = PhotoUrls()
)

fun PhotoResult.toDatabasePhoto() : Photo {
    return Photo(
        urlFull = this.urls.full ?: "",
        urlRaw = this.urls.raw ?: "",
        urlRegular = this.urls.regular ?: "",
        urlSmall = this.urls.small ?: "",
        urlThumb = this.urls.thumb ?: ""
    )
}