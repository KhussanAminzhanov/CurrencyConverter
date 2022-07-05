package com.example.currencyconverter.presentation.search

import androidx.recyclerview.widget.DiffUtil
import com.example.currencyconverter.data.database.Photo

class PhotoDiffUtilItemCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Photo, newItem: Photo) = oldItem == newItem
}