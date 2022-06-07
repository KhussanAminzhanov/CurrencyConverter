package com.example.currencyconverter.currencyscreen

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.database.CurrencyDao
import com.example.currencyconverter.database.CurrencyItem
import kotlinx.coroutines.launch
import java.util.*

class CurrencyViewModel(private val dao: CurrencyDao) : ViewModel() {

    val checkedCurrencyPositions = mutableListOf<CurrencyItem>()
    val isItemSelected = MutableLiveData(false)
    val currencies = dao.getAll()
    val value = 0

    fun addCurrency(currency: CurrencyItem) = viewModelScope.launch { dao.insert(currency) }
    fun update(currency: CurrencyItem) = viewModelScope.launch { dao.update(currency) }
    fun delete(currency: CurrencyItem) = viewModelScope.launch { dao.delete(currency) }
    fun deleteAll(currencies: List<CurrencyItem>) =
        viewModelScope.launch { dao.deleteAll(currencies) }

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
        deleteAll(checkedCurrencyPositions)
        checkedCurrencyPositions.clear()
    }

    fun showDeleteConfirmationDialog(fragmentManager: FragmentManager) {
        val dialog = DeleteConfirmationDialogFragment { deleteCurrencies() }
        dialog.show(fragmentManager, DeleteConfirmationDialogFragment.TAG)
    }
}