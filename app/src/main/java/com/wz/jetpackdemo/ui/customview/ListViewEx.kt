package com.wz.jetpackdemo.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ListView

class ListViewEx @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ListView(context, attrs, defStyleAttr) {
    var mLastX: Int = 0
    var mLastY: Int = 0

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val x = ev?.x
        val y = ev?.y
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x?.minus(mLastX)
                val deltaY = y?.minus(mLastY)
                if (Math.abs(deltaX!!) > Math.abs(deltaY!!)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        mLastX = x?.toInt() ?: 0
        mLastY = y?.toInt() ?: 0

        return super.dispatchTouchEvent(ev)
    }
}

