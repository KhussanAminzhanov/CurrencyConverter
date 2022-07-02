package com.example.currencyconverter.presentation.converter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


class ConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (listener != null) {
            val isConnected = (networkInfo != null) && networkInfo.isConnectedOrConnecting
            listener!!.onNetworkChange(isConnected)
        }
    }

    interface ReceiverListener {
        fun onNetworkChange(isConnected: Boolean)
    }

    companion object {
        var listener: ReceiverListener? = null
    }
}