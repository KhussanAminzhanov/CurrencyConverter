package com.example.currencyconverter.favoritesscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.ItemPodcastPlayerBinding
import com.example.currencyconverter.databinding.ItemVideoPlayerBinding

class FavoritesAdapter(list: List<String>) : RecyclerView.Adapter<FavoritesViewHolder>() {

    private val data = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_VIDEO -> {
                val binding = ItemVideoPlayerBinding.inflate(inflater)
                FavoritesViewHolder(binding)
            }
            else -> {
                val binding = ItemPodcastPlayerBinding.inflate(inflater)
                FavoritesViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val item = data[position]
        when (holder.itemViewType) {
            TYPE_VIDEO -> {
                val binding = holder.videoBinding
            }
            else -> {
                val binding = holder.podcastBinding
            }
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) TYPE_PODCAST
        else TYPE_VIDEO
    }

    companion object {
        const val TYPE_VIDEO = 0
        const val TYPE_PODCAST = 0
    }
}