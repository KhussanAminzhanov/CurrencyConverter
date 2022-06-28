package com.example.currencyconverter.ui.currencyselector

import android.os.Bundle
import android.util.Log
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

        viewModel.currencyQuotes.observe(viewLifecycleOwner) { currencies ->
            val list = currencies::class.memberProperties.map { member ->
                Log.i("bottom_sheet", "|${member.call(currencies)}|")
                "${member.name} ${member.call(currencies)}"
            }
            list.forEachIndexed { index, s ->
                data.add(CurrencyItem(index, s, R.drawable.flag_usa, 1.0))
            }
            adapter.submitList(data)
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