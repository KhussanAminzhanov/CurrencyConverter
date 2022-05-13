package com.example.currencyconverter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.databinding.CurrencyItemBinding

class CurrenciesAdapter : RecyclerView.Adapter<CurrenciesAdapter.CurrenciesItemViewHolder>() {

    var data = listOf<CurrencyItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder = CurrenciesItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, holder.itemView.context)
    }

    override fun getItemCount(): Int = data.size

    class CurrenciesItemViewHolder(private val binding: CurrencyItemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): CurrenciesItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = CurrencyItemBinding.inflate(layoutInflater, parent, false)
                return CurrenciesItemViewHolder(view)
            }
        }

        fun bind(item: CurrencyItem, context: Context) {
            binding.currencyValueTextInputLayout.hint = item.name
            binding.currencyName.text = item.name
            binding.currencyFlagImage.setImageDrawable(ContextCompat.getDrawable(context, item.image))
        }
    }
}