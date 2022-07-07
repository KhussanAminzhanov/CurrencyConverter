package com.example.currencyconverter.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.data.database.CurrencyTransaction
import com.example.currencyconverter.databinding.ItemTransactionBinding

class CurrencyTransactionViewHolder(val binding: ItemTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CurrencyTransaction) {
        binding.tvValue.text = item.value.toString()
        binding.tvCurrencyName.text = item.name
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): CurrencyTransactionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTransactionBinding.inflate(inflater, parent, false)
            return CurrencyTransactionViewHolder(binding)
        }
    }
}