package com.wz.jetpackdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import com.wz.jetpackdemo.extension.closeSafe
import java.io.*
import java.net.ServerSocket
import java.net.Socket

class TCPServerService:Service() {
    val TAG = TCPServerService::class.java.simpleName
    private val mDefinedMessages = arrayOf(
        "你好啊，哈哈",
        "请问你叫什么名字呀？",
        "今天北京天气不错啊，shy",
        "你知道吗？我可是可以和多个人同时聊天的哦",
        "给你讲个笑话吧：据说爱笑的人运气不会太差，不知道真假。"
    )
    private var mIsServiceDestoryed = false
    override fun onCreate() {
        Thread {
            var serverSocket:ServerSocket? = null
            try {
                serverSocket = ServerSocket(8688)
            } catch (e: IOException) {
                Log.e(TAG, "establish tcp server failed,port:8688")
                e.printStackTrace()
                return@Thread
            }
            while (!mIsServiceDestoryed) {
                try {
                    val client = serverSocket.accept()
                    Log.e(TAG, "accept")
                    Thread {
                        try {
                            responseClient(client)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()

        super.onCreate()
    }

    private fun responseClient(client: Socket?) {
        val input = BufferedReader(InputStreamReader(client?.getInputStream()))
        val output =
            PrintWriter(BufferedWriter(OutputStreamWriter(client?.getOutputStream())), true)
        output.println("welcome to chatroom!")
        Log.e(TAG, "responseClient:$mIsServiceDestoryed")
        while (!mIsServiceDestoryed) {
            Log.e(TAG, "responseClient:$client")
            val str = input.readLine()
            Log.e(TAG, "msg from client:$str")
            if (TextUtils.isEmpty(str)) {
                break
            }
            val random = (mDefinedMessages.indices).random()
            val msg = mDefinedMessages[random]
            output.println(msg)
            Log.e(TAG,"send: $msg")
        }
        Log.e(TAG, "client quit. $mIsServiceDestoryed")
        input.closeSafe()
        output.closeSafe()
        client?.close()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mIsServiceDestoryed = true
        super.onDestroy()
    }

}