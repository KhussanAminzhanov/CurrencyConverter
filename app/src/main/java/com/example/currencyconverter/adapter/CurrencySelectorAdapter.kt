package com.example.currencyconverter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.database.CurrencyItem
import com.example.currencyconverter.ui.currency.currencyselector.CurrencySelectorViewHolder

class CurrencySelectorAdapter(
    private val data: List<CurrencyItem>,
    private val onItemClick: (CurrencyItem) -> Unit
) : RecyclerView.Adapter<CurrencySelectorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencySelectorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency_name, parent, false)
        return CurrencySelectorViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: CurrencySelectorViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}