package com.example.currencyconverter.presentation.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityLoginBinding
import com.example.currencyconverter.presentation.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.stateViewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val pinCodeDigitViews = mutableListOf<PinCodeDigitView>()
    private val model: LoginViewModel by stateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setupNumericKeypad()
        setupPinCode()

        model.index.observe(this) { updatePinCodeDigitViews() }
    }

    private fun setupPinCode() {
        repeat(model.pinCodeLength) {
            val pinCodeDigitView = getPinCodeDigitView()
            pinCodeDigitViews.add(pinCodeDigitView)
            binding.llPinCode.addView(pinCodeDigitView)
        }
    }

    private fun updatePinCodeDigitViews() {
        pinCodeDigitViews.forEachIndexed { index, pinCodeDigitView ->
            val currentDigitIndex = (model.index.value ?: 0) - 1
            if (index <= currentDigitIndex) {
                pinCodeDigitView.state.value = PinCodeDigitView.PinCodeDigitState.FULL
            } else {
                pinCodeDigitView.state.value = PinCodeDigitView.PinCodeDigitState.EMPTY
            }
        }
    }

    private fun getPinCodeDigitView(): PinCodeDigitView {
        return layoutInflater.inflate(
            R.layout.pin_code_digit,
            binding.llPinCode,
            false
        ) as PinCodeDigitView
    }

    private fun setupNumericKeypad() {
        for (i in 1..9) {
            binding.gridLayoutNumericKeypad.addView(getNormalNumericButton(i.toString()) {
                model.enterPinCodeDigit(i)
                checkPinCode()
            })
        }
        binding.gridLayoutNumericKeypad.addView(getBackButton())
        binding.gridLayoutNumericKeypad.addView(getNormalNumericButton("0") {
            model.enterPinCodeDigit(0)
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
        backButton.setOnClickListener { model.removePinCodeDigit() }
        backButton.setOnLongClickListener { model.resetPinCodeIndex(); true }
        return backButton
    }

    private fun getOkButton(): Button {
        val okButton = getNormalNumericButton(getString(R.string.ok_button_text))
        okButton.background = AppCompatResources.getDrawable(this, R.drawable.btn_ok_selector)
        okButton.setOnClickListener { checkPinCode() }
        return okButton
    }

    private fun checkPinCode() {
        if (model.index.value == model.pinCodeLength) {
            if (model.checkPinCode()) {
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                model.resetPinCodeIndex()
                vibrate()
                val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
                animation.setAnimationListener(getPinCodeTextViewAnimationListener())
                binding.llPinCode.startAnimation(animation)
            }
        }
    }

    private fun getPinCodeTextViewAnimationListener(): Animation.AnimationListener {
        return object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                pinCodeDigitViews.forEach {
                    it.state.value = PinCodeDigitView.PinCodeDigitState.ANIMATION
                }
            }

            override fun onAnimationEnd(p0: Animation?) = updatePinCodeDigitViews()
            override fun onAnimationRepeat(p0: Animation?) = Unit
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
}

