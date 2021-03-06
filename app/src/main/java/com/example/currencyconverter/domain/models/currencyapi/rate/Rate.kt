package com.example.currencyconverter.domain.models.currencyapi.rate

import com.google.gson.annotations.SerializedName

data class Rate(
    @SerializedName("code") var code: String? = null,
    @SerializedName("value") var value: Double? = null
)