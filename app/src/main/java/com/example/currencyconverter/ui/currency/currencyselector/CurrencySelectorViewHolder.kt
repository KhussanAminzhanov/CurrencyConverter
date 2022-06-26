package com.example.currencyconverter.ui.currency.currencyselector

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.database.CurrencyItem

class CurrencySelectorViewHolder(view: View, val onClick: (CurrencyItem) -> Unit) : RecyclerView.ViewHolder(view) {
    private val currencyName: TextView

    init {
        currencyName = view.findViewById(R.id.text_view_currency_name)
    }

    fun bind(currencyItem: CurrencyItem) {
        currencyName.text = currencyItem.name
        currencyName.setOnClickListener {
            onClick(currencyItem)
        }
    }
}