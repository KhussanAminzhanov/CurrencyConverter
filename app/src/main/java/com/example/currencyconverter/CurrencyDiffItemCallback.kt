package com.example.currencyconverter

import androidx.recyclerview.widget.DiffUtil

class CurrencyDiffItemCallback : DiffUtil.ItemCallback<CurrencyItem>() {
    override fun areItemsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem) =
        oldItem.currencyId == newItem.currencyId

    override fun areContentsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem) = oldItem == newItem
}