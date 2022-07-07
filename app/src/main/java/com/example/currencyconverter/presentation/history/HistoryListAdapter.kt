package com.example.currencyconverter.presentation.history

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.data.database.CurrencyTransaction
import com.example.currencyconverter.presentation.converter.CustomItemTouchHelperAdapter

class HistoryListAdapter(
    private val onTransactionDismiss: (transaction: CurrencyTransaction) -> Unit,
    private val onTransactionMove: (from: Int, to: Int) -> Unit,
) : ListAdapter<CurrencyTransaction, CurrencyTransactionViewHolder>(TransactionDiffUtilItemCallback()),
    CustomItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrencyTransactionViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CurrencyTransactionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.binding.ibtnDelete.setOnClickListener { onTransactionDismiss(getItem(position)) }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        onTransactionMove(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        val transaction = getItem(position)
        this.onTransactionDismiss(transaction)
    }
}