package com.example.currencyconverter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter

class CurrenciesAdapter(
    val viewModel: CurrenciesViewModel,
    val viewLifecycleOwner: LifecycleOwner
) :
    ListAdapter<CurrencyItem, CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    lateinit var mContext: Context
    var selectedCurrencyItemPosition = -1

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
        viewModel.deleteCurrency(getItem(position).currencyId, view)
        notifyItemRemoved(position)
    }

    fun showAlertDialog() {
        val customView =
            LayoutInflater.from(mContext)
                .inflate(R.layout.delete_currency_alert_dialog_layout, null)
        val dialog = AlertDialog.Builder(mContext).setView(customView).create()

        with(customView) {
            findViewById<Button>(R.id.alert_dialog_cancel_button).setOnClickListener { dialog.dismiss() }
            findViewById<Button>(R.id.alert_dialog_delete_button).setOnClickListener {
                viewModel.deleteCurrency(getItem(selectedCurrencyItemPosition).currencyId, this)
                notifyItemRemoved(selectedCurrencyItemPosition)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}