package com.example.currencyconverter.presentation.chat

import androidx.recyclerview.widget.DiffUtil
import com.example.currencyconverter.data.database.CurrencyTransaction

class TransactionDiffUtilItemCallback : DiffUtil.ItemCallback<CurrencyTransaction>() {
    override fun areItemsTheSame(
        oldItem: CurrencyTransaction,
        newItem: CurrencyTransaction
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: CurrencyTransaction,
        newItem: CurrencyTransaction
    ) = oldItem == newItem
}