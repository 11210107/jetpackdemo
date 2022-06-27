package com.wz.jetpackdemo.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DrawableLeftTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas?) {
        val drawableLeft = compoundDrawables[0]
        drawableLeft?.let {
            val textWith = paint.measureText(text.toString())
            compoundDrawablePadding
            val drawableWidth = drawableLeft.intrinsicWidth
            val bodyWidth = textWith + drawableWidth + compoundDrawablePadding
            canvas?.translate((width - bodyWidth) / 2, 0f)
        }
        super.onDraw(canvas)
    }
}