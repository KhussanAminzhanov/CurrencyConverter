package com.example.currencyconverter.domain.models.currencyapi.currency

import com.google.gson.annotations.SerializedName

data class Currencies(
    @SerializedName("data") var data: Map<String, CurrencyName> = emptyMap()
)