package com.wz.jetpackdemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class NestedPagerAdapter(fragmentManager: FragmentManager,var fragments:List<Fragment>) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}