package com.example.currencyconverter.presentation.chat

import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.currencyconverter.databinding.ItemImageMessageBinding
import com.example.currencyconverter.domain.models.ChatMessage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ImageMessageViewHolder(private val binding: ItemImageMessageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChatMessage) {
        loadImageToView(binding.ivMessage, item.imageUrl!!)

        val messengerName = item.name ?: "A"
        binding.tvMessengerFullName.text =
            if (item.name == null) ChatFragment.ANONYMOUS else item.name
        binding.tvMessengerCapitalLetter.text = messengerName.first().toString()
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
                    Log.e(MessageAdapter.TAG, "Getting download url was not successful.", e)
                }
        } else {
            Glide.with(view.context).load(url).into(view)
        }
    }
}