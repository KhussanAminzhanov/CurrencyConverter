package com.example.currencyconverter.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrencyDao {

    @Insert
    fun insert(currency: CurrencyItem)

    @Update
    fun update(currency: CurrencyItem)

    @Delete
    fun delete(currency: CurrencyItem)

    @Delete
    fun deleteAll(currencies: List<CurrencyItem>)

    @Query("SELECT * FROM currency_table WHERE currencyId = :currencyId")
    fun get(currencyId: Int): LiveData<CurrencyItem>

    @Query("SELECT * FROM currency_table ORDER BY currencyId DESC")
    fun getAll(): LiveData<List<CurrencyItem>>
}