package com.wz.jetpackdemo

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

class MessengerService:Service() {
    val TAG = MessengerService::class.java.simpleName

    private class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0x01 -> {
                    Log.e(
                        "MessengerService",
                        "receive msg from Client:${msg.getData().getString("msg")}"
                    )
                    val client = msg.replyTo
                    val msgReply = Message.obtain(null, 0x02)
                    val bundle = Bundle()
                    bundle.putString("reply", "yes! got it,call you late")
                    msgReply.data = bundle
                    client.send(msgReply)
                }

                else -> super.handleMessage(msg)
            }
        }
    }

    val mMessenger = Messenger(MessengerHandler())

    override fun onBind(intent: Intent?): IBinder? {
        return mMessenger.binder
    }

}