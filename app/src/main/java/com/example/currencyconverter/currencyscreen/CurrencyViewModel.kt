package com.example.currencyconverter.currencyscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.database.CurrencyDao
import com.example.currencyconverter.database.CurrencyItem
import kotlinx.coroutines.launch

class CurrencyViewModel(private val dao: CurrencyDao) : ViewModel() {

    val isItemSelected = MutableLiveData(false)
    val currencies = dao.getAll()

    fun addCurrency(currency: CurrencyItem) = viewModelScope.launch { dao.insert(currency) }
    fun update(currency: CurrencyItem) = viewModelScope.launch { dao.update(currency) }
    fun delete(currency: CurrencyItem) = viewModelScope.launch { dao.delete(currency) }
    fun deleteAll(currencies: List<CurrencyItem>) = viewModelScope.launch { dao.deleteAll(currencies) }

    fun moveCurrencies(firstId: Int, secondId: Int) {
//        if (fromPosition < toPosition) {
//            for (i in fromPosition until toPosition) {
//                Collections.swap(CurrenciesData.data, i, i + 1)
//                CurrenciesData.swapCurrencyId(CurrenciesData.data, i, i + 1)
//            }
//        } else {
//            for (i in fromPosition downTo toPosition + 1) {
//                Collections.swap(CurrenciesData.data, i, i - 1)
//                CurrenciesData.swapCurrencyId(CurrenciesData.data, i, i - 1)
//            }
//        }
    }
}