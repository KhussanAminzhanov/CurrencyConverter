package com.example.currencyconverter

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.CurrencyItemBinding

class CurrenciesAdapter(
    private val viewModel: CurrenciesViewModel
) :
    ListAdapter<CurrencyItem, CurrenciesAdapter.CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder =
        CurrenciesItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, holder.itemView.context)

        holder.binding.currencyValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                var newValue = 0L
                if (s!!.isNotEmpty() and (Long.MAX_VALUE.toString().length > s.toString().length)) newValue = s.toString().toLong()
                viewModel.changeCurrencyData(holder.bindingAdapterPosition, newValue)
            }
        })

        holder.binding.currencyLayout.setOnLongClickListener {
            showAlertDialog(holder.bindingAdapterPosition, holder.itemView)
            true
        }

        holder.itemView.context
    }

    class CurrenciesItemViewHolder(val binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): CurrenciesItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = CurrencyItemBinding.inflate(layoutInflater, parent, false)
                return CurrenciesItemViewHolder(view)
            }
        }

        fun bind(item: CurrencyItem, context: Context) {
            binding.currencyValueEditText.setText(item.value.toString())
            binding.currencyValueTextInputLayout.hint = item.name
            binding.currencyName.text = item.name
            binding.currencyFlagImage.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    item.image
                )
            )
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        viewModel.moveCurrencies(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int, view: View) {
        viewModel.deleteCurrency(position, view)
        notifyItemRemoved(position)
    }

    private fun showAlertDialog(position: Int, view: View) {
        val customView =
            LayoutInflater.from(view.context).inflate(R.layout.delete_currency_alert_dialog_layout, null)
        val dialog = AlertDialog.Builder(view.context).setView(customView).create()

        with(customView) {
            findViewById<Button>(R.id.alert_dialog_cancel_button).setOnClickListener { dialog.dismiss() }
            findViewById<Button>(R.id.alert_dialog_delete_button).setOnClickListener {
                viewModel.deleteCurrency(position, view)
                notifyItemRemoved(position)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}