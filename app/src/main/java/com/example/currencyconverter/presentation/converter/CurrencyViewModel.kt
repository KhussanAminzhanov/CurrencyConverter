package com.example.currencyconverter.presentation.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.database.Currency
import com.example.currencyconverter.data.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CurrencyViewModel(
    private val repository: CurrencyRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    enum class SortType { ALPHABET, VALUE, UNSORTED }

    private val _sortingType = MutableLiveData(SortType.UNSORTED)
    val sortingType: LiveData<SortType> = _sortingType

    private val _isItemSelected = MutableLiveData(false)
    val isItemSelected: LiveData<Boolean> = _isItemSelected

    private val _balance = MutableLiveData(1.0)
    val balance: LiveData<Double> = _balance

    private val checkedCurrencies = mutableListOf<Currency>()

    private val currencyDao = repository.database.currencyDao
    val currencies = repository.database.currencyDao.getAll()
    val currencyQuotes = repository.database.currencyQuoteDao.getAll()
//
//    init {
//        refreshCurrencyQuotes()
//    }

    fun refreshCurrencyQuotes() = viewModelScope.launch(ioDispatcher) {
        repository.refreshCurrencyQuotes()
    }

    fun addCurrency(currency: Currency) =
        viewModelScope.launch(ioDispatcher) { currencyDao.insert(currency) }

    fun deleteCurrency(currency: Currency) =
        viewModelScope.launch(ioDispatcher) { currencyDao.delete(currency) }

    fun deleteCurrencies() = viewModelScope.launch(ioDispatcher) {
        currencyDao.deleteAll(checkedCurrencies)
        checkedCurrencies.clear()
    }

    fun getCurrenciesSorted(list: List<Currency>) = when (_sortingType.value) {
        SortType.ALPHABET -> list.sortedBy { it.name }
        SortType.VALUE -> list.sortedBy { it.exchangeRate }
        else -> list.sortedBy { it.currencyId }
    }

    fun setSortingType(sortType: SortType) {
        _sortingType.value = sortType
    }

    fun setItemSelected(isItemSelected: Boolean) {
        _isItemSelected.value = isItemSelected
    }

    fun setBalance(balance: Double) {
        _balance.value = balance
    }

    fun addCheckedCurrency(currency: Currency) = checkedCurrencies.add(currency)
    fun removeCheckedCurrency(currency: Currency) = checkedCurrencies.remove(currency)
    fun containsCheckedCurrency(currency: Currency) = checkedCurrencies.contains(currency)
    fun clearCheckCurrencies() = checkedCurrencies.clear()

    fun moveCurrencies(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                currencies.value?.let { Collections.swap(it, i, i + 1) }
                swapCurrencyId(i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                currencies.value?.let { Collections.swap(it, i, i - 1) }
                swapCurrencyId(i, i - 1)
            }
        }
    }

    private fun swapCurrencyId(from: Int, to: Int) {
        val firstCurrency = currencies.value?.get(from)
        val secondCurrency = currencies.value?.get(to)

        if (firstCurrency == null || secondCurrency == null) return
        val position = firstCurrency.currencyId
        firstCurrency.currencyId = secondCurrency.currencyId
        secondCurrency.currencyId = position

        viewModelScope.launch(ioDispatcher) {
            currencyDao.update(firstCurrency)
            currencyDao.update(secondCurrency)
        }
    }
}