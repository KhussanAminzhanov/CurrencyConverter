package com.example.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrenciesViewModel : ViewModel() {

    private val data = mutableListOf(
        CurrencyItem(0, "Tenge", R.drawable.kazakhstan_flag, 0L),
        CurrencyItem(1, "Dollar", R.drawable.usa_flag, 0L)
    )
    private val _currencies = MutableLiveData<List<CurrencyItem>>(data)
    val currencies: LiveData<List<CurrencyItem>> = _currencies

    var currentId = 2L

    fun addCurrency(newCurrencyItem: CurrencyItem) {
        data.add(newCurrencyItem)
        currentId++
        _currencies.value = data
    }
}