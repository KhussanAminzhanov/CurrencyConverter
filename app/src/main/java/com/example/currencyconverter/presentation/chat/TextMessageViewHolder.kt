package com.example.currencyconverter.presentation.chat

import android.graphics.Color
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ItemTextMessageBinding
import com.example.currencyconverter.domain.models.ChatMessage

class TextMessageViewHolder(private val binding: ItemTextMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatMessage, currencyUserName: String?) {
        binding.tvMessage.text = item.text
        setTextColor(item.name, currencyUserName, binding.tvMessage)
        val userNameCapital = (item.name ?: "A").first().toString()
        binding.tvMessengerFullName.text = if (item.name == null) ChatFragment.ANONYMOUS else item.name
        binding.tvMessengerCapitalLetter.text = userNameCapital
    }

    private fun setTextColor(userName: String?, currentUserName: String?, textView: TextView) {
        if (userName != ChatFragment.ANONYMOUS && currentUserName == userName && userName != null) {
            textView.setBackgroundResource(R.drawable.rounded_message_blue)
            textView.setTextColor(Color.WHITE)
        } else {
            textView.setBackgroundResource(R.drawable.rounded_message_gray)
            textView.setTextColor(Color.BLACK)
        }
    }
}