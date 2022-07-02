package com.example.currencyconverter.domain.models.rate.rates

import com.google.gson.annotations.SerializedName


data class JMD (

  @SerializedName("code"  ) var code  : String? = null,
  @SerializedName("value" ) var value : Double? = null

)