package com.example.currencyconverter.currencyscreen

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.database.CurrencyDao
import com.example.currencyconverter.database.CurrencyItem
import kotlinx.coroutines.launch
import java.util.*

class CurrencyViewModel(private val dao: CurrencyDao) : ViewModel() {

    enum class SortType { ALPHABET, VALUE, UNSORTED }

    private val _sortingType = MutableLiveData(SortType.UNSORTED)
    val sortingType: LiveData<SortType> = _sortingType

    val isItemSelected = MutableLiveData(false)
    val balance = MutableLiveData(1.0)
    val checkedCurrencyPositions = mutableListOf<CurrencyItem>()
    val currencies = dao.getAll()

    fun addCurrency(currency: CurrencyItem) = viewModelScope.launch { dao.insert(currency) }
    fun update(currency: CurrencyItem) = viewModelScope.launch { dao.update(currency) }
    fun delete(currency: CurrencyItem) = viewModelScope.launch { dao.delete(currency) }
    fun deleteAll(currencies: List<CurrencyItem>) =
        viewModelScope.launch { dao.deleteAll(currencies) }

    fun getCurrenciesSorted(list: List<CurrencyItem>) = when (_sortingType.value) {
        SortType.ALPHABET -> list.sortedBy { it.name }
        SortType.VALUE -> list.sortedBy { it.exchangeRate }
        else -> list
    }

    fun setSortingType(sortType: SortType) {
        _sortingType.value = sortType
    }

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

        viewModelScope.launch {
            dao.update(firstCurrency)
            dao.update(secondCurrency)
        }
    }

    private fun deleteCurrencies() {
        viewModelScope.launch {
            dao.deleteAll(checkedCurrencyPositions)
            checkedCurrencyPositions.clear()
        }
    }

    fun showDeleteConfirmationDialog(fragmentManager: FragmentManager) {
        val dialog = DeleteConfirmationDialogFragment(this::deleteCurrencies)
        dialog.show(fragmentManager, DeleteConfirmationDialogFragment.TAG)
    }
}