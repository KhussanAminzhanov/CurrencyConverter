package com.example.currencyconverter.domain.models.currencyapi.rate

import com.google.gson.annotations.SerializedName


data class Rates (

  @SerializedName("data" ) var data : Data = Data()

)