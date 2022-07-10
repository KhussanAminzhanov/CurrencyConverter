package com.example.currencyconverter

import android.os.Bundle

abstract class Analytics {
    abstract fun logEvent(eventName: String, params: Bundle?)
    abstract fun setUserProperty(name: String, value: String)
    abstract fun setUserId(id: String)
    abstract fun clearData()
    abstract fun instanceId(listener: (String) -> Unit)
}