package com.wz.jetpackdemo.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout

class StickyLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val TAG = StickyLayout::class.java.simpleName
    var mHeader: View? = null
    var mContent: View? = null

    val STATUS_EXPANDED = 1
    val STATUS_COLLAPSED = 2
    var mStatus = STATUS_EXPANDED

    //header的高度 单位：px
    var mOriginalHeaderHeight = 0
    var mHeaderHeight = 0

    var mTouchSlop = 0
    //分别记录上次滑动的坐标
    var mLastX = 0
    var mLastY = 0

    //分别记录上次滑动的坐标（onInterceptTouchEvent）
    var mLastXIntercept = 0
    var mLastYIntercept = 0

    val TAN = 2//用来控制滑动角度，仅当角度a满足 tan a = deltaX / deltaY >2 进行滑动
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus && (mHeader == null || mContent == null)) {
            initData()
        }
    }

    private fun initData() {
        val headerId = resources.getIdentifier("header", "id", context.packageName)
        val contentId = resources.getIdentifier("content", "id", context.packageName)
        if (headerId != 0 && contentId != 0) {
            mHeader = findViewById(headerId)
            mContent = findViewById(contentId)
            mOriginalHeaderHeight = mHeader?.measuredHeight ?: 0
            mHeaderHeight = mOriginalHeaderHeight
            mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
            Log.e(TAG, "mTouchSlop:$mTouchSlop")

        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercepted = 0
        val x = ev?.x
        val y = ev?.y
        when (ev?.action) {
            MotionEvent.ACTION_DOWN ->{
                mLastXIntercept = x?.toInt()?:0
                mLastYIntercept = y?.toInt() ?: 0
                mLastX = x?.toInt()?:0
                mLastY = y?.toInt()?:0
                intercepted = 0
            }
            MotionEvent.ACTION_MOVE->{
                val deltaX = x?.minus(mLastXIntercept)
                val deltaY = y?.minus(mLastYIntercept)?:0
                if (mStatus == STATUS_EXPANDED && deltaY.toInt() <= -mTouchSlop) {
                    intercepted = 1
                }
            }
            MotionEvent.ACTION_UP->{
                intercepted = 0
                mLastXIntercept = 0
                mLastYIntercept = 0
            }
        }
        Log.e(TAG, "intercepted: $intercepted")

        return intercepted != 0
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x?.toInt() ?: 0
        val y = event?.y?.toInt() ?: 0
        when (event?.action) {
            MotionEvent.ACTION_MOVE ->{
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                mHeaderHeight += deltaY
                setHeaderHeight(mHeaderHeight)
            }
            MotionEvent.ACTION_UP->{
                var destHeight = 0
                if (mHeaderHeight <= mOriginalHeaderHeight * 0.5) {
                    destHeight = 0
                    mStatus = STATUS_COLLAPSED
                } else {
                    destHeight = mOriginalHeaderHeight
                    mStatus = STATUS_EXPANDED
                }
                smoothSetHeaderHeight(mHeaderHeight,destHeight,500)
            }
        }

        return super.onTouchEvent(event)
    }

    private fun smoothSetHeaderHeight(from: Int, to: Int, duration: Int) {
        val frameCount = duration / 1000 * 30 + 1
        val partation = (to - from) / frameCount
        Thread{
            for (i in 0 until frameCount) {
                var height = 0
                if (i == frameCount - 1) {
                    height = to
                } else {
                    height = (from + partation * i)
                }
                post {
                    setHeaderHeight(height)
                }
                Thread.sleep(10)
            }
        }.start()
    }

    private fun setHeaderHeight(mHeaderheight: Int) {
        var height = mHeaderheight
        Log.e(TAG, "setHeaderHeight height=$height")
        if (height < 0) {
            height = 0
        }else if (height > mOriginalHeaderHeight) {
            height = mOriginalHeaderHeight
        }
        if (mHeaderHeight != height || true) {
            mHeaderHeight = height
            mHeader?.layoutParams?.height = mHeaderHeight
            mHeader?.requestLayout()
        }
    }

}