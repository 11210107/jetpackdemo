package com.wz.jetpackdemo.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.wz.jetpackdemo.TCPServerService
import com.wz.jetpackdemo.databinding.SocketChatFragmentBinding
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class SocketChatFragment : BaseViewBindingFragment<SocketChatFragmentBinding>() {
    val TAG = SocketChatFragment::class.java.simpleName

    lateinit var viewModel: SocketChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SocketChatViewModel::class.java)
        val intent = Intent(activity, TCPServerService::class.java)
        activity?.startService(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Thread{
            viewModel.connectionTCPServer()
        }.start()
        viewModel.messageState.observe(viewLifecycleOwner,{
            when (it) {
                2 -> binding.btnSend.isEnabled = true
                1 ->{
                    val newMsg = viewModel.newMsg.value
                    Log.e(TAG, "newMsg: $newMsg")
                    val text = binding.tvChatContent.text.toString()
                    binding.tvChatContent.text = text + newMsg
                }
            }

        })
        viewModel.edittextMsg.observe(viewLifecycleOwner,{
            binding.etMessage.setText("")
            val text = binding.tvChatContent.text.toString()
            binding.tvChatContent.text = text + it

        })
        binding.btnSend.setOnClickListener {
            val msg = binding.etMessage.text.toString()
            if (!TextUtils.isEmpty(msg)) {
                viewModel.sendMsg(msg)
            }
        }
    }

    override fun onDestroy() {
        viewModel.activityDestoryedFlag.value = 1
        viewModel.clientClose()
        super.onDestroy()
    }
}