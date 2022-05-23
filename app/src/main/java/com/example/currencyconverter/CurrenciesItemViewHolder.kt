package com.example.currencyconverter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.CurrencyItemBinding

class CurrenciesItemViewHolder(
    private val binding: CurrencyItemBinding,
    private val adapter: CurrenciesAdapter
) : RecyclerView.ViewHolder(binding.root) {

    private val viewModel by lazy { adapter.viewModel }
    private val checkBox = CheckBox(adapter.mContext)

    companion object {
        fun inflateFrom(
            parent: ViewGroup,
            adapter: CurrenciesAdapter
        ): CurrenciesItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = CurrencyItemBinding.inflate(layoutInflater, parent, false)
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
                viewModel.updateCurrencyData(bindingAdapterPosition, newValue)
            }
        })

        binding.currencyLayout.setOnLongClickListener {
            if (viewModel.isItemSelected.value != true) viewModel.setItemSelected(true)
            true
        }

        viewModel.isItemSelected.observe(adapter.viewLifecycleOwner) { itemSelected ->
            binding.currencyLayout.isLongClickable = !itemSelected
            if (itemSelected) addCheckbox() else removeCheckbox()
        }
    }

    private fun addCheckbox() {
        binding.innerLinearLayout.addView(checkBox)
    }

    private fun removeCheckbox() {
        binding.innerLinearLayout.removeView(checkBox)
    }
}