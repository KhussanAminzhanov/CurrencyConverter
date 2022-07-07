package com.example.currencyconverter.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.currencyconverter.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    val binding get() = _binding!!

    private lateinit var adapter: HistoryListAdapter
    private lateinit var fm: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater)
        fm = requireActivity().supportFragmentManager

        adapter = HistoryListAdapter(listOf("First", "Second", "Third", "Fourth", "Fifth", "Sixth"))
        binding.rvNews.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}