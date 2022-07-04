package com.example.currencyconverter.domain.models.unsplash

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "photos_table")
data class Photo(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") var id: String? = null,
    @SerializedName("urls") var urls: PhotoUrls = PhotoUrls()
)