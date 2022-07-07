package com.example.currencyconverter.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CurrencyTransactionDao {

    @Insert
    suspend fun insert(transaction: CurrencyTransaction)

    @Insert
    suspend fun insertAll(transactions: List<CurrencyTransaction>)

    @Update
    suspend fun update(transaction: CurrencyTransaction)

    @Delete
    suspend fun delete(transaction: CurrencyTransaction)

    @Delete
    suspend fun deleteAll(transactions: List<CurrencyTransaction>)

    @Query("SELECT * FROM transaction_table WHERE id = :transactionId")
    fun get(transactionId: Int): LiveData<CurrencyTransaction>

    @Query("SELECT * FROM TRANSACTION_TABLE ORDER BY id")
    fun getAll(): LiveData<List<CurrencyTransaction>>
}