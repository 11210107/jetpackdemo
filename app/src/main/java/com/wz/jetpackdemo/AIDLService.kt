package com.wz.jetpackdemo

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import androidx.annotation.RequiresApi
import com.wz.jetpackdemo.extension.lock
import com.wz.jetpackdemo.extension.rlock
import com.wz.jetpackdemo.model.Book
import com.wz.jetpackdemo.model.INewBookArraivedListener
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

class AIDLService:Service() {
    val TAG = AIDLService::class.java.simpleName
    private val books = mutableListOf<Book>()
    private val mListenerList = CopyOnWriteArrayList<INewBookArraivedListener>()
    private val mListeners = RemoteCallbackList<INewBookArraivedListener>()

    val mIsServiceDestoryed: AtomicBoolean = AtomicBoolean(false)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate() {
        super.onCreate()
        books.add(Book(0, "Android开发艺术探索"))
        books.add(Book(1, "深入理解Java虚拟机"))
        Thread {
            while (!mIsServiceDestoryed.get()) {
                Thread.sleep(5000L)
                val bookId = books.size + 1
                onNewBookArrived(Book(bookId, "new book# $bookId"))
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun onNewBookArrived(book: Book) {
        books.add(book)
//        Log.e(TAG, "onNewBookArrived,notify listeners:${mListenerList.size}")
//        mListenerList.forEach {
//            it.onNewBookArrived(book)
//        }
        Log.e(TAG, "onNewBookArrived,notify listeners:${mListeners.registeredCallbackCount}")
        for (i in 0 until mListeners.beginBroadcast()) {
            val broadcastItem = mListeners.getBroadcastItem(i)
            broadcastItem?.onNewBookArrived(book)
        }
        mListeners.finishBroadcast()

    }

    val mIBookManager = object :IBookManager.Stub(){
        override fun getBookList(): MutableList<Book> {
            Log.e(TAG,"invoking getBookList() method,now the list is:${books}")
//            Thread.sleep(10000L) //Service ANR
            return rlock {
                books
            }
        }

        override fun addBook(book: Book?) {
            lock {
                if (books == null) {
                    books == mutableListOf<Book>()
                }
                if (book == null) {
                    Log.e(TAG, "book is null in In")
                }
                if (!books.contains(book)) {
                    books.add(book!!)
                }
                Log.e(TAG,"invoking addBook() method , now the list is : $books")
            }
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        override fun registerListener(listener: INewBookArraivedListener?) {
//            if (!mListenerList.contains(listener)) {
//                mListenerList.add(listener)
//            } else {
//                Log.e(TAG, "already exists.")
//            }
            mListeners.register(listener)
            Log.e(TAG, "registerListener,size: ${mListeners.registeredCallbackCount}")
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        override fun unregisterListener(listener: INewBookArraivedListener?) {
//            if (mListenerList.contains(listener)) {
//                mListenerList.remove(listener)
//                Log.e(TAG, "unregister listener succeed")
//            } else {
//                Log.e(TAG,"not found,can not unregister")
//            }
            mListeners.unregister(listener)
            Log.e(TAG, "unregisterListener,current size: ${mListeners.registeredCallbackCount}")
        }

    }


    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind intent = ${intent.toString()}")
        val check =
            checkCallingOrSelfPermission("com.wz.jetpackdemo.permission.ACCESS_BOOK_SERVICE")
        if (check == PackageManager.PERMISSION_DENIED) {
            return null
        }
        return mIBookManager
    }

}