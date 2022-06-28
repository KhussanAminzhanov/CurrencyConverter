package com.example.currencyconverter.ui.currencyselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.R
import com.example.currencyconverter.adapter.CurrencySelectorAdapter
import com.example.currencyconverter.database.CurrencyItem
import com.example.currencyconverter.databinding.BottomSheetAddCurrencyBinding
import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.reflect.full.memberProperties

class CurrencySelectorBottomSheet(val viewModel: CurrencyViewModel) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddCurrencyBinding? = null
    private val binding get() = _binding!!
    private var data = mutableListOf<CurrencyItem>()

    private lateinit var adapter: CurrencySelectorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddCurrencyBinding.inflate(inflater, container, false)
        binding.btnCancel.setOnClickListener { this.dismiss() }

        adapter = CurrencySelectorAdapter { currencyItem ->
            viewModel.addCurrency(currencyItem)
            this.dismiss()
        }

        adapter.submitList(data)
        binding.recyclerViewCurrenciesNames.adapter = adapter

        viewModel.currencyQuotesNames.observe(viewLifecycleOwner) { currencies ->
            val list = currencies::class.memberProperties.map { member ->
                val ticket = member.name
                val name = member.call(currencies)
                val change = viewModel.quotes.value?.get("KZT$ticket") ?: 1.0
                CurrencyItem(name = "$ticket $name", image = R.drawable.flag_usa, exchangeRate = change)
            }
            adapter.submitList(list)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CurrencySelectorBottomSheet"
    }
}