package com.example.currencyconverter.domain.models.currencyapi.rate.rates

import com.google.gson.annotations.SerializedName


data class CDF (

  @SerializedName("code"  ) var code  : String? = null,
  @SerializedName("value" ) var value : Double? = null

)