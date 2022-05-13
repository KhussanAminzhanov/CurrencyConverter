package com.example.currencyconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private val _pinCode = MutableLiveData("")
    val pinCode: LiveData<String>
        get() = _pinCode

    private val _index = MutableLiveData(0)
    val index: LiveData<Int>
        get() = _index

}