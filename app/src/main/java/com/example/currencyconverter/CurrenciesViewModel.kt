package com.example.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class CurrenciesViewModel : ViewModel() {

    enum class SortType {
        ALPHABET, VALUE, UNSORTED
    }

    var currentId = 0
    private var sortingType = SortType.UNSORTED
    private var data = mutableListOf(
        CurrencyItem(currentId, "Tenge, Kazakhstan", R.drawable.kazakhstan_flag, 0L),
        CurrencyItem(currentId, "Dollar, USA", R.drawable.usa_flag, 1000L)
    )

    private val _currencies = MutableLiveData<List<CurrencyItem>>(data)
    val currencies: LiveData<List<CurrencyItem>> = _currencies

    private val _itemSelected = MutableLiveData(false)
    val itemSelected: LiveData<Boolean> = _itemSelected

    fun addCurrency(newCurrencyItem: CurrencyItem) {
        data.add(newCurrencyItem)
        currentId++

        sortData()
        _currencies.value = data
    }

    fun deleteCurrency(currencyItemId: Int) {

        var indexOfCurrencyItem = 0

        for ((index, currency) in data.withIndex()) {
            if (currency.currencyId == currencyItemId) {
                indexOfCurrencyItem = index
                break
            }
        }

        data.removeAt(indexOfCurrencyItem)
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

    fun moveCurrencies(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
                val id = data[i].currencyId
                data[i].currencyId = data[i + 1].currencyId
                data[i + 1].currencyId = id
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
                val id = data[i].currencyId
                data[i].currencyId = data[i - 1].currencyId
                data[i - 1].currencyId = id
            }
        }
    }

    fun setSortingType(type: SortType) {
        sortingType = type
        sortData()
        _currencies.value = data
    }

    fun changeCurrencyData(position: Int, newValue: Long) {
        data[position].value = newValue
        _currencies.value = data
    }

    fun isItemSelected(value: Boolean) {
        _itemSelected.value = value
    }
}