package com.example.currencyconverter.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrencyDao {

    @Insert
    suspend fun insert(currency: CurrencyItem)

    @Update
    suspend fun update(currency: CurrencyItem)

    @Delete
    suspend fun delete(currency: CurrencyItem)

    @Delete
    suspend fun deleteAll(currencies: List<CurrencyItem>)

    @Query("SELECT * FROM currency_table WHERE currencyId = :currencyId")
    fun get(currencyId: Int): LiveData<CurrencyItem>

    @Query("SELECT * FROM currency_table ORDER BY currencyId")
    fun getAll(): LiveData<List<CurrencyItem>>

    @Query("SELECT * FROM currency_table ORDER BY currency_value")
    fun getAllSortedByValue(): LiveData<List<CurrencyItem>>

    @Query("SELECT * FROM currency_table ORDER BY currency_name")
    fun getAllSortedByName(): LiveData<List<CurrencyItem>>
}