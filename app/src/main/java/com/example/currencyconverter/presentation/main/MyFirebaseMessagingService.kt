package com.example.currencyconverter.presentation.main

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        handleMessage(message)
    }

    private fun handleMessage(message: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(baseContext, "Notification received!", Toast.LENGTH_LONG).show()
        }
    }
}