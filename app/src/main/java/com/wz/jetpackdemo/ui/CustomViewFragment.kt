package com.wz.jetpackdemo.ui

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.databinding.CustomViewFragmentBinding
import com.wz.jetpackdemo.extension.getScreenMetrics
import com.wz.jetpackdemo.ui.customview.ListViewEx
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class CustomViewFragment : BaseViewBindingFragment<CustomViewFragmentBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.scrollScreenView.smoothScrollTo(-50,-50)
//        ObjectAnimator.ofFloat(binding.scrollScreenView, "translationX", 0f, 100f).setDuration(1000)
//            .start()
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000)
        animator.addUpdateListener {
            val animatedFraction = it.animatedFraction
            binding.scrollScreenView.scrollTo(0 + (100 * animatedFraction).toInt(), 0)
        }
//        animator.start()
        initView()
    }

    private fun initView() {
        val screenWidth = context?.getScreenMetrics()?.widthPixels
        val screenHeight = context?.getScreenMetrics()?.heightPixels
        for (i in 0 until 3) {
            val layout =
                layoutInflater.inflate(R.layout.content_layout, binding.container, false)
            layout.layoutParams.width = screenWidth ?: 0
            val titleView = layout.findViewById<TextView>(R.id.title)
            titleView.text = "page:${i + 1}"
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0))
            createList(layout)
            binding.container.addView(layout)
        }
    }

    private fun createList(layout: View?) {
        val listView = layout?.findViewById<ListViewEx>(R.id.list)
        val datas = mutableListOf<String>()
        for (i in 0..50) {
            datas.add("name: $i")
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.content_list_item, R.id.name, datas)
        listView?.adapter = adapter
        listView?.setOnItemClickListener { _, _, _, _ ->
            Toast.makeText(requireContext(), "click item", Toast.LENGTH_LONG).show()

        }
    }

}