package com.wz.jetpackdemo.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.adapter.FoodAdapter
import com.wz.jetpackdemo.databinding.FoodFragmentBinding
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class FoodFragment : BaseViewBindingFragment<FoodFragmentBinding>() {
    val TAG = FoodFragment::class.java.simpleName
    companion object{
        @JvmStatic
        fun getInstance():FoodFragment {
            return FoodFragment()
        }
    }
    private var mFooterView: View? = null
    private val mDatas by lazy {
        mutableListOf<Int>()
    }
    private val mAdapter by lazy {
        FoodAdapter(R.layout.item_food_layout)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(TAG,"onCreateView")
        mFooterView = inflater.inflate(R.layout.item_food_footer_layout, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG,"onViewCreated")
        binding.recycleView.adapter = mAdapter
        initData()
        mFooterView?.let {
            mAdapter.addFooterView(it)
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            Log.e(TAG,"onItemClick")
        }
    }

    private fun initData() {
        for (i in 0 until 32) {
            mDatas.add(i)
        }
        mAdapter.data = mDatas
    }
}