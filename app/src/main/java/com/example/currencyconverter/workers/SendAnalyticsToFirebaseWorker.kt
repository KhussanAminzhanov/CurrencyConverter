package com.example.currencyconverter.workers

import android.content.Context
import android.os.Bundle
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.currencyconverter.KEY_IMAGE_ID
import com.google.firebase.analytics.FirebaseAnalytics

class SendAnalyticsToFirebaseWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun doWork(): Result {
        makeStatusNotifications("Sending GMSAnalytics", applicationContext)

        firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
        val itemId = inputData.getString(KEY_IMAGE_ID)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
        return Result.success()
    }

    companion object {
        private const val DOWNLOAD_IMAGE = "FIREBASE_ANALYTICS_DOWNLOAD_IMAGE"
    }
}