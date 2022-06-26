package com.example.currencyconverter.domain

import com.example.currencyconverter.R

data class CurrencyItem (
    var currencyId: Int = 0,
    var name: String = "Tenge, Kazakhstan",
    var image: Int = R.drawable.flag_kazakhstan,
    var exchangeRate: Double,
)