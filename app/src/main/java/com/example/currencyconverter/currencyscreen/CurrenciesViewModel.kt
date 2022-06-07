package com.example.currencyconverter.currencyscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyconverter.database.CurrenciesData
import com.example.currencyconverter.database.CurrencyDao
import com.example.currencyconverter.database.CurrencyItem

class CurrenciesViewModel() : ViewModel() {

    private val _currencies = MutableLiveData(CurrenciesData.getCurrencies())
    val currencies: LiveData<List<CurrencyItem>> = _currencies

    private val _isItemSelected = MutableLiveData(false)
    val isItemSelected: LiveData<Boolean> = _isItemSelected

    fun setItemSelected(value: Boolean) {
        _isItemSelected.value = value
    }
}