package com.example.currencyconverter.currencyscreen.currencyselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.databinding.BottomSheetAddCurrencyBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CurrencySelectorBottomSheet() : BottomSheetDialogFragment() {

    private val data = listOf<String>()

    private var _binding: BottomSheetAddCurrencyBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { CurrencySelectorAdapter(data) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddCurrencyBinding.inflate(inflater, container, false)

        binding.btnCancel.setOnClickListener { this.dialog?.dismiss() }

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