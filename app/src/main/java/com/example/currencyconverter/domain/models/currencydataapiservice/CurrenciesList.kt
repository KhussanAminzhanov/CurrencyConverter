package com.example.currencyconverter.domain.models.currencydataapiservice

import com.google.gson.annotations.SerializedName

data class CurrenciesList(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("currencies") var currencies: Currencies = Currencies()
)