package com.wz.jetpackdemo

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
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
    val CHANNEL = "AIDLService"
    val mIsServiceDestoryed: AtomicBoolean = AtomicBoolean(false)
    @SuppressLint("RemoteViewLayout")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate() {
        super.onCreate()
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notification :Notification
        val remoteViews = RemoteViews(packageName,R.layout.layout_notification)
        remoteViews.setTextViewText(R.id.msg, "jetpackdemo")
        remoteViews.setTextViewText(R.id.open_activity, "this is content")
        remoteViews.setImageViewResource(R.id.icon, R.mipmap.ic_shark)
        remoteViews.setOnClickPendingIntent(R.id.open_activity, pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL, "aidl", NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.description = "notification description"
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
            notificationManager.createNotificationChannel(notificationChannel)
            notification = NotificationCompat.Builder(this, CHANNEL)
//                .setContentTitle("This is content title")
//                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_shark)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_shark))
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .build()
        } else {
            notification = NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle("This is content title")
                .setContentText("his is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_shark)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_shark))
                .setContentIntent(pendingIntent)
                .build()
        }
        startForeground(1, notification)
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