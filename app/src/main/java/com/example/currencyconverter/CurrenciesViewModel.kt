package com.example.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class CurrenciesViewModel : ViewModel() {

    enum class SortType {
        ALPHABET, VALUE, UNSORTED
    }

    private var data = mutableListOf(
        CurrencyItem(0, "Tenge", R.drawable.kazakhstan_flag, 0L),
        CurrencyItem(1, "Dollar", R.drawable.usa_flag, 1000L)
    )

    private val _currencies = MutableLiveData<List<CurrencyItem>>(data)
    val currencies: LiveData<List<CurrencyItem>> = _currencies

    private var sortingType = SortType.UNSORTED
    var currentId = 2L

    fun addCurrency(newCurrencyItem: CurrencyItem) {
        data.add(newCurrencyItem)
        currentId++
        _currencies.value = data
    }

    fun deleteCurrency(position: Int) {
        data.removeAt(position)
        _currencies.value = data
    }

    fun moveCurrencies(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
    }

    fun setSortingType(type: SortType) {
        sortingType = type
        sortData()
        _currencies.value = data
    }

    private fun sortData() {
        data = when (sortingType) {
            SortType.ALPHABET -> {
                data.sortedBy { it.name }.toMutableList()
            }
            SortType.VALUE -> {
                data.sortedBy { it.value }.toMutableList()
            }
            SortType.UNSORTED -> {
                data.sortedBy { it.currencyId }.toMutableList()
            }
        }
    }
}