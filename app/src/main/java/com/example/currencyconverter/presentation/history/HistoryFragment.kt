package com.example.currencyconverter.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.currencyconverter.data.database.CurrencyTransaction
import com.example.currencyconverter.databinding.FragmentHistoryBinding
import com.example.currencyconverter.presentation.converter.CustomItemTouchHelperCallback
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    val binding get() = _binding!!

    private val viewModel: HistoryViewModel by sharedViewModel()

    private lateinit var adapter: HistoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater)
        setupRecyclerView()
        setupObservers()
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = HistoryListAdapter(
            ::onTransactionDismiss,
            ::onTransactionMove,
        )
        val callback = CustomItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        binding.rvNews.adapter = adapter
        touchHelper.attachToRecyclerView(binding.rvNews)
    }

    private fun setupObservers() {
        viewModel.currencyTransactions.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun onTransactionDismiss(transaction: CurrencyTransaction) {
        viewModel.deleteCurrencyTransaction(transaction)
    }

    private fun onTransactionMove(from: Int, to: Int) {
        viewModel.moveCurrencyTransactions(from, to)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}