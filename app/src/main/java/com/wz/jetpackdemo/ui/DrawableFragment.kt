package com.wz.jetpackdemo.ui

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.databinding.DrawableFragmentBinding
import com.wz.jetpackdemo.extension.RealSubject
import com.wz.jetpackdemo.ui.main.BaseViewBindingFragment
import kotlin.properties.Delegates

class DrawableFragment : BaseViewBindingFragment<DrawableFragmentBinding>() {
    val TAG = DrawableFragment::class.java.simpleName
    var message: String by Delegate()
    val dialog: DialogFragment by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DialogFragment(R.layout.content_layout) }
    var name :String by Delegates.observable("<no name>"){property, oldValue, newValue ->
        Log.e(TAG, "property:$property oldValue:$oldValue newValue:$newValue")
    }
    @SuppressLint("ObjectAnimatorBinding")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val background = binding.ivOval.background as TransitionDrawable
        background.startTransition(1000)
        val ovalSlide = AnimationUtils.loadAnimation(context, R.anim.oval_slide)
        ovalSlide.repeatMode = Animation.RESTART

//        binding.ivOval.startAnimation(ovalSlide)
        val tanslationAnim = ObjectAnimator.ofFloat(binding.ivOval,"translationY",0f,600f)
        val scaleX = ObjectAnimator.ofFloat(binding.ivOval,"scaleX",0.5f,1.5f)
        val scaleY = ObjectAnimator.ofFloat(binding.ivOval,"scaleY",0.5f,1.5f)
        val alphaAnim = ObjectAnimator.ofFloat(binding.ivOval,"alpha",0.25f,1f)
        val colorAnim: ValueAnimator = ObjectAnimator.ofInt(binding.ivOval, "backgroundColor",  Color.parseColor("#0093FB"), Color.parseColor("#52B931"))
        colorAnim.duration =  3000
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.repeatCount = ValueAnimator.INFINITE
        colorAnim.repeatMode = ValueAnimator.RESTART
//        colorAnim.start()
        scaleX.repeatCount = ValueAnimator.INFINITE
        scaleX.repeatMode = ValueAnimator.REVERSE
        scaleY.repeatCount = ValueAnimator.INFINITE
        scaleY.repeatMode = ValueAnimator.REVERSE
        alphaAnim.repeatCount = ValueAnimator.INFINITE
        alphaAnim.repeatMode = ValueAnimator.REVERSE
        tanslationAnim.repeatCount = ValueAnimator.INFINITE
        tanslationAnim.repeatMode = ValueAnimator.REVERSE
        val set = AnimatorSet()
        set.duration = 2000
        set.playTogether(tanslationAnim,scaleX,scaleY,alphaAnim)
        set.start()
        binding.ivOval.postDelayed({
//            set.end()
        },5000)
//        val ofInt = ObjectAnimator.ofInt(ViewWrapper(binding.ivOval), "width", 500)
//        ofInt.duration = 2000
//        ofInt.start()
        val valueAnimator = ValueAnimator.ofFloat(0f, 100f)
        val evaluator = IntEvaluator()
        valueAnimator.addUpdateListener {
            Log.e(TAG, "current value:${it.animatedValue} fraction:${it.animatedFraction}")
            evaluator.evaluate(it.animatedFraction,0,100)
        }
        valueAnimator.duration = 5000
        valueAnimator.start()

        var subject = RealSubject()
        subject.buy()
        message = "hello delegate"
        Log.e(TAG,"DrawableFragment $message")
        dialog.show(parentFragmentManager,"dialog")
        name = "wangzhen"
    }

}