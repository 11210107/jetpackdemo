package com.wz.jetpackdemo.extension

import android.util.Log

class Delegate:Subject {
    val TAG = Delegate::class.java.simpleName
    override fun buy() {
        Log.d(TAG, "Delegate buy")
    }
}