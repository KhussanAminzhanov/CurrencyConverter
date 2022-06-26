package com.example.currencyconverter.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.currencyconverter.database.CurrencyDao
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.database.CurrencyItem

class CurrencyRepository(val context: Context) {

    private val db: CurrencyDatabase = CurrencyDatabase.getInstance(context)
    private val dao: CurrencyDao = db.currencyDao

    val currencies: LiveData<List<CurrencyItem>> = dao.getAll()

    fun get(currencyId: Int) : LiveData<CurrencyItem> {
        return dao.get(currencyId)
    }

    suspend fun insert(currency: CurrencyItem) {
        dao.insert(currency)
    }

    suspend fun update(currency: CurrencyItem) {
        dao.update(currency)
    }

    suspend fun delete(currency: CurrencyItem) {
        dao.delete(currency)
    }

    suspend fun deleteAll(currencies: List<CurrencyItem>) {
        dao.deleteAll(currencies)
    }
}