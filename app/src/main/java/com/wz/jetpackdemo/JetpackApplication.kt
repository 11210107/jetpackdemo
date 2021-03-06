package com.wz.jetpackdemo

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.os.Process
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates


class JetpackApplication : Application() {

    companion object {
        val TAG = JetpackApplication::class.java.simpleName
        @JvmStatic
        var context: Application by Delegates.notNull()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        Log.e(TAG, "onCreate:")
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
        val processName = getProcessName(this)
        context = this
        val rootDir: String = MMKV.initialize(this)
        Log.e(TAG, "mmkv root: $rootDir")
        Log.e(TAG, "processName: $processName")
    }

    class ApplicationLifecycleObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onAppForeground() {
            Log.e(TAG,"onAppForeground")

        }
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onAppBackground() {
            Log.e(TAG,"onAppBackground")

        }
    }

    fun getProcessName(cxt: Context): String? {
        val pid = Process.myPid()
        val am = cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }
}