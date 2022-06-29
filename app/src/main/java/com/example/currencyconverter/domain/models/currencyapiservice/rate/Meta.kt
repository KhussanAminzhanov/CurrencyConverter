package com.example.currencyconverter.domain.models.currencyapiservice.rate

import com.google.gson.annotations.SerializedName


data class Meta (

  @SerializedName("last_updated_at" ) var lastUpdatedAt : String? = null

)