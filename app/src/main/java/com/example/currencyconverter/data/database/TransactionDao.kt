package com.example.currencyconverter.data.database

import android.net.TrafficStats
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction)

    @Insert
    suspend fun insertAll(transactions: List<Transaction>)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Delete
    suspend fun deleteAll(transactions: List<Transaction>)

    @Query("SELECT * FROM transaction_table WHERE id = :transactionId")
    suspend fun get(transactionId: Int): LiveData<TrafficStats>

    @Query("SELECT * FROM TRANSACTION_TABLE ORDER BY id")
    suspend fun getAll(): LiveData<List<Transaction>>
}