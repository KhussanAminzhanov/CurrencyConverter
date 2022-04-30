package com.example.currencyconverter

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val EMPTY_CIRCLE = "○"
const val FULL_CIRCLE = "●"
const val PIN_CODE_LENGTH = 4
const val CORRECT_PIN_CODE = "1567"

class MainActivity : AppCompatActivity() {

    private val textView: TextView by lazy { findViewById(R.id.pin_code_textView) }
    private val pinCode = IntArray(PIN_CODE_LENGTH) { 0 }
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val numberButtons = mutableListOf<Button>()
        val buttonBack: Button = findViewById(R.id.buttonBack)
        val buttonOk: Button = findViewById(R.id.buttonOk)
        val buttonIndices = intArrayOf(
            R.id.button0, R.id.button1, R.id.button2,
            R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8,
            R.id.button9,
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
                    textView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                    textView.text = updatePinCodeText(0)
                }
            }
        }
    }

    private fun updatePinCodeText(index: Int): String {
        this.index = index
        return FULL_CIRCLE.repeat(index) + EMPTY_CIRCLE.repeat(PIN_CODE_LENGTH - index)
    }

}

