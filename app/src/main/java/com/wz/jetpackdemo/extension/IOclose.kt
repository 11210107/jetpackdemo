package com.wz.jetpackdemo.extension

import java.io.Closeable
import java.io.IOException

fun Closeable.closeSafe() {
    try {
        if (this != null) {
            this.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

}