package com.wz.jetpackdemo.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatTime(pattern: String):String {
    val simpleDateFormat = SimpleDateFormat(pattern, Locale.CHINA)
    return simpleDateFormat.format(Date(this))
}