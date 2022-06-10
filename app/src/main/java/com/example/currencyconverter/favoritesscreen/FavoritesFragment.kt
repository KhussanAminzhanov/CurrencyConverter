package com.example.currencyconverter.favoritesscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.currencyconverter.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    val binding get() = _binding!!

    private lateinit var adapter: FavoritesAdapter
    private lateinit var fm: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater)
        fm = requireActivity().supportFragmentManager

        adapter = FavoritesAdapter(listOf("First", "Second", "Third", "Fourth", "Fifth", "Sixth"), fm)
        binding.rvNews.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}