package com.example.currencyconverter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.CurrencyItemBinding
import com.google.android.material.internal.TextWatcherAdapter

class CurrenciesAdapter(private val viewModel: CurrenciesViewModel) :
    ListAdapter<CurrencyItem, CurrenciesAdapter.CurrenciesItemViewHolder>(CurrencyDiffItemCallback()), CurrencyItemTouchHelperAdapter {

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
                if (s!!.isNotEmpty()) {
                    newValue = s.toString().toLong()
                }
                viewModel.changeCurrencyData(holder.bindingAdapterPosition, newValue)
            }
        })
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

    override fun onItemDismiss(position: Int) {
        viewModel.deleteCurrency(position)
        notifyItemRemoved(position)
    }
}