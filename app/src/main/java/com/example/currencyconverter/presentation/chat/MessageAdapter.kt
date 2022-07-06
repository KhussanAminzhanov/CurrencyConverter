package com.example.currencyconverter.presentation.chat

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ItemImageMessageBinding
import com.example.currencyconverter.databinding.ItemTextMessageBinding
import com.example.currencyconverter.domain.models.ChatMessage
import com.example.currencyconverter.presentation.chat.ChatFragment.Companion.ANONYMOUS
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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
            (holder as TextMessageViewHolder).bind(model)
        } else {
            (holder as ImageMessageViewHolder).bind(model)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (options.snapshots[position].text != null) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
    }

    inner class TextMessageViewHolder(private val binding: ItemTextMessageBinding) :
        ViewHolder(binding.root) {

        fun bind(item: ChatMessage) {
            binding.tvMessage.text = item.text
            setTextColor(item.name, binding.tvMessage)
            val messengerName = item.name ?: "A"
            binding.tvMessengerFullName.text = if (item.name == null) ANONYMOUS else item.name
            binding.tvMessengerCapitalLetter.text = messengerName.first().toString()
        }

        private fun setTextColor(userName: String?, textView: TextView) {
            if (userName != ANONYMOUS && currentUserName == userName && userName != null) {
                textView.setBackgroundResource(R.drawable.rounded_message_blue)
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_gray)
                textView.setTextColor(Color.BLACK)
            }
        }
    }

    inner class ImageMessageViewHolder(private val binding: ItemImageMessageBinding) :
        ViewHolder(binding.root) {
        fun bind(item: ChatMessage) {
            loadImageToView(binding.ivMessage, item.imageUrl!!)

            val messengerName = item.name ?: "A"
            binding.tvMessengerFullName.text = if (item.name == null) ANONYMOUS else item.name
            binding.tvMessengerCapitalLetter.text = messengerName.first().toString()
        }
    }

    private fun loadImageToView(view: ImageView, url: String) {
        if (url.startsWith("gs://")) {
            val storageReference = Firebase.storage.getReference(url)
            storageReference.downloadUrl
                .addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Glide.with(view.context)
                        .load(downloadUrl)
                        .into(view)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Getting download url was not successful.", e)
                }
        } else {
            Glide.with(view.context).load(url).into(view)
        }
    }

    companion object {
        const val TAG = "MessageAdapter"
        const val VIEW_TYPE_TEXT = 1
        const val VIEW_TYPE_IMAGE = 1
    }
}