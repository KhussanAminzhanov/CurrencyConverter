package com.example.currencyconverter.presentation.converter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.data.database.Currency
import com.example.currencyconverter.databinding.ItemCurrencyBinding
import kotlinx.coroutines.launch

class CurrenciesItemViewHolder(
    private val binding: ItemCurrencyBinding,
    private val adapter: CurrencyListAdapter
) : RecyclerView.ViewHolder(binding.root) {

    private val viewModel by lazy { adapter.viewModel }
    private val checkBox = CheckBox(adapter.parent.context)
    private var checkboxHasParent = false

    companion object {
        fun inflateFrom(
            parent: ViewGroup,
            adapter: CurrencyListAdapter
        ): CurrenciesItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = ItemCurrencyBinding.inflate(layoutInflater, parent, false)
            return CurrenciesItemViewHolder(view, adapter)
        }
    }

    fun bind(item: Currency, context: Context) {
        binding.currencyValueTextInputLayout.hint = item.name
        binding.currencyFlagImage.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                item.image
            )
        )

        binding.currencyLayout.setOnLongClickListener {
            viewModel.setItemSelected(true)
            true
        }

        viewModel.balance.observe(adapter.viewLifecycleOwner) { newValue ->
            viewModel.viewModelScope.launch {
                val newValueFormatted = "%.4f".format(newValue * item.exchangeRate)
                binding.currencyValueEditText.setText(newValueFormatted)
            }
        }

        viewModel.isItemSelected.observe(adapter.viewLifecycleOwner) { itemSelected ->
            binding.currencyLayout.isLongClickable = !itemSelected
            binding.currencyLayout
            if (itemSelected) addCheckbox() else removeCheckbox()
        }

        checkBox.isChecked = isCheckboxChecked(item)
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                adapter.viewModel.addToCheckedCurrencies(item)
            } else {
                adapter.viewModel.removeCurrencyFromCheckedCurrencies(item)
            }
        }
    }

    private fun addCheckbox() {
        if (checkboxHasParent) return
        binding.innerLinearLayout.addView(checkBox)
        checkboxHasParent = true
        checkBox.isChecked = false
    }

    private fun removeCheckbox() {
        if (!checkboxHasParent) return
        binding.innerLinearLayout.removeView(checkBox)
        checkboxHasParent = false
    }

    private fun isCheckboxChecked(currency: Currency): Boolean {
        return adapter.viewModel.checkedCurrenciesContains(currency)
    }
}