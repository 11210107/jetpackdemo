package com.wz.jetpackdemo.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller

class HorizontalScrollViewEx @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    val TAG = HorizontalScrollViewEx::class.java.simpleName
    val mScroller: Scroller = Scroller(context)
    val mVelocityTracker = VelocityTracker.obtain()
    var mLastX = 0
    var mLastY = 0
    private var mLastXIntercept = 0
    private var mLastYIntercept = 0
    var mChildWidth = 0
    var mChildIndex = 0
    var mChildrenSize = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measuredWidth = 0
        var measuredHeight = 0
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        if (childCount == 0) {
            setMeasuredDimension(0, 0)
        }else if (heightSpecMode == MeasureSpec.AT_MOST && widthSpecMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measuredWidth = childView.measuredWidth * childCount
            measuredHeight = childView.measuredHeight
            setMeasuredDimension(measuredWidth, measuredHeight)
        }else if (widthSpecMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measuredWidth = childView.measuredWidth * childCount
            setMeasuredDimension(measuredWidth, heightSpecSize)
        }else if (heightSpecMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            setMeasuredDimension(widthSpecSize, childView.measuredHeight)
        } else {
            val childView = getChildAt(0)
            measuredWidth = childView.measuredWidth * childCount
            measuredHeight = childView.measuredHeight
            setMeasuredDimension(measuredWidth,measuredHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        mChildrenSize = childCount
        if (childCount == 0) {
            return
        }
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE) {
                val childWidth = childView.measuredWidth
                mChildWidth = childWidth
                childView.layout(childLeft, 0, childLeft + childWidth, childView.measuredHeight)
                childLeft += childWidth
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercepted = false
        val x = ev?.x?.toInt() ?: 0
        val y = ev?.y?.toInt() ?: 0
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                    intercepted = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastXIntercept
                val deltaY = y - mLastYIntercept
                intercepted = Math.abs(deltaX) > Math.abs(deltaY)
            }
            MotionEvent.ACTION_UP ->
                intercepted = false

        }
        Log.e(TAG, "intercepted= $intercepted")
        mLastX = x
        mLastY = y
        mLastYIntercept = y
        mLastXIntercept = x
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mVelocityTracker.addMovement(event)
        val x = event?.x
        val y = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x?.minus(mLastX)
                val deltaY = y?.minus(mLastY)
                if (!(scrollX < 0 && mChildIndex == 0)) {
                    scrollBy(-deltaX?.toInt()!!, 0)
                }
                Log.e(TAG, "deltaX: $deltaX scrollX: $scrollX")
            }
            MotionEvent.ACTION_UP -> {
                val scrollToChidlIndex = scaleX / mChildWidth
                mVelocityTracker.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker.xVelocity
                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = if (xVelocity > 0) mChildIndex - 1 else mChildIndex + 1
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth
                }
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1))
                val dx = mChildIndex * mChildWidth - scrollX
                smoothScrollBy(dx, 0)
                mVelocityTracker.clear()
                Log.e(TAG, "index: $scrollToChidlIndex dx: $dx mChildIndex: $mChildIndex xVelocity: $xVelocity")
            }
        }
        mLastX = x?.toInt()?:0
        mLastY = y?.toInt()?: 0
        return true
    }




    fun smoothScrollBy(dx: Int, dy: Int) {
        Log.e(TAG, "scrollX:$scrollX  dx: $dx")
        mScroller.startScroll(scrollX, 0, dx, 0, 500)
        invalidate()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker.recycle()
        super.onDetachedFromWindow()
    }

}