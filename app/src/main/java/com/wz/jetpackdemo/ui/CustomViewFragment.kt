package com.wz.jetpackdemo.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.databinding.CustomViewFragmentBinding
import com.wz.jetpackdemo.extension.getScreenMetrics
import com.wz.jetpackdemo.ui.customview.ListViewEx
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment

class CustomViewFragment : BaseViewBindingFragment<CustomViewFragmentBinding>() {

    val TAG = CustomViewFragment::class.java.simpleName

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
        windowManager()
    }
    var inViewX = 0
    var inViewY = 0
    var inWindowX = 0
    var inWindowY = 0
    private fun windowManager() {
        val button = Button(context)
        button.text = "window add"
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            0,
            PixelFormat.TRANSPARENT
        )
        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(button, layoutParams)
        val gestureDetector = GestureDetector(context,object :GestureDetector.SimpleOnGestureListener(){
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return false
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                inViewX = e1?.x?.toInt()?:0
                inViewY = e1?.y?.toInt()?:0
                inWindowX = e2?.rawX?.toInt()?:0
                inWindowY = e2?.rawY?.toInt()?:0
                Log.e(TAG,"inViewX:$inViewX inWindowX:$inWindowX inWindowY:$inWindowY inViewY:$inViewY")
                updateViewLocation(inWindowX - inViewX ,inWindowY - inViewY - button.measuredHeight,windowManager,layoutParams,button)
                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        })
        button.setOnTouchListener { v, event -> gestureDetector.onTouchEvent(event) }

    }

    private fun updateViewLocation(x: Int, y: Int, windowManager: WindowManager, layoutParams: WindowManager.LayoutParams,view: View) {
        layoutParams.x = x
        layoutParams.y = y
        windowManager.updateViewLayout(view,layoutParams)
    }

    private fun createList(layout: View?) {
        val listView = layout?.findViewById<ListViewEx>(R.id.list)
        val itemAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_item)
        val layoutAnimationController = LayoutAnimationController(itemAnimation)
        layoutAnimationController.delay = 0.5f
        layoutAnimationController.order = LayoutAnimationController.ORDER_NORMAL
        listView?.layoutAnimation = layoutAnimationController

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