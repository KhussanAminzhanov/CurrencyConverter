package com.example.currencyconverter

class HMSAnalytics : Analytics() {

    override fun logEvent(eventName: String, params: Bundle?) {}

    override fun setUserProperty(name: String, value: String) {}

    override fun setUserId(id: String) {}

    override fun clearData() {}

    override fun instanceId(listener: (String) -> Unit) {}
}