package com.example.currencyconverter.presentation.converter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.data.database.Currency
import com.example.currencyconverter.databinding.ItemCurrencyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrenciesItemViewHolder(
    private val binding: ItemCurrencyBinding,
    private val balance: LiveData<Double>,
    private val isItemSelected: LiveData<Boolean>,
    private val onItemLongClick: () -> Unit,
    private val onItemCheck: (currency: Currency) -> Unit,
    private val onItemUncheck: (currency: Currency) -> Unit,
    private val isItemChecked: (currency: Currency) -> Boolean

) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: Currency

    companion object {
        fun inflateFrom(
            parent: ViewGroup,
            balance: LiveData<Double>,
            isItemSelected: LiveData<Boolean>,
            onLongClick: () -> Unit,
            onItemCheck: (currency: Currency) -> Unit,
            onItemUncheck: (currency: Currency) -> Unit,
            isItemChecked: (currency: Currency) -> Boolean,
        ): CurrenciesItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCurrencyBinding.inflate(layoutInflater, parent, false)
            return CurrenciesItemViewHolder(
                binding = binding,
                balance = balance,
                isItemSelected = isItemSelected,
                onItemLongClick = onLongClick,
                onItemCheck = onItemCheck,
                onItemUncheck = onItemUncheck,
                isItemChecked = isItemChecked
            )
        }
    }

    fun bind(item: Currency) {
        this.item = item
        setupLayoutData()
        setupObservers()
        setupCheckbox()
    }

    private fun setupLayoutData() {
        binding.tvTicket.text = item.name.split(" ").last()
        binding.tvName.text = item.name.slice(0..(item.name.length - 4))
        binding.currencyLayout.setOnLongClickListener { onItemLongClick(); true }
        binding.edtCurrencyValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                if (s.isEmpty()) s.append('0')
                else s.trimStart('0')
            }
        })
    }

    private fun setupObservers() {
        balance.observe(itemView.context as LifecycleOwner) { newValue ->
            CoroutineScope(Dispatchers.Main).launch {
                val newValueFormatted = "%.4f".format(newValue * item.exchangeRate)
                binding.edtCurrencyValue.setText(newValueFormatted)
            }
        }

        isItemSelected.observe(itemView.context as LifecycleOwner) { itemSelected ->
            binding.currencyLayout.isLongClickable = !itemSelected
            if (itemSelected) showCheckbox() else hideCheckbox()
        }
    }

    private fun setupCheckbox() {
        binding.chbItemChecked.isChecked = isItemChecked(item)
        binding.chbItemChecked.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) onItemCheck(item) else onItemUncheck(item)
        }
    }

    private fun showCheckbox() {
        binding.chbItemChecked.visibility = View.VISIBLE
        binding.chbItemChecked.isChecked = false
    }

    private fun hideCheckbox() {
        binding.chbItemChecked.visibility = View.GONE
    }
}