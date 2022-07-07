package com.example.currencyconverter.presentation.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.data.database.Transaction

class HistoryListAdapter(
    private val data: List<Transaction>,
) : RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransactionViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size
}