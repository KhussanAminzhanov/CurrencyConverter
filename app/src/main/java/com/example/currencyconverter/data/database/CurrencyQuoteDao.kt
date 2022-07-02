package com.example.currencyconverter.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
interface CurrencyQuoteDao {

    @Insert(onConflict = IGNORE)
    suspend fun insert(currencyQuote: CurrencyQuote)

    @Insert(onConflict = IGNORE)
    suspend fun insertAll(currencyQuotes: List<CurrencyQuote>)

    @Update
    suspend fun update(currencyQuyQuotes: CurrencyQuote)

    @Delete
    suspend fun delete(currencyQuyQuotes: CurrencyQuote)

    @Delete
    suspend fun deleteAll(currencyQuotes: List<CurrencyQuote>)

    @Query("SELECT * FROM currency_quote_table ORDER BY name")
    fun getAll(): LiveData<List<CurrencyQuote>>
}