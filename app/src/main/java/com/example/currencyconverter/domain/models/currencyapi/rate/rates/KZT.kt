package com.example.currencyconverter.domain.models.currencyapi.rate.rates

import com.google.gson.annotations.SerializedName


data class KZT (

  @SerializedName("code"  ) var code  : String? = null,
  @SerializedName("value" ) var value : Int?    = null

)