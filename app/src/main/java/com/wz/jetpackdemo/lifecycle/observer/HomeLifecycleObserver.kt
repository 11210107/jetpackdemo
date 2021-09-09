package com.wz.jetpackdemo.lifecycle.observer

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class HomeLifecycleObserver : LifecycleObserver {
    val TAG = HomeLifecycleObserver::class.java.simpleName
    @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
    fun onStart(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {

        }
    }
    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    fun connect() {
        Log.e(TAG,"connect")
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    fun disconnect() {
        Log.e(TAG,"disconnect")
    }
}