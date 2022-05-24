package com.example.currencyconverter

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DeleteCurrencyConfirmationDialogFragment(val onPositive: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(R.string.delete_currency_alert_dialog_title_text)
            .setPositiveButton("Delete") { _, _ -> onPositive(); dismiss()}
            .setNegativeButton("Cancel") { _, _ -> dismiss()}
            .create()
    }

    companion object {
        const val TAG = "DeleteCurrencyConfirmationDialog"
    }
}