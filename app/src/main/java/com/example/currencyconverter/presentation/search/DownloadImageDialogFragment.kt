package com.example.currencyconverter.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.currencyconverter.data.database.Photo
import com.example.currencyconverter.databinding.DialogFragmentDownloadImageBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DownloadImageDialogFragment(val photo: Photo) : DialogFragment() {

    private var _binding: DialogFragmentDownloadImageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentDownloadImageBinding.inflate(inflater, container, false)

        Glide.with(requireContext()).load(photo.urlRegular).into(binding.ivImage)

        binding.btnDownload.setOnClickListener { viewModel.savePhoto(photo.urlFull) }
        binding.btnCancel.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "DownloadImageDialogFragment"
    }
}