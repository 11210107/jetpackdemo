package com.wz.jetpackdemo.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.adapter.MinePagerAdapter
import com.wz.jetpackdemo.databinding.MineFragmentBinding
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class MineFragment : BaseViewBindingFragment<MineFragmentBinding>() {
    val TAG = MineFragment::class.java.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "onAttach")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated")
        binding.tvLoginRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_fragment_mine_to_fragment_main)
        }

        binding.tvLoginMain.setOnClickListener {
            val action =
                MineFragmentDirections.actionFragmentMineToFragmentHome("token", 10001)
            Navigation.findNavController(it).navigate(action)
        }
        binding.tvUserDetail.setOnClickListener {
            startActivity(Intent(activity, UserDetailActivity::class.java))
        }
        val minePagerAdapter = MinePagerAdapter()
        binding.viewPager.adapter = minePagerAdapter
        minePagerAdapter.mDatas = mutableListOf<Int>().apply {
            add(R.mipmap.ic_shark)
            add(R.mipmap.ic_launcher)
        }
        minePagerAdapter.notifyDataSetChanged()
        binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")
    }
}