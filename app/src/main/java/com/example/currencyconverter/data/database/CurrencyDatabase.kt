package com.example.currencyconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Currency::class, CurrencyQuote::class, CurrencyTransaction::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract val currencyDao: CurrencyDao
    abstract val currencyQuoteDao: CurrencyQuoteDao
    abstract val transactionDao: CurrencyTransactionDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: CurrencyDatabase

        fun getInstance(context: Context): CurrencyDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CurrencyDatabase::class.java,
                        "currency_database"
                    ).build()
                }
                return INSTANCE
            }
        }
    }
}