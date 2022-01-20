package com.wz.jetpackdemo.ui

import android.view.View

class ViewWrapper(var mTarget: View) {
    fun getWidth():Int{
        return mTarget.layoutParams.width
    }
    fun setWidth(width: Int) {
        mTarget.layoutParams.width = width
        mTarget.requestLayout()
    }
}