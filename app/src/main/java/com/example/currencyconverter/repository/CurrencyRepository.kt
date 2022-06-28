package com.example.currencyconverter.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.currencyconverter.database.CurrencyDao
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.database.Currency

class CurrencyRepository(val context: Context) {

    private val db: CurrencyDatabase = CurrencyDatabase.getInstance(context)
    private val dao: CurrencyDao = db.currencyDao

    val currencies: LiveData<List<Currency>> = dao.getAll()

    fun get(currencyId: Int) : LiveData<Currency> {
        return dao.get(currencyId)
    }

    suspend fun insert(currency: Currency) {
        dao.insert(currency)
    }

    suspend fun update(currency: Currency) {
        dao.update(currency)
    }

    suspend fun delete(currency: Currency) {
        dao.delete(currency)
    }

    suspend fun deleteAll(currencies: List<Currency>) {
        dao.deleteAll(currencies)
    }
}