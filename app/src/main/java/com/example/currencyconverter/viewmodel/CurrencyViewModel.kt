package com.example.currencyconverter.viewmodel

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.database.Currency
import com.example.currencyconverter.database.CurrencyQuote
import com.example.currencyconverter.repository.CurrencyRepository
import com.example.currencyconverter.ui.converter.DeleteConfirmationDialogFragment
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

    val isNetworkError = MutableLiveData(false)
    val isItemSelected = MutableLiveData(false)
    val balance = MutableLiveData(1.0)
    val checkedCurrencyPositions = mutableListOf<Currency>()
    val database = repository.database.currencyDao
    val currencies = repository.database.currencyDao.getAll()
    val currencyNames: LiveData<Map<String, String>> = repository.currencyNames
    val currencyRates: LiveData<Map<String, Double?>> = repository.currencyRates
    val currencyQuotes: LiveData<List<CurrencyQuote>> = repository.currencyQuotes

//    init {
//        refreshCurrencyNames()
//    }

    fun refreshCurrencyNames() {
        viewModelScope.launch(ioDispatcher) {
            try {
                Log.i(TAG, "refreshing currency names")
                repository.refreshCurrencyNames()
            } catch (e: Exception) {
                onNetworkError("Error refreshing currency names: $e")
            }
        }
    }

    fun refreshCurrencyRates() {
        viewModelScope.launch(ioDispatcher) {
            try {
                repository.refreshCurrencyRates()
            } catch (e: Exception) {
                onNetworkError("Error refreshing currency rates: $e")
            }
        }
    }

    fun refreshCurrencyQuotes(currencyQuotes: List<CurrencyQuote>) {
        viewModelScope.launch(ioDispatcher) {
            repository.refreshCurrencyQuotes(currencyQuotes)
        }
    }

    private fun onNetworkError(msg: String) {
        Log.e(TAG, "Error refreshing currency quotes: $msg")
        isNetworkError.value = true
    }

    fun addCurrency(currency: Currency) = viewModelScope.launch(ioDispatcher) { database.insert(currency) }
    fun delete(currency: Currency) = viewModelScope.launch(ioDispatcher) { database.delete(currency) }

    fun getCurrenciesSorted(list: List<Currency>) = when (_sortingType.value) {
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

        viewModelScope.launch(ioDispatcher) {
            database.update(firstCurrency)
            database.update(secondCurrency)
        }
    }

    private fun deleteCurrencies() {
        viewModelScope.launch(ioDispatcher) {
            database.deleteAll(checkedCurrencyPositions)
            checkedCurrencyPositions.clear()
        }
    }

    fun showDeleteConfirmationDialog(fragmentManager: FragmentManager) {
        val dialog = DeleteConfirmationDialogFragment(this::deleteCurrencies)
        dialog.show(fragmentManager, DeleteConfirmationDialogFragment.TAG)
    }

    companion object {
        private const val TAG = "currency_view_model"
    }
}