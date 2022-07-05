package com.example.currencyconverter.workers

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import com.example.currencyconverter.KEY_IMAGE_URI
import com.example.currencyconverter.KEY_IMAGE_URL
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "SavePhotoWorker"

class SavePhotoWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private val title = "unsplash_image"
    private val dateFormatter = SimpleDateFormat(
        "yyyy.MM.dd 'at' HH:mm:ss z",
        Locale.getDefault()
    )

    override fun doWork(): Result {
        makeStatusNotifications("Saving Image", applicationContext)
        val resolver = applicationContext.contentResolver
        return try {
            val imageUrl = inputData.getString(KEY_IMAGE_URL)
            val bitmap = Glide.with(applicationContext).asBitmap().load(imageUrl).submit().get()
            val imageUri = MediaStore.Images.Media.insertImage(
                resolver,
                bitmap,
                title,
                dateFormatter.format(Date())
            )
            if (!imageUri.isNullOrEmpty()) {
                val output = workDataOf(KEY_IMAGE_URI to imageUri)
                Result.success(output)
            } else {
                Log.e(TAG, "Writing to MediaStore failed")
                Result.failure()
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }
}