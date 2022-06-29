package com.example.currencyconverter.network

import com.google.gson.annotations.SerializedName


data class KZTMWK (

  @SerializedName("start_rate" ) var startRate : Double? = null,
  @SerializedName("end_rate"   ) var endRate   : Double? = null,
  @SerializedName("change"     ) var change    : Int?    = null,
  @SerializedName("change_pct" ) var changePct : Int?    = null

)