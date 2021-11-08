package com.wz.jetpackdemo.ui

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wz.jetpackdemo.extension.closeSafe
import com.wz.jetpackdemo.extension.formatTime
import java.io.*
import java.net.Socket

class SocketChatViewModel : ViewModel() {
    val TAG = SocketChatViewModel::class.java.simpleName

    val MESSAGE_RECEIVE_NEW_MSG = 1
    val MESSAGE_SOCKET_CONNECTED = 2

    val messageState = MutableLiveData<Int>()
    val edittextMsg = MutableLiveData<String>()
    val newMsg = MutableLiveData<String>()
    val activityDestoryedFlag = MutableLiveData(0)

    private var mClientSocket: Socket? = null
    private var mPrintWriter: PrintWriter? = null

    fun connectionTCPServer() {
        var socket:Socket? = null
        while (socket == null) {
            try {
                socket = Socket("localhost", 8688)
                mClientSocket = socket
                mPrintWriter =
                    PrintWriter(BufferedWriter(OutputStreamWriter((socket.getOutputStream()))),true)
                messageState.postValue(MESSAGE_SOCKET_CONNECTED)
                Log.e(TAG, "connect server success!")
            } catch (e: Exception) {
                SystemClock.sleep(1000L)
                Log.e(TAG, "connect tcp server failed,retry...")
                e.printStackTrace()
            }
        }
        var bufferedReader:BufferedReader? = null
        try {
            bufferedReader = BufferedReader(InputStreamReader(socket?.getInputStream()))
            while (activityDestoryedFlag.value == 0) {
                val msg = bufferedReader.readLine()
                Log.e(TAG, "receive: $msg")
                if (msg != null) {
                    val time = System.currentTimeMillis().formatTime("YYYY-MM-DD HH:mm:ss")
                    val showedMsg = "server $time : $msg\n"
                    newMsg.postValue(showedMsg)
                    messageState.postValue(MESSAGE_RECEIVE_NEW_MSG)
                }
            }
            Log.e(TAG, "quit...")
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            mPrintWriter?.closeSafe()
            bufferedReader?.closeSafe()
            socket.close()
        }

    }

    fun sendMsg(msg: String) {
        if (mPrintWriter != null) {
            Thread{
                mPrintWriter?.println(msg)
            }.start()

            val time = System.currentTimeMillis().formatTime("YYYY-MM-DD HH:mm:ss")
            val showedMsg = "self $time:$msg\n"
            edittextMsg.value = showedMsg
        }
    }

    fun clientClose() {
        if (mClientSocket != null) {
            try {
                mClientSocket?.shutdownInput()
                mClientSocket?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}