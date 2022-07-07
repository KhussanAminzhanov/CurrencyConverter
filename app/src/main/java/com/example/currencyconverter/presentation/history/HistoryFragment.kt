package com.example.currencyconverter.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.databinding.FragmentHistoryBinding
import com.example.currencyconverter.presentation.chat.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModel()

    private lateinit var adapter: HistoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater)

        binding.rvNews.adapter = adapter

        return binding.root
    }

    private fun setupObservers() {
        viewModel.currencyTransactions.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}