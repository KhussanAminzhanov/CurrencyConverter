package com.example.currencyconverter.domain.models.rate.rates

import com.google.gson.annotations.SerializedName


data class RUB (

  @SerializedName("code"  ) var code  : String? = null,
  @SerializedName("value" ) var value : Double? = null

)