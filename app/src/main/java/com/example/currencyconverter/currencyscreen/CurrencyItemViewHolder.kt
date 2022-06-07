package com.example.currencyconverter.currencyscreen

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.database.CurrenciesData
import com.example.currencyconverter.database.CurrencyItem
import com.example.currencyconverter.databinding.ItemCurrencyBinding

const val TAG_VIEW_HOLDER = "view_holder"

class CurrenciesItemViewHolder(
    private val binding: ItemCurrencyBinding,
    private val adapter: CurrenciesAdapter
) : RecyclerView.ViewHolder(binding.root) {

    private val viewModel by lazy { adapter.viewModel }
    private val checkBox = CheckBox(adapter.getContext())
    private var hasParent = false

    companion object {
        fun inflateFrom(
            parent: ViewGroup,
            adapter: CurrenciesAdapter
        ): CurrenciesItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = ItemCurrencyBinding.inflate(layoutInflater, parent, false)
            return CurrenciesItemViewHolder(view, adapter)
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

        binding.currencyValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                var newValue = 0L
                if (s.isNotEmpty()) newValue = s.toString().toLong()
                CurrenciesData.updateCurrencyData(bindingAdapterPosition, newValue)
            }
        })

        binding.currencyLayout.setOnLongClickListener {
            viewModel.setItemSelected(true)
            true
        }

        viewModel.isItemSelected.observe(adapter.viewLifecycleOwner) { itemSelected ->
            Log.i(TAG_VIEW_HOLDER, "isItemSelected observer called, itemSelected = $itemSelected")
            binding.currencyLayout.isLongClickable = !itemSelected
            binding.currencyLayout
            if (itemSelected) addCheckbox() else removeCheckbox()
        }

        checkBox.isChecked = isCheckboxChecked()
        checkBox.setOnClickListener {
            if (checkBox.isChecked) {
                adapter.checkedCurrencyPositions.add(absoluteAdapterPosition)
            } else {
                adapter.checkedCurrencyPositions.remove(absoluteAdapterPosition)
            }
        }
    }

    private fun addCheckbox() {
        Log.i(TAG_VIEW_HOLDER, "addCheckbox called")
        if (hasParent) return
        checkBox.isChecked = false
        binding.innerLinearLayout.addView(checkBox)
        hasParent = true
    }

    private fun removeCheckbox() {
        Log.i(TAG_VIEW_HOLDER, "removeCheckbox called")
        if (!hasParent) return
        binding.innerLinearLayout.removeView(checkBox)
        hasParent = false
    }

    private fun isCheckboxChecked(): Boolean {
        return adapter.checkedCurrencyPositions.contains(absoluteAdapterPosition)
    }
}