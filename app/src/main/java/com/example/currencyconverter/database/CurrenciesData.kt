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

    private fun sortData() {
        data = when (sortingType) {
            SortType.ALPHABET -> data.sortedBy { it.name }.toMutableList()
            SortType.VALUE -> data.sortedBy { it.exchangeRate }.toMutableList()
            SortType.UNSORTED -> data.sortedBy { it.currencyId }.toMutableList()
        }
    }

    fun setSortingType(type: SortType) {
        sortingType = type
        sortData()
    }

    fun randomCurrency(): CurrencyItem {
        val list = listOf(
            CurrencyItem(currentId, "Dollar, USA", R.drawable.flag_usa, 0.0023),
            CurrencyItem(currentId, "Euro, EU", R.drawable.flag_europe, 0.0022),
            CurrencyItem(currentId, "Lira, Turkey", R.drawable.flag_turkey, 0.1415),
            CurrencyItem(currentId, "Tenge, Kazakhstan", R.drawable.flag_kazakhstan, 1.0)
        )
        return list.shuffled()[Random.nextInt(0, 4)]
    }
}