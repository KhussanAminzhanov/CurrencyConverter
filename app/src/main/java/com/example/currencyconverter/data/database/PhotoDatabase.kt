package com.example.currencyconverter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyconverter.domain.models.unsplash.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {
    abstract val photoDao: PhotoDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: PhotoDatabase

        fun getInstance(context: Context): PhotoDatabase {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PhotoDatabase::class.java,
                        "photo_database"
                    ).build()
                }
                return INSTANCE
            }
        }
    }
}