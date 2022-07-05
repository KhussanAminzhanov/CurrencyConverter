package com.example.currencyconverter.domain.models.currencyapi.rate

import com.example.currencyconverter.domain.models.currencyapi.rate.rates.AWG
import com.google.gson.annotations.SerializedName


data class Rates(

    @SerializedName("data") var data: Map<String, AWG> = emptyMap()

)