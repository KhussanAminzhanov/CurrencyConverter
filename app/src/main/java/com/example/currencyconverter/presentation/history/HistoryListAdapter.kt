package com.example.currencyconverter.presentation.history

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.data.database.CurrencyTransaction
import com.example.currencyconverter.presentation.chat.TransactionDiffUtilItemCallback

class HistoryListAdapter(
    private val data: List<CurrencyTransaction>,
) : ListAdapter<CurrencyTransaction, CurrencyTransactionViewHolder>(TransactionDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyTransactionViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CurrencyTransactionViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }
}