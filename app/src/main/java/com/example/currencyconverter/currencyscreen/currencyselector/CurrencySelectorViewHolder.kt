package com.example.currencyconverter.currencyscreen.currencyselector

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R

class CurrencySelectorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val currencyName: TextView

    init {
        currencyName = view.findViewById(R.id.text_view_currency_name)
    }
}