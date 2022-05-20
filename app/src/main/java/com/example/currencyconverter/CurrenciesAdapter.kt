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
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.CurrencyItemBinding
import com.google.android.material.snackbar.Snackbar


class CurrenciesAdapter(
    private val viewModel: CurrenciesViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) :
    ListAdapter<CurrencyItem, CurrenciesAdapter.CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    private lateinit var mContext: Context
    private var selectedCurrencyItemPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder {
        mContext = parent.context
        return CurrenciesItemViewHolder.inflateFrom(parent)
    }

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, holder.itemView.context)

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
            if (viewModel.itemSelected.value != true) viewModel.isItemSelected(true)
            selectedCurrencyItemPosition = holder.bindingAdapterPosition
            true
        }

        viewModel.itemSelected.observe(viewLifecycleOwner) { itemSelected ->
            holder.binding.currencyLayout.isLongClickable = !itemSelected
        }
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
        viewModel.deleteCurrency(getItem(position).currencyId)
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
                viewModel.deleteCurrency(getItem(selectedCurrencyItemPosition).currencyId)
                notifyItemRemoved(selectedCurrencyItemPosition)
                dialog.dismiss()
                viewModel.isItemSelected(false)
            }
        }
        dialog.show()

    }

}