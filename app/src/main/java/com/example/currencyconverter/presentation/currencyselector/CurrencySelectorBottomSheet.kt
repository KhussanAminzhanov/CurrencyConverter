package com.example.currencyconverter.presentation.currencyselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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