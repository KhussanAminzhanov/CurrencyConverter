package com.example.currencyconverter.database

import com.example.currencyconverter.R
import kotlin.random.Random

object CurrenciesData {

    enum class SortType {
        ALPHABET, VALUE, UNSORTED
    }

    private var sortingType = SortType.UNSORTED
    private var data = mutableListOf<CurrencyItem>()

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
        return getCurrenciesList().shuffled()[Random.nextInt(0, 4)]
    }

    fun getCurrenciesList(): List<CurrencyItem> {
        return listOf(
            CurrencyItem(name = "Dollar, USA", image = R.drawable.flag_usa, exchangeRate = 0.0023),
            CurrencyItem(name = "Euro, EU", image = R.drawable.flag_europe, exchangeRate = 0.0022),
            CurrencyItem(name = "Lira, Turkey", image = R.drawable.flag_turkey, exchangeRate = 0.1415),
            CurrencyItem(name = "Tenge, Kazakhstan", image = R.drawable.flag_kazakhstan, exchangeRate = 1.0),
        )
    }
}