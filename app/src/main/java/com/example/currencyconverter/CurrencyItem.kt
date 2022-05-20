package com.example.currencyconverter

data class CurrencyItem(
    var currencyId: Int = 0,
    var name: String = "",
    var image: Int = R.drawable.kazakhstan_flag,
    var value: Long = 0L,
)
