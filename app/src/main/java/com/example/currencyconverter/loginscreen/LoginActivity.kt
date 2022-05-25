package com.example.currencyconverter.loginscreen

import android.content.Context
import android.content.Intent
import android.os.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityLoginBinding

const val EMPTY_CIRCLE = "○"
const val FULL_CIRCLE = "●"

class LoginActivity : AppCompatActivity(), Animation.AnimationListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val okButton = Button(this)
        val backButton = Button(this)

        for (i in 1..9) {
            val numericKeypad = layoutInflater.inflate(R.layout.btn_numeric_keypad, null) as Button
            numericKeypad.text = i.toString()
            numericKeypad.setOnClickListener {
                val digit = (it as Button).text.first().digitToInt()
                viewModel.enterPinCodeDigit(digit)
            }
            binding.gridLayoutNumericKeypad?.addView(numericKeypad)
        }

        backButton.setOnClickListener { viewModel.removePinCodeDigit() }

        backButton.setOnLongClickListener {
            viewModel.resetPinCodeIndex()
            true
        }

        okButton.setOnClickListener {
            if (viewModel.index.value == viewModel.pinCodeLength) {
                if (viewModel.checkPinCode()) {
                    finish()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    vibrate()
                    val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
                    animation.setAnimationListener(this)
                    binding.textViewPinCode.startAnimation(animation)
                    viewModel.resetPinCodeIndex()
                }
            }
        }

        viewModel.index.observe(this) {
            binding.textViewPinCode.text = getString(
                R.string.pin_code_text,
                FULL_CIRCLE.repeat(viewModel.index.value!!),
                EMPTY_CIRCLE.repeat(viewModel.pinCodeLength - viewModel.index.value!!)
            )
        }
    }

    override fun onAnimationStart(p0: Animation?) {
        binding.textViewPinCode.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.incorrect_pin_code
            )
        )
    }

    override fun onAnimationEnd(p0: Animation?) {
        binding.textViewPinCode.setTextColor(ContextCompat.getColor(this, R.color.blue))
    }

    override fun onAnimationRepeat(p0: Animation?) = Unit

    @Suppress("DEPRECATION")
    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(50)
        }

    }

}

