package com.example.currencyconverter.loginscreen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.R
import kotlin.math.min

class PinCodeDigitView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    enum class PinCodeDigitState {
        FULL, EMPTY, ANIMATION
    }

    var state = MutableLiveData(PinCodeDigitState.EMPTY)

    private var radius = 0.0F
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        state.observe(context as LifecycleOwner) {
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w, h) / 2.0 * 0.7).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        paint.style = Paint.Style.FILL
        paint.color = when (state.value) {
            PinCodeDigitState.FULL -> context.getColor(R.color.blue)
            else -> context.getColor(R.color.white)
        }
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)

        paint.color = when (state.value) {
            PinCodeDigitState.ANIMATION -> context.getColor(R.color.red)
            else -> context.getColor(R.color.blue)
        }
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4.5f
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
    }
}