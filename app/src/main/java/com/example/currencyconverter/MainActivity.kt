package com.example.currencyconverter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

const val emptyCircle = '○'
const val fullCircle = '●'

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val textView: TextView = findViewById(R.id.pin_code_textView)
        val numberButtons = mutableListOf<Button>()
        val buttonIndices = intArrayOf(
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
        )

        for (i in 0..9) {
            numberButtons.add(findViewById(buttonIndices[i]))
            numberButtons[i].text = i.toString()
            numberButtons[i].setOnClickListener {
                textView.text = textView.text.toString() + i
            }
        }

        val buttonBack: Button = findViewById(R.id.buttonBack)
        val buttonOk: Button = findViewById(R.id.buttonOk)

        buttonBack.setOnClickListener {
            textView.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.blue_text_color
                )
            )
            if (textView.text.isNotBlank()) {
                textView.text = textView.text.subSequence(0, textView.text.length - 1)
            }
        }

        buttonOk.setOnClickListener {
            if (textView.text.isNotBlank()) {
                val text = if (textView.text == "1567") {
                    "Correct!"
                } else {
                    textView.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.incorrect_pin_code
                        )
                    )
                    textView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
                    "Incorrect!"
                }
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}