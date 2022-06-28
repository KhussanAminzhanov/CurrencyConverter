package com.example.currencyconverter.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_quote_table")
data class CurrencyQuote constructor(
    @PrimaryKey(autoGenerate = true)
    var currencyId: Int = 0,
    var name: String = "Tenge, Kazakhstan",
    var exchangeRate: Double,
)

fun CurrencyQuote.asCurrency() : Currency {
    return Currency(name = this.name, exchangeRate = this.exchangeRate)
}
