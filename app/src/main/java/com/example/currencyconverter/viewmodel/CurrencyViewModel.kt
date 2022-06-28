package com.example.currencyconverter.viewmodel

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.database.CurrencyItem
import com.example.currencyconverter.domain.models.Currencies
import com.example.currencyconverter.repository.CurrenciesRepository
import com.example.currencyconverter.repository.CurrencyRepository
import com.example.currencyconverter.ui.converter.DeleteConfirmationDialogFragment
import kotlinx.coroutines.launch
import java.util.*

class CurrencyViewModel(context: Context, val repository: CurrenciesRepository) : ViewModel() {

    enum class SortType { ALPHABET, VALUE, UNSORTED }

    private val _currencyQuotes = MutableLiveData<Currencies>()
    val currencyQuotes: LiveData<Currencies> = _currencyQuotes

    private val _sortingType = MutableLiveData(SortType.UNSORTED)
    val sortingType: LiveData<SortType> = _sortingType

    private val repo = CurrencyRepository(context)
    val isItemSelected = MutableLiveData(false)
    val balance = MutableLiveData(1.0)
    val checkedCurrencyPositions = mutableListOf<CurrencyItem>()
    val currencies = repo.currencies

    init {
        refreshCurrencyQuotes()
    }

    fun addCurrency(currency: CurrencyItem) = viewModelScope.launch { repo.insert(currency) }
    fun delete(currency: CurrencyItem) = viewModelScope.launch { repo.delete(currency) }

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
            repo.update(firstCurrency)
            repo.update(secondCurrency)
        }
    }

    private fun deleteCurrencies() {
        viewModelScope.launch {
            repo.deleteAll(checkedCurrencyPositions)
            checkedCurrencyPositions.clear()
        }
    }

    fun refreshCurrencyQuotes() {
        viewModelScope.launch {
            repository.getCurrencies(
                onSuccess = ::onCurrenciesFetchSuccess,
                onError = ::onCurrenciesFetchError
            )
        }
    }

    private fun onCurrenciesFetchSuccess(currencies: Currencies) {
        _currencyQuotes.value = currencies
        Log.i(TAG, "Success fetching ${currencies.AED}")
    }

    private fun onCurrenciesFetchError() {
        Log.e(TAG, "Error fetching currencies")
    }

    fun showDeleteConfirmationDialog(fragmentManager: FragmentManager) {
        val dialog = DeleteConfirmationDialogFragment(this::deleteCurrencies)
        dialog.show(fragmentManager, DeleteConfirmationDialogFragment.TAG)
    }

    companion object {
        private const val TAG = "currency_view_model"
    }
}