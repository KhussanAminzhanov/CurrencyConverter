package com.example.currencyconverter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout

class CurrenciesAdapter : RecyclerView.Adapter<CurrenciesAdapter.CurrenciesItemViewHolder>() {

    var data = listOf<CurrencyItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder = CurrenciesItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    class CurrenciesItemViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {

        companion object {
            fun inflateFrom(parent: ViewGroup): CurrenciesItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.currency_item, parent, false) as LinearLayoutCompat
                return CurrenciesItemViewHolder(view)
            }
        }

        fun bind(item: CurrencyItem) {
            rootView.findViewById<TextInputLayout>(R.id.currency_value_text_input_layout).hint = item.name
        }
    }
}