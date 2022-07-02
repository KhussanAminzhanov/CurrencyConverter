package com.example.currencyconverter.presentation.converter

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.data.database.Currency

class CurrencyListAdapter(
    private val balance: LiveData<Double>,
    private val isItemSelected: LiveData<Boolean>,
    private val onItemMoveCallback: (from: Int, to: Int) -> Unit,
    private val onItemDismissCallback: (currency: Currency) -> Unit,
    private val onItemLongClick: () -> Unit,
    private val onItemCheck: (currency: Currency) -> Unit,
    private val onItemUncheck: (currency: Currency) -> Unit,
    private val isItemChecked: (currency: Currency) -> Boolean
) : ListAdapter<Currency, CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrenciesItemViewHolder.inflateFrom(
            parent = parent,
            balance = balance,
            isItemSelected = isItemSelected,
            onLongClick = onItemLongClick,
            onItemCheck = onItemCheck,
            onItemUncheck = onItemUncheck,
            isItemChecked = isItemChecked,
        )

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        onItemMoveCallback(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        val deletedCurrency = getItem(position)
        onItemDismissCallback(deletedCurrency)
    }
}