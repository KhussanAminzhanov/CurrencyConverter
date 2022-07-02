package com.example.currencyconverter.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencyconverter.R

@Entity(tableName = "currency_table")
data class Currency constructor(
    @PrimaryKey(autoGenerate = true)
    var currencyId: Int = 0,
    var name: String = "Tenge, Kazakhstan",
    var image: Int = R.drawable.flag_usa,
    var exchangeRate: Double,
)
