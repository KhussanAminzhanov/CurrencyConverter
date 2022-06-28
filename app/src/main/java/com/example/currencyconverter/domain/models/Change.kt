package com.example.currencyconverter.domain.models

import com.google.gson.annotations.SerializedName

data class Change(

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("change") var change: Boolean? = null,
    @SerializedName("start_date") var startDate: String? = null,
    @SerializedName("end_date") var endDate: String? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("quotes") var quotes: Quotes = Quotes()

)