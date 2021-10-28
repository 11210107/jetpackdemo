package com.wz.jetpackdemo.ui.user

import android.content.Intent
import android.os.Bundle
import com.wz.jetpackdemo.databinding.UserDetailActivityBinding
import com.wz.jetpackdemo.ui.BaseViewBindingActivity
import com.wz.jetpackdemo.ui.StockActivity

class UserDetailActivity: BaseViewBindingActivity<UserDetailActivityBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvToStock.setOnClickListener {
            val intent = Intent(this, StockActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getViewBinding()=UserDetailActivityBinding.inflate(layoutInflater)

}