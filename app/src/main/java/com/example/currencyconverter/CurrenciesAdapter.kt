package com.example.currencyconverter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.snackbar.Snackbar

class CurrenciesAdapter(
    val viewModel: CurrenciesViewModel,
    val viewLifecycleOwner: LifecycleOwner
) :
    ListAdapter<CurrencyItem, CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    lateinit var mContext: Context
    val checkedCurrencyPositions = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder {
        mContext = parent.context
        return CurrenciesItemViewHolder.inflateFrom(parent,this)
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
        val deletedCurrency = viewModel.deleteCurrency(position)
        notifyItemRemoved(position)

        Snackbar.make(view, "Currency deleted!", Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.add_currency_button)
            .setAction("Undo") {
                notifyItemInserted(itemCount)
                viewModel.addCurrency(deletedCurrency)
            }.show()
    }

    fun deleteSelectedCurrencies() {
        val customView =
            LayoutInflater.from(mContext)
                .inflate(R.layout.delete_currency_alert_dialog_layout, null)
        val dialog = AlertDialog.Builder(mContext).setView(customView).create()

        with(customView) {
            findViewById<Button>(R.id.alert_dialog_cancel_button).setOnClickListener { dialog.dismiss() }
            findViewById<Button>(R.id.alert_dialog_delete_button).setOnClickListener {
                val deletedCurrencies = viewModel.deleteCurrencies(checkedCurrencyPositions)
                checkedCurrencyPositions.forEach { position -> notifyItemRemoved(position) }
                checkedCurrencyPositions.clear()
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}