package com.example.currencyconverter.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.currencyconverter.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

private const val TAG = "WorkerUtils"

fun makeStatusNotifications(message: String, context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
        val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = description

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
}

fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap, imageId: String): Uri {
    val name = String.format("unsplash-$imageId-%s.jpeg", UUID.randomUUID().toString())
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }
    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: Exception) {
            }
        }
    }
    return Uri.fromFile(outputFile)
}

private fun saveImage(image: Bitmap): String? {
    var savedImagePath: String? = null
    val imageFileName = "JPEG_" + "FILE_NAME" + ".jpg"
    val storageDir = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .toString() + "/YOUR_FOLDER_NAME"
    )
    var success = true
    if (!storageDir.exists()) {
        success = storageDir.mkdirs()
    }
    if (success) {
        val imageFile = File(storageDir, imageFileName)
        savedImagePath = imageFile.getAbsolutePath()
        try {
            val fOut: OutputStream = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        galleryAddPic(savedImagePath)
    }
    return savedImagePath
}

//private fun galleryAddPic(imagePath: String?) {
//    imagePath?.let { path ->
//        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//        val f = File(path)
//        val contentUri: Uri = Uri.fromFile(f)
//        mediaScanIntent.data = contentUri
//        sendBroadcast(mediaScanIntent)
//    }
//}