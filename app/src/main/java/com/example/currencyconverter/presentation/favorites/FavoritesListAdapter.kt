package com.example.currencyconverter.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ItemPodcastPlayerBinding
import com.example.currencyconverter.databinding.ItemVideoPlayerBinding

class FavoritesListAdapter(
    private val data: List<String>,
    private val fm: FragmentManager
) : RecyclerView.Adapter<FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_VIDEO -> {
                val binding = ItemVideoPlayerBinding.inflate(inflater, parent, false)
                FavoritesViewHolder(binding)
            }
            else -> {
                val binding = ItemPodcastPlayerBinding.inflate(inflater, parent, false)
                FavoritesViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val item = data[position]
        when (holder.itemViewType) {
            TYPE_VIDEO -> {
                val binding = holder.videoBinding

                binding?.root?.setOnClickListener {
                    fm.beginTransaction().add(R.id.fcv_movie_info, MovieInfoFragment())
                        .commit()
                }
            }
            else -> {
                val binding = holder.podcastBinding
            }
        }
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % 2 == 0) TYPE_PODCAST
        else TYPE_VIDEO
    }

    companion object {
        const val TYPE_VIDEO = 0
        const val TYPE_PODCAST = 1
    }
}