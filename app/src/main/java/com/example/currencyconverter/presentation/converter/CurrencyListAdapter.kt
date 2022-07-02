package com.example.currencyconverter.presentation.converter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.R
import com.example.currencyconverter.data.database.Currency
import com.google.android.material.snackbar.Snackbar

class CurrencyListAdapter(
    val viewModel: CurrencyViewModel
) : ListAdapter<Currency, CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CurrenciesItemViewHolder.inflateFrom(parent, this)

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        viewModel.moveCurrencies(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int, view: View) {
        val deletedCurrency = getItem(position)
        viewModel.deleteCurrency(deletedCurrency)

        Snackbar.make(view, "Currency deleted!", Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.bottom_nav)
            .setAction("Undo") {
                notifyItemInserted(itemCount)
                viewModel.addCurrency(deletedCurrency)
            }.show()
    }
}