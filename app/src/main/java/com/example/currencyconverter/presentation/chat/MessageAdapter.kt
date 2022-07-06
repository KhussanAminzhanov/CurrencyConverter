package com.example.currencyconverter.presentation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.currencyconverter.databinding.ItemImageMessageBinding
import com.example.currencyconverter.databinding.ItemTextMessageBinding
import com.example.currencyconverter.domain.models.ChatMessage
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MessageAdapter(
    private val options: FirebaseRecyclerOptions<ChatMessage>,
    private val currentUserName: String?
) : FirebaseRecyclerAdapter<ChatMessage, ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_TEXT) {
            val binding = ItemTextMessageBinding.inflate(inflater, parent, false)
            TextMessageViewHolder(binding)
        } else {
            val binding = ItemImageMessageBinding.inflate(inflater, parent, false)
            ImageMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        model: ChatMessage
    ) {
        if (options.snapshots[position].text != null) {
            (holder as TextMessageViewHolder).bind(model, currentUserName)
        } else {
            (holder as ImageMessageViewHolder).bind(model)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (options.snapshots[position].text != null) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
    }

    companion object {
        const val TAG = "MessageAdapter"
        const val VIEW_TYPE_TEXT = 1
        const val VIEW_TYPE_IMAGE = 1
    }
}

