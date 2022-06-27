package com.wz.jetpackdemo

import android.content.Context

fun Context.dp2px(dpValue: Int): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}