package com.example.currencyconverter.presentation.converter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.R
import com.example.currencyconverter.data.database.Currency
import com.google.android.material.snackbar.Snackbar

class CurrencyListAdapter(
    val viewModel: CurrencyViewModel,
    val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<Currency, CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder {
        this.parent = parent
        return CurrenciesItemViewHolder.inflateFrom(parent, this)
    }

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, holder.itemView.context)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        viewModel.moveCurrencies(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int, view: View) {
        val deletedCurrency = getItem(position)
        viewModel.delete(deletedCurrency)

        Snackbar.make(view, "Currency deleted!", Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.bottom_nav)
            .setAction("Undo") {
                notifyItemInserted(itemCount)
                viewModel.addCurrency(deletedCurrency)
            }.show()
    }
}