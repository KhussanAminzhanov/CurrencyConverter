package com.example.currencyconverter.presentation.history

import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemPodcastPlayerBinding
import com.example.currencyconverter.databinding.ItemVideoPlayerBinding

class TransactionViewHolder : RecyclerView.ViewHolder {
    var videoBinding: ItemVideoPlayerBinding? = null
    var podcastBinding: ItemPodcastPlayerBinding? = null

    constructor(binding: ItemVideoPlayerBinding) : super(binding.root) {
        videoBinding = binding
    }

    constructor(binding: ItemPodcastPlayerBinding) : super(binding.root) {
        podcastBinding = binding
    }
}