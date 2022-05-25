package com.example.currencyconverter.currencyscreen

import com.example.currencyconverter.R

data class CurrencyItem(
    var currencyId: Int = 0,
    var name: String = "",
    var image: Int = R.drawable.flag_kazakhstan,
    var value: Long = 0L,
)
