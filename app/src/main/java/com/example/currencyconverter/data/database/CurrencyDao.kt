package com.example.currencyconverter.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface CurrencyDao {

    @Insert(onConflict = REPLACE)
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