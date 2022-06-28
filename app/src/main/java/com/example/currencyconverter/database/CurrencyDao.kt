package com.example.currencyconverter.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrencyDao {

    @Insert
    suspend fun insert(currency: Currency)

    @Update
    suspend fun update(currency: Currency)

    @Delete
    suspend fun delete(currency: Currency)

    @Delete
    suspend fun deleteAll(currencies: List<Currency>)

    @Query("SELECT * FROM currency_table WHERE currencyId = :currencyId")
    fun get(currencyId: Int): LiveData<Currency>

    @Query("SELECT * FROM currency_table ORDER BY currencyId")
    fun getAll(): LiveData<List<Currency>>
}