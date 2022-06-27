package com.wz.jetpackdemo.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val mPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }
    init {
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.WHITE

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()
        val radius = width / 2
        canvas?.drawCircle(radius.toFloat(), 0f, radius.toFloat(), mPaint)
        canvas?.drawCircle(radius.toFloat(), height.toFloat(), radius.toFloat(), mPaint)
        canvas?.restore()

    }
}