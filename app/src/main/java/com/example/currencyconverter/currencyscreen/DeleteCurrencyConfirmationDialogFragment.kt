package com.example.currencyconverter.currencyscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.currencyconverter.databinding.DeleteCurrencyAlertDialogLayoutBinding

class DeleteCurrencyConfirmationDialogFragment(val onPositive: () -> Unit) : DialogFragment() {

    private val binding by lazy { DeleteCurrencyAlertDialogLayoutBinding.inflate(layoutInflater) }

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