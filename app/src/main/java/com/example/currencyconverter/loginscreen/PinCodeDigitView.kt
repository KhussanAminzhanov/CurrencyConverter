package com.example.currencyconverter.loginscreen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.currencyconverter.R
import kotlin.math.min

private enum class PinCodeDigitState {
    FULL, EMPTY
}

class PinCodeDigitView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius = 0.0F
    private var state = PinCodeDigitState.EMPTY
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w, h) / 2.0 * 0.8).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        paint.color = when (state) {
            PinCodeDigitState.FULL -> context.getColor(R.color.blue)
            PinCodeDigitState.EMPTY -> context.getColor(R.color.white)
        }
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
    }
}