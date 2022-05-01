package com.example.currencyconverter

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

const val EMPTY_CIRCLE = "○"
const val FULL_CIRCLE = "●"
const val PIN_CODE_LENGTH = 4
const val CORRECT_PIN_CODE = "1567"
const val PIN_CODE_INSTANCE_KEY = "PIN_CODE"
const val PIN_CODE_INDEX = "PIN_CODE_INDEX"

class MainActivity : AppCompatActivity(), Animation.AnimationListener {

    private val textView: TextView by lazy { findViewById(R.id.pin_code_textView) }
    private var pinCode = IntArray(PIN_CODE_LENGTH) { 0 }
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val numberButtons = mutableListOf<Button>()
        val buttonBack: Button = findViewById(R.id.buttonBack)
        val buttonOk: Button = findViewById(R.id.buttonOk)
        val buttonIndices = intArrayOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
        )

        
        textView.text = updatePinCodeText(0)

        for (i in 0..9) {
            val button: Button = findViewById(buttonIndices[i])
            button.text = i.toString()
            button.setOnClickListener {
                if (index < PIN_CODE_LENGTH) {
                    pinCode[index] = (it as Button).text.toString().toInt()
                    textView.text = updatePinCodeText(index + 1)
                }
            }
            numberButtons.add(button)
        }

        buttonBack.setOnClickListener {
            if (index > 0) textView.text = updatePinCodeText(index - 1)
        }

        buttonBack.setOnLongClickListener {
            textView.text = updatePinCodeText(0)
            true
        }

        buttonOk.setOnClickListener {
            if (index == PIN_CODE_LENGTH) {
                if (pinCode.joinToString("") == CORRECT_PIN_CODE) {
                    val intent = Intent(this, MainScreen::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
                    animation.setAnimationListener(this)
                    textView.startAnimation(animation)
                    textView.text = updatePinCodeText(0)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(PIN_CODE_INSTANCE_KEY, pinCode)
        outState.putInt(PIN_CODE_INDEX, index)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pinCode = savedInstanceState.getIntArray(PIN_CODE_INSTANCE_KEY) ?: IntArray(PIN_CODE_LENGTH) { 0 }
        index = savedInstanceState.getInt(PIN_CODE_INDEX)
        textView.text = updatePinCodeText(index)
    }

    private fun updatePinCodeText(index: Int): String {
        this.index = index
        return FULL_CIRCLE.repeat(index) + EMPTY_CIRCLE.repeat(PIN_CODE_LENGTH - index)
    }

    override fun onAnimationStart(p0: Animation?) {
        textView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_pin_code))
    }

    override fun onAnimationEnd(p0: Animation?) {
        textView.setTextColor(ContextCompat.getColor(this, R.color.blue_text_color))
    }

    override fun onAnimationRepeat(p0: Animation?) {}

}

