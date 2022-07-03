package com.example.currencyconverter.presentation.converter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
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

    private val checkBox = CheckBox(itemView.context)
    private var checkboxHasParent = false
    private lateinit var item: Currency

    companion object {
        fun inflateFrom(
            parent: ViewGroup,
            onLongClick: () -> Unit,
            onItemCheck: (currency: Currency) -> Unit,
            onItemUncheck: (currency: Currency) -> Unit,
            isItemChecked: (currency: Currency) -> Boolean,
            balance: LiveData<Double>,
            isItemSelected: LiveData<Boolean>
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
        binding.currencyValueTextInputLayout.hint = item.name
        binding.currencyFlagImage.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                item.image
            )
        )
        binding.currencyLayout.setOnLongClickListener { onItemLongClick(); true }
    }

    private fun setupObservers() {
        balance.observe(itemView.context as LifecycleOwner) { newValue ->
            CoroutineScope(Dispatchers.Main).launch {
                val newValueFormatted = "%.4f".format(newValue * item.exchangeRate)
                binding.currencyValueEditText.setText(newValueFormatted)
            }
        }

        isItemSelected.observe(itemView.context as LifecycleOwner) { itemSelected ->
            binding.currencyLayout.isLongClickable = !itemSelected
            if (itemSelected) addCheckbox() else removeCheckbox()
        }
    }

    private fun setupCheckbox() {
        checkBox.isChecked = isItemChecked(item)
        checkBox.setOnClickListener {
            if (checkBox.isChecked) onItemCheck(item) else onItemUncheck(item)
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
}