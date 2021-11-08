package com.wz.jetpackdemo.aidlimpl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.wz.jetpackdemo.BinderPoolService
import com.wz.jetpackdemo.IBinderPool
import java.util.concurrent.CountDownLatch


class BinderPool {
    val TAG = BinderPool::class.java.simpleName
    private lateinit var mConnectBinderPoolCountDownLatch:CountDownLatch
    var mBinderPool: IBinderPool? = null
    var mContext:Context? = null

    private constructor(context: Context){
        mContext = context.applicationContext
        connnectBinderPoolService()
    }

    companion object{
        val BINDER_NONE = -1
        val BINDER_COMPUTE =0
        val BINDER_SECURITY_CENTER = 1

        private var instance:BinderPool? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this){
                instance?: BinderPool(context).also { instance = it }
            }
    }

    @Synchronized
    private fun connnectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = CountDownLatch(1)
        val service = Intent(mContext, BinderPoolService::class.java)
        mContext?.bindService(service,mBinderPoolConnection,Context.BIND_AUTO_CREATE)
        mConnectBinderPoolCountDownLatch.await()
    }

    fun queryBinder(binderCode: Int): IBinder? {
        return  mBinderPool?.queryBinder(binderCode)
    }

    private val mBinderPoolConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG,"BinderPoolService connected")
            mBinderPool = IBinderPool.Stub.asInterface(service)
            mBinderPool?.asBinder()?.linkToDeath(mBinderPoolDeathRecipient,0)
            mConnectBinderPoolCountDownLatch.countDown()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG,"BinderPoolService disconnected")
        }

    }

    private val mBinderPoolDeathRecipient = IBinder.DeathRecipient {
        Log.e(TAG, "binder died.")
        unLink()
        mBinderPool = null
        connnectBinderPoolService()

    }

    private fun unLink() {
        mBinderPool?.asBinder()?.unlinkToDeath(mBinderPoolDeathRecipient, 0)
    }

}

class BinderPoolImpl : IBinderPool.Stub() {
    override fun queryBinder(binderCode: Int): IBinder? {
        when (binderCode) {
            BinderPool.BINDER_SECURITY_CENTER -> return SecurityCenterImpl()
            BinderPool.BINDER_COMPUTE -> return ComputeImpl()
        }
        return null
    }

}