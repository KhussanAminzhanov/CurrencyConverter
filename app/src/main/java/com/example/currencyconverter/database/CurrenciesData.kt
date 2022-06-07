package com.example.currencyconverter.database

import com.example.currencyconverter.R
import java.util.*
import kotlin.random.Random

object CurrenciesData {

    enum class SortType {
        ALPHABET, VALUE, UNSORTED
    }

    private var sortingType = SortType.UNSORTED
    private var data = mutableListOf<CurrencyItem>()
    var currentId = 0

    private fun swapCurrencyId(data: List<CurrencyItem>, firstIndex: Int, secondIndex: Int) {
        val id = data[firstIndex].currencyId
        data[firstIndex].currencyId = data[secondIndex].currencyId
        data[secondIndex].currencyId = id
    }

    fun getCurrencies() : List<CurrencyItem> = data

    fun addCurrency(newCurrencyItem: CurrencyItem) {
        data.add(newCurrencyItem)
        sortData()
        currentId++
    }

    fun deleteCurrency(position: Int): CurrencyItem {
        val deletedCurrency = data[position].copy()
        data.removeAt(position)
        return deletedCurrency
    }

    fun deleteCurrencies(positions: List<Int>): List<CurrencyItem> {
        val deletedCurrencies = mutableListOf<CurrencyItem>()
        positions.forEach { position -> deletedCurrencies.add(data[position].copy()) }
        deletedCurrencies.forEach { currency -> data.remove(currency) }
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

    private fun sortData() {
        data = when (sortingType) {
            SortType.ALPHABET -> data.sortedBy { it.name }.toMutableList()
            SortType.VALUE -> data.sortedBy { it.value }.toMutableList()
            SortType.UNSORTED -> data.sortedBy { it.currencyId }.toMutableList()
        }
    }

    fun setSortingType(type: SortType) {
        sortingType = type
        sortData()
    }

    fun randomCurrency(): CurrencyItem {
        val list = listOf(
            CurrencyItem(currentId, "Dollar, USA", R.drawable.flag_usa, currentId.toLong()),
            CurrencyItem(currentId, "Euro, EU", R.drawable.flag_europe, currentId.toLong()),
            CurrencyItem(currentId, "Lira, Turkey", R.drawable.flag_turkey, currentId.toLong()),
            CurrencyItem(currentId, "Tenge, Kazakhstan", R.drawable.flag_kazakhstan, currentId.toLong())
        )
        return list.shuffled()[Random.nextInt(0, 4)]
    }
}