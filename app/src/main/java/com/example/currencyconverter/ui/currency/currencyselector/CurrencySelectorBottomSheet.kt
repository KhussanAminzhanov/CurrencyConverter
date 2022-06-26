package com.example.currencyconverter.ui.currency.currencyselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.adapter.CurrencySelectorAdapter
import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.example.currencyconverter.database.CurrenciesData
import com.example.currencyconverter.databinding.BottomSheetAddCurrencyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencySelectorBottomSheet(val viewModel: CurrencyViewModel) : BottomSheetDialogFragment() {

    private val data = CurrenciesData.getCurrenciesList()
    private var _binding: BottomSheetAddCurrencyBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CurrencySelectorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddCurrencyBinding.inflate(inflater, container, false)
        binding.btnCancel.setOnClickListener { this.dismiss() }
        adapter = CurrencySelectorAdapter(data) { currencyItem ->
            viewModel.addCurrency(currencyItem)
            this.dismiss()
        }
        binding.recyclerViewCurrenciesNames.adapter = adapter
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