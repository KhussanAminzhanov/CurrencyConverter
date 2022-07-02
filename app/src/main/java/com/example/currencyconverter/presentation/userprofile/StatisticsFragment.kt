package com.example.currencyconverter.presentation.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater)
        binding.tvTotalTimeSpent.text = getString(R.string.statistics_total_spend_time, "10.20.30")
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}