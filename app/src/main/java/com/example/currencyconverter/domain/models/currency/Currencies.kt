package com.example.currencyconverter.domain.models.currency

import com.example.example.Data
import com.google.gson.annotations.SerializedName


data class Currencies (

  @SerializedName("data" ) var data : Data = Data()

)