package com.example.currencyconverter.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var value: Int
)