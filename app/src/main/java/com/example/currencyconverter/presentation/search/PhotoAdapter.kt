package com.example.currencyconverter.presentation.search

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.data.database.Photo

class PhotoAdapter(private val onItemClick: (Photo) -> Unit) :
    ListAdapter<Photo, PhotoViewHolder>(PhotoDiffUtilItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo, onItemClick)
    }
}