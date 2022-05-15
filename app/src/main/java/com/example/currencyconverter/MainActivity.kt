package com.example.currencyconverter

import android.content.Context
import android.content.Intent
import android.os.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.databinding.ActivityMainBinding

const val EMPTY_CIRCLE = "○"
const val FULL_CIRCLE = "●"

class MainActivity : AppCompatActivity(), Animation.AnimationListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val buttonIndices = intArrayOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
        )

        for (i in 0..9) {
            val button: Button = findViewById(buttonIndices[i])
            button.text = i.toString()
            button.setOnClickListener {
                val digit = (it as Button).text.first().digitToInt()
                viewModel.enterPinCodeDigit(digit)
            }
        }

        binding.buttonBack.setOnClickListener { viewModel.removePinCodeDigit() }

        binding.buttonBack.setOnLongClickListener {
            viewModel.resetPinCodeIndex()
            true
        }

        binding.buttonOk.setOnClickListener {
            if (viewModel.index.value == viewModel.pinCodeLength) {
                if (viewModel.checkPinCode()) {
                    finish()
                    val intent = Intent(this, MainScreen::class.java)
                    startActivity(intent)
                } else {
                    vibrate()
                    val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
                    animation.setAnimationListener(this)
                    binding.pinCodeTextView.startAnimation(animation)
                    viewModel.resetPinCodeIndex()
                }
            }
        }

        viewModel.index.observe(this) {
            binding.pinCodeTextView.text = getString(
                R.string.pin_code_text,
                FULL_CIRCLE.repeat(viewModel.index.value!!),
                EMPTY_CIRCLE.repeat(viewModel.pinCodeLength - viewModel.index.value!!)
            )
        }
    }

    override fun onAnimationStart(p0: Animation?) {
        binding.pinCodeTextView.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.incorrect_pin_code
            )
        )
    }

    override fun onAnimationEnd(p0: Animation?) {
        binding.pinCodeTextView.setTextColor(ContextCompat.getColor(this, R.color.blue))
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

