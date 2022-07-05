package com.example.currencyconverter.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos_table")
data class Photo constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var urlFull: String,
    var urlRaw: String,
    var urlThumb: String,
    var urlSmall: String,
    var urlRegular: String
)