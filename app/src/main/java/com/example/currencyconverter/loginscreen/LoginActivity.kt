package com.example.currencyconverter.loginscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityLoginBinding

const val EMPTY_CIRCLE = "○"
const val FULL_CIRCLE = "●"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNumericKeypad()

        viewModel.index.observe(this) {
            binding.textViewPinCode.text = getString(
                R.string.pin_code_text,
                FULL_CIRCLE.repeat(viewModel.index.value!!),
                EMPTY_CIRCLE.repeat(viewModel.pinCodeLength - viewModel.index.value!!)
            )
        }
    }

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

    private fun setupNumericKeypad() {
        for (i in 1..9) {
            binding.gridLayoutNumericKeypad.addView(getNormalNumericButton(i.toString()) {
                viewModel.enterPinCodeDigit(i)
            })
        }

        binding.gridLayoutNumericKeypad.addView(getBackButton())
        binding.gridLayoutNumericKeypad.addView(getNormalNumericButton("0") {
            viewModel.enterPinCodeDigit(0)
        })
        binding.gridLayoutNumericKeypad.addView(getOkButton())
    }

    @SuppressLint("InflateParams")
    private fun getNormalNumericButton(text: String, onClickListener: () -> Unit = {}): Button {
        val numericKeypad = layoutInflater.inflate(R.layout.btn_numeric_keypad, null) as Button
        numericKeypad.text = text
        numericKeypad.setOnClickListener { onClickListener() }
        return numericKeypad
    }

    private fun getBackButton(): Button {
        val backButton = getNormalNumericButton(getString(R.string.back_button_text))
        backButton.setOnClickListener { viewModel.removePinCodeDigit() }
        backButton.setOnLongClickListener { viewModel.resetPinCodeIndex(); true }
        return backButton
    }

    private fun getOkButton(): Button {
        val okButton = getNormalNumericButton(getString(R.string.ok_button_text))
        okButton.background = AppCompatResources.getDrawable(this, R.drawable.btn_ok_selector)
        okButton.setOnClickListener {
            if (viewModel.index.value == viewModel.pinCodeLength) {
                if (viewModel.checkPinCode()) {
                    finish()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    viewModel.resetPinCodeIndex()
                    vibrate()
                    val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
                    animation.setAnimationListener(getPinCodeTextViewAnimationListener())
                    binding.textViewPinCode.startAnimation(animation)
                }
            }
        }
        return okButton
    }


    private fun getPinCodeTextViewAnimationListener(): Animation.AnimationListener {
        return object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.textViewPinCode.setTextColor(
                    ContextCompat.getColor(this@LoginActivity, R.color.incorrect_pin_code)
                )
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.textViewPinCode.setTextColor(
                    ContextCompat.getColor(
                        this@LoginActivity,
                        R.color.blue
                    )
                )
            }

            override fun onAnimationRepeat(p0: Animation?) = Unit
        }
    }
}

