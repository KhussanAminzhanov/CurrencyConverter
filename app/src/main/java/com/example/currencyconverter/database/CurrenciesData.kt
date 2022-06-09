package com.example.currencyconverter.database

import com.example.currencyconverter.R
import kotlin.random.Random

object CurrenciesData {

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