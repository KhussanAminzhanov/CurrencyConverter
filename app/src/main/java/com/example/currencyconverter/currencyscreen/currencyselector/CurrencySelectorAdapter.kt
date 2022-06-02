package com.example.currencyconverter.currencyscreen.currencyselector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R

class CurrencySelectorAdapter(private val data: List<String>) : RecyclerView.Adapter<CurrencySelectorViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencySelectorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency_name, parent, false)
        return CurrencySelectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencySelectorViewHolder, position: Int) {
        holder.currencyName.text = data[position]
    }

    override fun getItemCount() = data.size
}