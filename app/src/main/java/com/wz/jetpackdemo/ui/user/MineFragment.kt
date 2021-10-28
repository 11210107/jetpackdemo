package com.wz.jetpackdemo.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.databinding.MineFragmentBinding
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class MineFragment : BaseViewBindingFragment<MineFragmentBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLoginRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragment_mine_to_fragment_main)
        }

        binding.tvLoginMain.setOnClickListener {
            val action =
                MineFragmentDirections.actionFragmentMineToFragmentHome("token", 10001)
            Navigation.findNavController(it).navigate(action)
        }
        binding.tvUserDetail.setOnClickListener {
            startActivity(Intent(activity,UserDetailActivity::class.java))
        }
    }
}