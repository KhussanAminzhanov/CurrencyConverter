package com.example.currencyconverter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {

    private val correctPinCode = "195612"
    val pinCodeLength = correctPinCode.length

    private val _pinCode = MutableLiveData(IntArray(pinCodeLength) {0})

    private val _index = MutableLiveData(0)
    val index: LiveData<Int>
        get() = _index

    fun enterPinCodeDigit(digit: Int) {
        if (_index.value!! < pinCodeLength) {
            _pinCode.value!![_index.value!!] = digit
            _index.value = _index.value!!.plus(1)
        }
    }

    fun removePinCodeDigit() {
        if (_index.value!! > 0) _index.value = _index.value!!.minus(1)
    }

    fun resetPinCodeIndex() {
        _index.value = 0
    }

    fun checkPinCode(): Boolean {
        return _pinCode.value?.joinToString("") == correctPinCode
    }
}