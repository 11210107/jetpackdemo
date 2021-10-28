package com.wz.jetpackdemo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.wz.jetpackdemo.MainActivity
import com.wz.jetpackdemo.databinding.StockActivityBinding
import com.wz.jetpackdemo.repository.UserRepository

class StockActivity:BaseViewBindingActivity<StockActivityBinding>() {
    val TAG = StockActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvStock.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        Log.e(TAG, "sUserId:${UserRepository.sUserId}")
    }
    override fun getViewBinding() = StockActivityBinding.inflate(layoutInflater)
}