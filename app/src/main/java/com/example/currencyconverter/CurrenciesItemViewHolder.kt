package com.example.currencyconverter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.CurrencyItemBinding

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
        binding.markDeleteCheckbox.isChecked = false
        binding.currencyFlagImage.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                item.image
            )
        )
    }
}