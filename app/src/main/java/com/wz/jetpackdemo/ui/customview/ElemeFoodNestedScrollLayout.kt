package com.wz.jetpackdemo.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.ViewCompat
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.extension.dp2px

class ElemeFoodNestedScrollLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NestedScrollingParent2 {
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        TODO("Not yet implemented")
    }

}