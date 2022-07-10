package com.example.currencyconverter.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class CurrencyMigration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE currency_table ADD COLUMN value DOUBLE NOT NULL DEFAULT 0.0")
    }
}