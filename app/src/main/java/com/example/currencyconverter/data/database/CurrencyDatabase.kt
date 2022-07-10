package com.example.currencyconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyconverter.data.database.migrations.CurrencyMigration1To2

@Database(
    entities = [Currency::class, CurrencyQuote::class, CurrencyTransaction::class],
    version = 2,
    exportSchema = true
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
                    ).addMigrations(MIGRATIN_1_2).build()
                }
                return INSTANCE
            }
        }

        val MIGRATIN_1_2 = CurrencyMigration1To2()
    }
}