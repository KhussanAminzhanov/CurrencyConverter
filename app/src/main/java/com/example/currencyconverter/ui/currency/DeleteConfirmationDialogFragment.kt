package com.example.currencyconverter.ui.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.currencyconverter.databinding.DialogFragmentDeleteConfirmationBinding

class DeleteConfirmationDialogFragment(val onPositive: () -> Unit) : DialogFragment() {

    private val binding by lazy { DialogFragmentDeleteConfirmationBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alertDialogCancelButton.setOnClickListener { dismiss() }
        binding.alertDialogDeleteButton.setOnClickListener { onPositive(); dismiss() }
    }

    companion object {
        const val TAG = "DeleteCurrencyConfirmationDialog"
    }
}