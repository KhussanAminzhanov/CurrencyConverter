package com.example.currencyconverter.viewmodel

import android.util.Log
import androidx.lifecycle.*


class LoginViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val correctPinCode = "195612"
    val pinCodeLength = correctPinCode.length

    private val _pinCode = state.getLiveData(KEY_PIN_CODE_INPUT, IntArray(pinCodeLength) { 0 })

    private val _index = state.getLiveData(KEY_PIN_CODE_INDEX, 0)
    val index: LiveData<Int>
        get() = _index

    private var pinCodeString = state.getLiveData(KEY_PIN_CODE_STRING, "")

    init {
        Log.i("LVM", "pin code = ${_pinCode.value?.joinToString("")}")
        Log.i("LVM", "pin index = ${_index.value}")
        Log.i("LVM", "pin code string = ${pinCodeString.value}")
    }

    fun enterPinCodeDigit(digit: Int) {
        if (_index.value!! < pinCodeLength) {
            _pinCode.value!![_index.value!!] = digit
            _index.value = _index.value!!.plus(1)
        }
        pinCodeString.value = _pinCode.value?.joinToString("")
    }

    fun removePinCodeDigit() {
        if (_index.value!! > 0) _index.value = _index.value!!.minus(1)
    }

    fun resetPinCodeIndex() {
        _index.value = 0
    }

    fun checkPinCode(): Boolean {
        return pinCodeString.value == correctPinCode
    }

    override fun onCleared() {
        state.set(KEY_PIN_CODE_INPUT, _pinCode.value)
        state.set(KEY_PIN_CODE_INDEX, _index.value)
        state.set(KEY_PIN_CODE_STRING, pinCodeString.value)
    }

    companion object {
        private const val KEY_PIN_CODE_INPUT = "LOGIN_VIEW_MODEL_KEY_PIN_CODE_INPUT"
        private const val KEY_PIN_CODE_INDEX = "LOGIN_VIEW_MODEL_KEY_PIN_CODE_INDEX"
        private const val KEY_PIN_CODE_STRING = "LOGIN_VIEW_MODEL_KEY_PIN_CODE_STRING"
    }
}