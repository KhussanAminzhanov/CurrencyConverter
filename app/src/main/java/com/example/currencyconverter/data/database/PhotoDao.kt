package com.example.currencyconverter.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.example.currencyconverter.domain.models.unsplash.Photo

@Dao
interface PhotoDao {

    @Insert(onConflict = IGNORE)
    fun add(photo: Photo)

    @Insert(onConflict = IGNORE)
    fun addAll(photos: List<Photo>)

    @Update
    fun update(photo: Photo)

    @Delete
    fun delete(photo: Photo)

    @Delete
    fun deleteAll(photos: List<Photo>)

    @Query("SELECT * FROM photos_table WHERE id = :photoId")
    fun get(photoId: Int): LiveData<Photo>

    @Query("SELECT * FROM photos_table ORDER BY id")
    fun getAll() : LiveData<List<Photo>>
}