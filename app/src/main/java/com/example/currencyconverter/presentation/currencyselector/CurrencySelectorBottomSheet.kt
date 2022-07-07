package com.example.currencyconverter.presentation.currencyselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.R
import com.example.currencyconverter.data.database.Currency
import com.example.currencyconverter.data.database.asCurrency
import com.example.currencyconverter.databinding.BottomSheetAddCurrencyBinding
import com.example.currencyconverter.presentation.converter.CurrencyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CurrencySelectorBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: CurrencyViewModel by sharedViewModel()
    private var _binding: BottomSheetAddCurrencyBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CurrencySelectorAdapter

//    init {
//        viewModel.refreshCurrencyQuotes()
//    }

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
        binding.recyclerViewCurrenciesNames.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.currencyQuotes.observe(viewLifecycleOwner) {
            val currencyList = it.map { currencyQuote -> currencyQuote.asCurrency() }
            if (currencyList.isEmpty()) adapter.submitList(getCurrenciesList())
            else adapter.submitList(currencyList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrenciesList(): List<Currency> {
        val currencyUSD = Currency( name = "US Dollar USD", image = R.drawable.flag_usa, exchangeRate = 0.0023)
        val currencyTRY = Currency( name = "Turkish Lira TRY", image = R.drawable.flag_turkey, exchangeRate = 0.04)
        return listOf(currencyUSD, currencyTRY)
    }

    companion object {
        const val TAG = "CurrencySelectorBottomSheet"
    }
}