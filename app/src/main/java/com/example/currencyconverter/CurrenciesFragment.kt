package com.example.currencyconverter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyconverter.databinding.FragmentCurrenciesBinding

class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val currencies = mutableListOf(
        CurrencyItem("Tenge", R.drawable.kazakhstan_flag, 0L),
        CurrencyItem("Dollar", R.drawable.usa_flag, 0L)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapter = CurrenciesAdapter()
        adapter.data = currencies
        binding.currenciesListRecyclerView.adapter = adapter

        binding.addCurrencyButton.setOnClickListener {
            currencies.add(CurrencyItem("Lira", R.drawable.turkey_flag, 0L))
            adapter.data = currencies
            binding.currenciesListRecyclerView.layoutManager?.scrollToPosition(adapter.data.size - 1)
        }

        return view
    }

}