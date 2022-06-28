package com.example.currencyconverter.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrencyQuoteDao {

    @Insert
    suspend fun insert(currencyQuote: CurrencyQuote)

    @Insert
    suspend fun insertAll(currencyQuotes: List<CurrencyQuote>)

    @Update
    suspend fun update(currencyQuyQuotes: CurrencyQuote)

    @Delete
    suspend fun delete(currencyQuyQuotes: CurrencyQuote)

    @Delete
    suspend fun deleteAll(currencyQuotes: List<CurrencyQuote>)

    @Query("SELECT * FROM currency_table WHERE currencyId = :currencyId")
    fun get(currencyId: Int): LiveData<CurrencyQuote>

    @Query("SELECT * FROM currency_table ORDER BY name")
    fun getAll(): LiveData<List<CurrencyQuote>>
}