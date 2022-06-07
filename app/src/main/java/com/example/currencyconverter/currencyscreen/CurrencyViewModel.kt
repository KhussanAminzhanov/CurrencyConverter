package com.example.currencyconverter.currencyscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.database.CurrenciesData
import com.example.currencyconverter.database.CurrencyDao
import com.example.currencyconverter.database.CurrencyItem
import kotlinx.coroutines.launch

class CurrencyViewModel(private val dao: CurrencyDao) : ViewModel() {

    private val _currencies = MutableLiveData(CurrenciesData.getCurrencies())
    val currencies: LiveData<List<CurrencyItem>> = _currencies

    val tasks = dao.getAll()

    private val _isItemSelected = MutableLiveData(false)
    val isItemSelected: LiveData<Boolean> = _isItemSelected

    fun setItemSelected(value: Boolean) {
        _isItemSelected.value = value
    }

    fun addCurrency(currency: CurrencyItem) = viewModelScope.launch { dao.insert(currency) }
    fun update(currency: CurrencyItem) = viewModelScope.launch { dao.update(currency) }
    fun delete(currency: CurrencyItem) = viewModelScope.launch { dao.delete(currency) }
    fun deleteAll(currencies: List<CurrencyItem>) = viewModelScope.launch { dao.deleteAll(currencies) }
}