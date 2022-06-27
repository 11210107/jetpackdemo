package com.wz.jetpackdemo.extension

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.wz.jetpackdemo.JetpackApplication

fun Context.getScreenMetrics():DisplayMetrics {
    val windowService = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowService.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}

fun Float.dp2px():Int{
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        JetpackApplication.context.resources.displayMetrics
    ).toInt()
}