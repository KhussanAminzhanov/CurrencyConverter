package com.example.currencyconverter.ui.currencyselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.adapter.CurrencySelectorAdapter
import com.example.currencyconverter.database.Currency
import com.example.currencyconverter.database.asCurrency
import com.example.currencyconverter.databinding.BottomSheetAddCurrencyBinding
import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencySelectorBottomSheet(val viewModel: CurrencyViewModel) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddCurrencyBinding? = null
    private val binding get() = _binding!!
    private var data = mutableListOf<Currency>()

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

        viewModel.repository.currencyQuotesList.observe(viewLifecycleOwner) { currencies ->
            adapter.submitList(currencies.map { it.asCurrency() })
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