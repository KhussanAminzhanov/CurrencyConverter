package com.example.currencyconverter.ui.currency

import androidx.recyclerview.widget.DiffUtil
import com.example.currencyconverter.database.CurrencyItem

class CurrencyDiffItemCallback : DiffUtil.ItemCallback<CurrencyItem>() {
    override fun areItemsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem) =
        oldItem.currencyId == newItem.currencyId

    override fun areContentsTheSame(oldItem: CurrencyItem, newItem: CurrencyItem) = oldItem == newItem
}