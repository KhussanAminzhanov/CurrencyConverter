package com.example.currencyconverter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class LoginViewModel(private val state: SavedStateHandle) : ViewModel() {

    private val correctPinCode = "195612"
    val pinCodeLength = correctPinCode.length

    private var input = state.getLiveData(KEY_PIN_CODE_STRING, "0".repeat(pinCodeLength))
    private val _pinCode = MutableLiveData(IntArray(pinCodeLength) { 0 })
    private val _index = state.getLiveData(KEY_PIN_CODE_INDEX, 0)

    val index: LiveData<Int>
        get() = _index

    init { restorePinCode() }

    private fun restorePinCode() {
        val input = input.value ?: return
        val pinCodeArray = input.map { it.digitToInt() }.toIntArray()
        pinCodeArray.forEachIndexed { index, i -> _pinCode.value?.set(index, i) }
    }

    fun enterPinCodeDigit(digit: Int) {
        if (_index.value!! < pinCodeLength) {
            _pinCode.value!![_index.value!!] = digit
            _index.value = _index.value!!.plus(1)
        }
        input.value = _pinCode.value?.joinToString("")
    }

    fun removePinCodeDigit() {
        if (_index.value!! > 0) _index.value = _index.value!!.minus(1)
    }

    fun resetPinCodeIndex() {
        _index.value = 0
    }

    fun checkPinCode(): Boolean {
        return input.value == correctPinCode
    }

    override fun onCleared() {
        state.set(KEY_PIN_CODE_INDEX, _index.value)
        state.set(KEY_PIN_CODE_STRING, input.value)
    }

    companion object {
        private const val KEY_PIN_CODE_INDEX = "LOGIN_VIEW_MODEL_KEY_PIN_CODE_INDEX"
        private const val KEY_PIN_CODE_STRING = "LOGIN_VIEW_MODEL_KEY_PIN_CODE_STRING"
    }
}