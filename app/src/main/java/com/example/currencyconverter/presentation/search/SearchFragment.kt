package com.example.currencyconverter.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyconverter.data.database.Photo
import com.example.currencyconverter.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by sharedViewModel()

    private lateinit var adapter: PhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupObservers()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = PhotoAdapter(::showDownloadImageDialog)
        binding.rvPhotos.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun showDownloadImageDialog(photo: Photo) {
        val dialog = DownloadImageDialogFragment(photo)
        dialog.show(parentFragmentManager, DownloadImageDialogFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}