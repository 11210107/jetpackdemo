package com.wz.jetpackdemo.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.abs

class TouchFramelayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val TAG = TouchFramelayout::class.java.simpleName

//    var mLastX = 0
//    var mLastY = 0
//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        var intercepted = false
//        val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
//        val x = ev?.rawX
//        val y = ev?.rawY
//        when (ev?.action) {
//            MotionEvent.ACTION_DOWN ->
//                intercepted = false
//            MotionEvent.ACTION_MOVE ->{
//                val deltaX = abs(x?.minus(mLastX) ?: 0f)
//                val deltaY = abs(y?.minus(mLastY) ?: 0f)
//                Log.e(TAG, "move,deltaX: $deltaX, deltaY:$deltaY , scaledTouchSlop:$scaledTouchSlop ")
//                intercepted = deltaX!! < deltaY!!
//            }
//            MotionEvent.ACTION_UP ->
//                intercepted = false
//        }
//        mLastX = x?.toInt()?:0
//        mLastY = y?.toInt()?: 0
//
//        return intercepted
//    }

}