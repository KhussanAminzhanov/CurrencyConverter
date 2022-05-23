package com.example.currencyconverter

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter

class CurrenciesAdapter(
    private val viewModel: CurrenciesViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) :
    ListAdapter<CurrencyItem, CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    private lateinit var mContext: Context
    private var selectedCurrencyItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder {
        Log.i("adapter", "onCreateViewHolder")

        mContext = parent.context
        return CurrenciesItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, holder.itemView.context)
        Log.i("adapter", "onBindViewHolder")

        holder.binding.currencyValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return

                var newValue = 0L
                if (s.isNotEmpty()) {
                    newValue = s.toString().toLong()
                }
                viewModel.changeCurrencyData(holder.bindingAdapterPosition, newValue)
            }
        })

        holder.binding.currencyLayout.setOnLongClickListener {
            if (viewModel.isItemSelected.value != true) viewModel.itemSelected(true)
            selectedCurrencyItemPosition = holder.bindingAdapterPosition
            true
        }

        viewModel.isItemSelected.observe(viewLifecycleOwner) { itemSelected ->
            holder.binding.currencyLayout.isLongClickable = !itemSelected
            holder.binding.markDeleteCheckbox.visibility =
                if (itemSelected) View.VISIBLE else View.GONE
        }
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