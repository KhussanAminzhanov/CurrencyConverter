package com.example.currencyconverter.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencyconverter.R

@Entity(tableName = "currency_table")
data class CurrencyItem constructor(
    @PrimaryKey(autoGenerate = true)
    var currencyId: Int = 0,
    var name: String = "Tenge, Kazakhstan",
    var image: Int = R.drawable.flag_kazakhstan,
    var exchangeRate: Double,
)
