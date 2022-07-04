package com.example.currencyconverter.domain.models.currencyapi.rate.rates

import com.google.gson.annotations.SerializedName


data class BWP (

  @SerializedName("code"  ) var code  : String? = null,
  @SerializedName("value" ) var value : Double? = null

)