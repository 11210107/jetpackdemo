package com.wz.jetpackdemo.extension

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

fun Context.getScreenMetrics():DisplayMetrics {
    val windowService = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowService.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}