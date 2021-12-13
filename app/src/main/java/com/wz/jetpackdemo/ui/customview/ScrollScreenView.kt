package com.wz.jetpackdemo.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import com.wz.jetpackdemo.R

class ScrollScreenView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    val TAG = ScrollScreenView::class.java.simpleName
    var mWidth = 200
    var mHeight = 200
    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mLastX:Int = 0
    var mLastY:Int = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth,heightSize)
        }else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight)
        }
    }

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        val circleColor = typeArray.getColor(R.styleable.CircleView_circle_color, Color.RED)
        typeArray.recycle()
        mPaint.color = circleColor

    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.rawX?.toInt()
        val y = event?.rawY?.toInt()
        when (event?.action) {
            MotionEvent.ACTION_DOWN ->{
                parent.requestDisallowInterceptTouchEvent(true)
                mLastX = x?:0
                mLastY = y?:0
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x?.minus(mLastX)
                val deltaY = y?.minus(mLastY)
                val parentNeedEvent = false
                if (parentNeedEvent) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }

        }

        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var rawX = event?.rawX
        val rawY = event?.rawY
        when (event?.action) {
            MotionEvent.ACTION_MOVE ->{
                val deltaX = rawX?.minus(mLastX)
                val deltaY = rawY?.minus(mLastY)
                Log.e(TAG, "move,rawX:$rawX rawY:$rawY mLastX:$mLastX mLastY:$mLastY deltaX: $deltaX, deltaY:$deltaY")
                layout((left + deltaX!!).toInt(), (top + deltaY!!).toInt(),
                    (right + deltaX).toInt(), (bottom+deltaY).toInt()
                )
            }
        }
        mLastX = rawX?.toInt() ?: 0

        mLastY = rawY?.toInt() ?: 0

        return true
    }
    val scroller = Scroller(context)

    fun smoothScrollTo(destX: Int, destY: Int) {
        val deltaX = destX - scrollX
        val deltaY = destY - scrollY
        scroller.startScroll(scrollX, scrollY, deltaX, deltaY, 3000)
        invalidate()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)

        canvas?.drawColor(Color.LTGRAY)
        val height = measuredHeight - paddingTop - paddingBottom
        val width = measuredWidth - paddingLeft -paddingRight
        val radius = Math.min(height, width) / 2
        canvas?.drawCircle(paddingLeft + width.toFloat()/2, paddingTop + height.toFloat()/2, radius.toFloat(), mPaint)

    }

    private fun getSize(defaultSize: Int, measueSpec: Int): Int {
        var measueSize = defaultSize
        val mode = MeasureSpec.getMode(measueSpec)
        val size = MeasureSpec.getSize(measueSpec)
        when (mode) {
            MeasureSpec.UNSPECIFIED -> measueSize = defaultSize
            MeasureSpec.AT_MOST -> measueSize = size
            MeasureSpec.EXACTLY -> measueSize = size
        }
        return measueSize
    }

}