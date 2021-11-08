package com.wz.jetpackdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.wz.jetpackdemo.aidlimpl.BinderPoolImpl

class BinderPoolService:Service() {
    val TAG = BinderPoolService::class.java.simpleName
    private val mBinderPool = BinderPoolImpl()

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind")
        return mBinderPool
    }

}