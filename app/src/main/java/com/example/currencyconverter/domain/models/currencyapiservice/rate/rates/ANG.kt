package com.example.currencyconverter.domain.models.currencyapiservice.rate.rates

import com.google.gson.annotations.SerializedName


data class ANG (

  @SerializedName("code"  ) var code  : String? = null,
  @SerializedName("value" ) var value : Double? = null

)