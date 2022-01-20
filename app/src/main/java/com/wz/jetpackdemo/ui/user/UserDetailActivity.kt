package com.wz.jetpackdemo.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.wz.jetpackdemo.databinding.UserDetailActivityBinding
import com.wz.jetpackdemo.ui.BaseViewBindingActivity
import com.wz.jetpackdemo.ui.StockActivity
import com.wz.jetpackdemo.viewmodel.UserModel

class UserDetailActivity: BaseViewBindingActivity<UserDetailActivityBinding>() {
    val TAG = UserDetailActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvToStock.setOnClickListener {
            val intent = Intent(this, StockActivity::class.java)
            startActivity(intent)
        }
        val userModel = ViewModelProvider(this).get(UserModel::class.java)

        Log.e(TAG, "isFirstLaunch:${userModel.isFirstLaunch}")
        userModel.isFirstLaunch = false

    }

    override fun getViewBinding()=UserDetailActivityBinding.inflate(layoutInflater)

}