package com.example.currencyconverter.ui.currencyselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.adapter.CurrencySelectorAdapter
import com.example.currencyconverter.database.Currency
import com.example.currencyconverter.database.CurrencyQuote
import com.example.currencyconverter.database.asCurrency
import com.example.currencyconverter.databinding.BottomSheetAddCurrencyBinding
import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencySelectorBottomSheet(
    private val viewModel: CurrencyViewModel
) : BottomSheetDialogFragment() {

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
        requireActivity()
        setupRecyclerView()
        setupObservers()

        binding.btnCancel.setOnClickListener { this.dismiss() }
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = CurrencySelectorAdapter { currencyItem ->
            viewModel.addCurrency(currencyItem)
            this.dismiss()
        }
        adapter.submitList(data)
        binding.recyclerViewCurrenciesNames.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.repository.currencyNames.observe(viewLifecycleOwner) {
            viewModel.refreshCurrencyRates()
        }

        viewModel.repository.currencyRates.observe(viewLifecycleOwner) {
            val currencyNames = viewModel.repository.currencyNames.value
            val currencyRates = viewModel.repository.currencyRates.value
            if (currencyNames != null && currencyRates != null) {
                val currencyQuotes = currencyNames.map {
                    val ticket = it.key
                    val name = it.value
                    val rate = currencyRates["KZT$ticket"] ?: 1.0
                    CurrencyQuote(name = "$name $ticket", exchangeRate = rate)
                }
                viewModel.refreshCurrencyQuotes(currencyQuotes)
            }
        }

        viewModel.repository.currencyQuotesList.observe(viewLifecycleOwner) {
            val currencyList = it.map { currencyQuote ->  currencyQuote.asCurrency() }
            adapter.submitList(currencyList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CurrencySelectorBottomSheet"
    }
}