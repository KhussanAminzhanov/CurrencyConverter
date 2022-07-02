package com.example.currencyconverter.presentation.converter

import androidx.recyclerview.widget.DiffUtil
import com.example.currencyconverter.data.database.Currency

class CurrencyDiffItemCallback : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
        oldItem.currencyId == newItem.currencyId

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem
}