package com.example.currencyconverter.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.currencyconverter.data.database.Photo
import com.example.currencyconverter.databinding.ItemPhotoBinding

class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo, onClick: (Photo) -> Unit) {
        Glide.with(itemView.context).load(photo.urlThumb).into(binding.ivPhotoThumb)
        binding.ivPhotoThumb.setOnClickListener { onClick(photo) }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): PhotoViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemPhotoBinding.inflate(inflater, parent, false)
            return PhotoViewHolder(binding)
        }
    }
}