package com.wz.jetpackdemo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wz.jetpackdemo.adapter.NestedPagerAdapter
import com.wz.jetpackdemo.databinding.NestedScrollFragmentBinding
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class NestedScrollFragment : BaseViewBindingFragment<NestedScrollFragmentBinding>() {
    private val mTitles = arrayOf("点餐", "评价", "商家")
    private val fragments by lazy {
        mutableListOf<Fragment>()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        fragments.add(FoodFragment.getInstance())
        fragments.add(FoodFragment.getInstance())
        fragments.add(FoodFragment.getInstance())
        binding.vp.adapter = NestedPagerAdapter(requireFragmentManager(), fragments)
        binding.stl.setViewPager(binding.vp,mTitles)
    }

}
