package com.example.currencyconverter.presentation.currencyselector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.R
import com.example.currencyconverter.data.database.Currency
import com.example.currencyconverter.presentation.converter.CurrencyDiffItemCallback

class CurrencySelectorAdapter(
    private val onItemClick: (Currency) -> Unit
) : ListAdapter<Currency, CurrencySelectorViewHolder>(CurrencyDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencySelectorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency_name, parent, false)
        return CurrencySelectorViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: CurrencySelectorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}