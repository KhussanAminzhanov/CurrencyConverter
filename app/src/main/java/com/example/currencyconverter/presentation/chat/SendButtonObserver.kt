package com.example.currencyconverter.presentation.chat

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import com.example.currencyconverter.R

class SendButtonObserver(private val button: ImageButton) : TextWatcher {
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s.toString().trim().isNotEmpty()) {
            button.isEnabled = true
            button.setImageResource(R.drawable.ic_baseline_send)
        } else {
            button.isEnabled = false
            button.setImageResource(R.drawable.ic_baseline_send_gray)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}

}