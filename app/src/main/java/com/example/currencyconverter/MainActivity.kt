package com.example.currencyconverter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val emptyCircle = "○"
const val fullCircle = "●"

class MainActivity : AppCompatActivity() {

    private val textView: TextView by lazy { findViewById(R.id.pin_code_textView) }
    private val pinCode = IntArray(4) { 0 }
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

        for (i in 0..9) {
            val button: Button = findViewById(buttonIndices[i])
            button.text = i.toString()
            button.setOnClickListener {
                if (index < 4) {
                    pinCode[index] = (it as Button).text.toString().toInt()
                    updatePinCodeText(index + 1, textView)
                }
            }
            numberButtons.add(button)
        }

        buttonBack.setOnClickListener {
            if (index > 0) updatePinCodeText(index - 1, textView)
        }

        buttonBack.setOnLongClickListener {
            updatePinCodeText(0, textView)
            true
        }

        buttonOk.setOnClickListener {
            if (index == 4) {
                if (pinCode.joinToString("") == "1567") {
                    val intent = Intent(this, MainScreen::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    textView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                    updatePinCodeText(0, textView)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updatePinCodeText(index: Int, textView: TextView) {
        this.index = index
        textView.text = fullCircle.repeat(index) + emptyCircle.repeat(4 - index)
    }

}

