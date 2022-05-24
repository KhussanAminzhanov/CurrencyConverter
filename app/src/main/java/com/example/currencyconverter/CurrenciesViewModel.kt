package com.example.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.random.Random

class CurrenciesViewModel : ViewModel() {

    enum class SortType {
        ALPHABET, VALUE, UNSORTED
    }

    var currentId = 0
    private var sortingType = SortType.UNSORTED
    private var data = mutableListOf<CurrencyItem>()

    private val _currencies = MutableLiveData<List<CurrencyItem>>(data)
    val currencies: LiveData<List<CurrencyItem>> = _currencies

    private val _isItemSelected = MutableLiveData(false)
    val isItemSelected: LiveData<Boolean> = _isItemSelected

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

    private fun swapCurrencyId(data: List<CurrencyItem>, firstIndex: Int, secondIndex: Int) {
        val id = data[firstIndex].currencyId
        data[firstIndex].currencyId = data[secondIndex].currencyId
        data[secondIndex].currencyId = id
    }

    fun addCurrency(newCurrencyItem: CurrencyItem) {
        data.add(newCurrencyItem)
        sortData()
        currentId++
        _currencies.value = data
    }

    fun deleteCurrency(position: Int) : CurrencyItem {
        val deletedCurrency = data[position].copy()
        data.removeAt(position)
        _currencies.value = data
        return deletedCurrency
    }

    fun deleteCurrencies(positions: List<Int>): List<CurrencyItem> {
        val deletedCurrencies = mutableListOf<CurrencyItem>()
        positions.forEach { position -> deletedCurrencies.add(data[position].copy()) }
        deletedCurrencies.forEach { currency -> data.remove(currency) }
        _currencies.value = data
        return deletedCurrencies
    }

    fun moveCurrencies(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
                swapCurrencyId(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
                swapCurrencyId(data, i, i - 1)
            }
        }
    }

    fun setSortingType(type: SortType) {
        sortingType = type
        sortData()
        _currencies.value = data
    }

    fun updateCurrencyData(position: Int, newValue: Long) {
        data[position].value = newValue
        _currencies.value = data
    }

    fun setItemSelected(value: Boolean) {
        _isItemSelected.value = value
    }

    fun randomCurrency(): CurrencyItem {
        val list = listOf(
            CurrencyItem(
                currentId,
                "Lira, Turkey",
                R.drawable.turkey_flag,
                currentId.toLong()
            ),
            CurrencyItem(
                currentId,
                "Dollar, USA",
                R.drawable.usa_flag,
                currentId.toLong()
            ),
            CurrencyItem(
                currentId,
                "Tenge, Kazakhstan",
                R.drawable.kazakhstan_flag,
                currentId.toLong()
            ),
            CurrencyItem(
                currentId,
                "Euro, EU",
                R.drawable.europe_flag,
                currentId.toLong()
            )
        )
        return list.shuffled()[Random.nextInt(0, 4)]
    }
}