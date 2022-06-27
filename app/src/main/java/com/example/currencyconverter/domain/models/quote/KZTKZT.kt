package com.example.currencyconverter.network

import com.google.gson.annotations.SerializedName


data class KZTKZT (

  @SerializedName("start_rate" ) var startRate : Int? = null,
  @SerializedName("end_rate"   ) var endRate   : Int? = null,
  @SerializedName("change"     ) var change    : Int? = null,
  @SerializedName("change_pct" ) var changePct : Int? = null

)