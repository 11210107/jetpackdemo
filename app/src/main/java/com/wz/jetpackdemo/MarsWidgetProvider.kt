package com.wz.jetpackdemo

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.RemoteViews
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock


class MarsWidgetProvider : AppWidgetProvider() {
    val TAG = MarsWidgetProvider::class.java.simpleName
    val CLICK_ACTION = "com.wz.jetpackdemo.action"
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i(TAG, "onReceive:action = ${intent?.action}")
        if (intent?.action.equals(CLICK_ACTION)) {
            Thread{
                val srcbBitmap =
                    BitmapFactory.decodeResource(context?.resources, R.mipmap.ic_launcher)
                val appWidgetManager = AppWidgetManager.getInstance(context)
                for (i in 0 until 37) {
                    val degree = (i * 10) % 360
                    val remoteViews = RemoteViews(context?.packageName, R.layout.widget)
                    remoteViews.setImageViewBitmap(
                        R.id.iv_widget,
                        rotateBitmap(context!!, srcbBitmap, degree.toFloat())
                    )
                    val intentClick = Intent()
                    intentClick.action = CLICK_ACTION
                    intentClick.component = ComponentName(context,MarsWidgetProvider::class.java)
                    val broadcast = PendingIntent.getBroadcast(context, 0, intentClick, PendingIntent.FLAG_ONE_SHOT)
                    remoteViews.setOnClickPendingIntent(R.id.iv_widget, broadcast)
                    appWidgetManager.updateAppWidget(
                        ComponentName(
                            context,
                            MarsWidgetProvider::class.java
                        ), remoteViews)
                    SystemClock.sleep(30)
                }

            }.start()
        }

    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i(TAG, "onUpdate")
        val counter = appWidgetIds?.size?:0
        Log.i(TAG, "counter:$counter")
        for (i in 0 until counter) {
            val appWidgetId = appWidgetIds?.get(i)
            onWidgetUpdate(context,appWidgetManager,appWidgetId)
        }
    }

    private fun onWidgetUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int?
    ) {
        Log.i(TAG, "onWidgetUpdate appWidgetId:$appWidgetId")
        val remoteViews = RemoteViews(context?.packageName, R.layout.widget)
        val intentClick = Intent()
        intentClick.action = CLICK_ACTION
        intentClick.component = ComponentName(context!!,MarsWidgetProvider::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0)
        remoteViews.setOnClickPendingIntent(R.id.iv_widget, pendingIntent)
        appWidgetId?.let {
            appWidgetManager?.updateAppWidget(appWidgetId,remoteViews)
        }
    }


    private fun rotateBitmap(context: Context, srcbBitmap: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.reset()
        matrix.setRotate(degree)
        return Bitmap.createBitmap(srcbBitmap, 0, 0, srcbBitmap.width, srcbBitmap.height, matrix, true)
    }

}