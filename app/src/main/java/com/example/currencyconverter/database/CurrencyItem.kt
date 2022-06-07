package com.example.currencyconverter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencyconverter.R

@Entity(tableName = "currency_table")
data class CurrencyItem(
    @PrimaryKey(autoGenerate = true) var currencyId: Int = 0,
    @ColumnInfo(name = "currency_name") var name: String = "Tenge, Kazakhstan",
    @ColumnInfo(name = "currency_image") var image: Int = R.drawable.flag_kazakhstan,
    @ColumnInfo(name = "currency_exchange_rate") var exchangeRate: Double,
)
