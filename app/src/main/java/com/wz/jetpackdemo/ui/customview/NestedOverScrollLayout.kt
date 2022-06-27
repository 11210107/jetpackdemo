package com.wz.jetpackdemo.ui.customview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.Scroller
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import com.wz.jetpackdemo.dp2px
import com.wz.jetpackdemo.interpolator.INTERPOLATOR_VISCOUS_FLUID
import com.wz.jetpackdemo.interpolator.ReboundInterpolator
import com.wz.jetpackdemo.util.SmartUtil
import com.wz.jetpackdemo.util.WidgetUtil
import java.lang.Math.log
import kotlin.math.*

class NestedOverScrollLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), NestedScrollingParent3 {
    val TAG = NestedOverScrollLayout::class.java.simpleName
    private var mVelocityTracker = VelocityTracker.obtain()
    private var mScroller = Scroller(context)

    private var mParentHelper: NestedScrollingParentHelper? = null

    private var mTouchSlop: Int = 0
    private var mMinimumVelocity: Float = 0f
    private var mMaximumVelocity: Float = 0f
    private var mCurrentVelocity: Float = 0f


    //阻尼滑动参数
    private val mMaxDragRate = 2.5f
    private val mMaxDragHeight = 250
    private val mScreenHeightPixels = context.resources.displayMetrics.heightPixels

    private var mHandler: Handler? = null
    private var mNestedInProcess = false
    //是否运行过渡滑动
    private var mIsAllowOverScroll = true
    //在子View滑动前，此View需要滑动的距离
    private var mPreConsumedNeeded = 0
    //当前竖直方向translationY的距离
    private var mSpinner = 0f

    private var mReboundAnimator: ValueAnimator? = null
    private var mReboundInterpolator = ReboundInterpolator(INTERPOLATOR_VISCOUS_FLUID)
    //用了实现fling时，先用过渡滑动再回弹的效果
    private var mAnimationRunnable: Runnable? = null
    // 控制fling时等待contentView回到translation = 0 的位置
    private var mVerticalPermit = false

    private var mRefreshContent: View? = null

    init {
        setWillNotDraw(false)
        mHandler = Handler(Looper.getMainLooper())
        mParentHelper = NestedScrollingParentHelper(this)
        ViewConfiguration.get(context).let {
            mTouchSlop = it.scaledTouchSlop
            mMinimumVelocity = it.scaledMinimumFlingVelocity.toFloat()
            mMaximumVelocity = it.scaledMaximumFlingVelocity.toFloat()
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val childCount = super.getChildCount()
        for (i in 0 until childCount) {
            val child = super.getChildAt(i)
            if (SmartUtil.isContentView(child)) {
                mRefreshContent = child
                break
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var minimumWidth = 0
        var minimumHeight = 0
        val thisView = this
        for (i in 0 until super.getChildCount()) {
            val childView = super.getChildAt(i)
            if (childView == null || childView.visibility == View.GONE) continue
            if (mRefreshContent == childView) {
                mRefreshContent?.let {
                    val lp = it.layoutParams
                    val marginLayoutParams = lp as MarginLayoutParams
                    val leftMargin = marginLayoutParams?.leftMargin ?: 0
                    val rightMargin = marginLayoutParams?.rightMargin ?: 0
                    val bottomMargin = marginLayoutParams.bottomMargin ?: 0
                    val topMargin = marginLayoutParams?.topMargin ?: 0
                    val widhtSpec = getChildMeasureSpec(
                        widthMeasureSpec,
                        thisView.paddingLeft + thisView.paddingRight + leftMargin + rightMargin,lp.width
                    )
                    val heightSpec = getChildMeasureSpec(
                        heightMeasureSpec,
                        thisView.paddingTop + thisView.paddingBottom + topMargin + bottomMargin,
                        lp.height
                    )
                    it.measure(widhtSpec, heightSpec)
                    minimumWidth += it.measuredWidth
                    minimumHeight += it.measuredHeight
                }
            }

        }
        minimumWidth += thisView.paddingLeft + thisView.paddingRight
        minimumHeight += thisView.paddingTop + thisView.paddingBottom
        super.setMeasuredDimension(
            resolveSize(
                minimumWidth.coerceAtLeast(super.getSuggestedMinimumHeight()),
                widthMeasureSpec
            ),
            resolveSize(
                minimumHeight.coerceAtLeast(super.getSuggestedMinimumHeight()),
                heightMeasureSpec
            )
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val thisView = this
        for (i in 0 until super.getChildCount()) {
            val childView = super.getChildAt(i)
            if (childView == null || childView.visibility == View.GONE) continue
            if (mRefreshContent == childView) {
                mRefreshContent?.let {
                    val layoutParams = it.layoutParams
                    val marginLayoutParams = layoutParams as MarginLayoutParams
                    val leftMargin = marginLayoutParams?.leftMargin?:0
                    val topMargin = marginLayoutParams?.topMargin ?: 0

                    val left = leftMargin + thisView.paddingLeft
                    val top = topMargin + thisView.paddingTop
                    val right = left + it.measuredWidth
                    val bottom = top + it.measuredHeight
                    it.layout(left, top, right, bottom)
                }
            }
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev ?: return false
        //如果处于嵌套滑动状态，正常下发，以确保嵌套滑动的正常运行。
        if (mNestedInProcess) {
            return super.dispatchTouchEvent(ev)
        }
        val actionMasked = ev.actionMasked
        if (interceptReboundByAction(actionMasked)) {
            return false
        }
        return super.dispatchTouchEvent(ev)
    }
    //根据条件是否拦截事件，如果是down事件，会终止回弹动画。
    private fun interceptReboundByAction(action: Int):Boolean {
        if (action == MotionEvent.ACTION_DOWN) {
            mReboundAnimator?.let {
                it.duration = 0
                it.cancel()
            }
            mReboundAnimator = null
        }
        return mReboundAnimator == null
    }
    /*嵌套滑动开始调用
    *方法返回ture时，表示此parent能够接收此次嵌套滑动事件，返回false，不接收此次嵌套滑动事件，后续方法都不会调用。
    */
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.e(TAG, "onStartNestedScroll")
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }
    //当onStartNestedScroll()方法返回true后，此方法会立即调用，可以在此方法做每次嵌套滑动的初始化工作。
    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.e(TAG, "onNestedScrollAccepted")
        mParentHelper?.onNestedScrollAccepted(child, target, axes, type)
        mPreConsumedNeeded = reverseComputeFromDamped2Origin(mSpinner)
        mNestedInProcess = true

        interceptReboundByAction(MotionEvent.ACTION_DOWN)
    }
    //当嵌套滑动即将结束时，会调用此方法
    override fun onStopNestedScroll(target: View, type: Int) {
        Log.e(TAG, "onStopNestedScroll")
        mParentHelper?.onStopNestedScroll(target, type)
        mNestedInProcess = false
        overSpinner()
    }
    //在Child滑动之前调用，可让Parent先消耗一定距离
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.e(TAG, "onNestedPreScroll")
        if (dy == 0) return
        //触摸事件才开始处理
        if (type == ViewCompat.TYPE_TOUCH) {
            val consumedY:Int
            //两者异向，加剧过渡滑动
            if (mPreConsumedNeeded * dy < 0) {
                consumedY = dy
                mPreConsumedNeeded -= dy
                moveTranslation(computeDampedSlipDistance(mPreConsumedNeeded))
            } else {
                //两者向同，需先将mPreConsumedNeed消耗掉
                val lastConsumedNeeded = mPreConsumedNeeded
                if (dy.absoluteValue > mPreConsumedNeeded.absoluteValue) {
                    consumedY = mPreConsumedNeeded
                    mPreConsumedNeeded = 0
                } else {
                    consumedY = dy
                    mPreConsumedNeeded -= dy
                }
                if (lastConsumedNeeded != mPreConsumedNeeded) {
                    moveTranslation(computeDampedSlipDistance(mPreConsumedNeeded))
                }
            }
            consumed[1] = consumedY
        }
    }
    //此 Parent 正在执行嵌套滑动时，会调用此方法，在这里实现嵌套滑动的逻辑
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        Log.e(TAG, "onNestedScroll")
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedScrollInternal(dyUnconsumed, type, null)
        }
    }
    // 此 Parent 正在执行嵌套滑动时，会调用此方法，在这里实现嵌套滑动的逻辑
    // 与上面方法的区别，此方法多了个 consumed 参数，用于存放嵌套滑动执行完后，
    // 被次 parent 消耗的滑动距离
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        Log.e(TAG, "onNestedScroll,dyUnconsumed = $dyUnconsumed")
        if (type == ViewCompat.TYPE_TOUCH) {
            onNestedScrollInternal(dyUnconsumed, type, consumed)
        } else {
            consumed[1] += dyUnconsumed
        }
    }


    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        // 返回 true，会接管子 View 的 fling 事件，子 View 的 fling 代码不会执行。
        return startFlingIfNeed(-velocityY)
    }

    private fun startFlingIfNeed(flingVelocity: Float): Boolean {
        val velocity = if (flingVelocity == 0f) mCurrentVelocity else flingVelocity
        if (velocity.absoluteValue > mMinimumVelocity) {
            if (velocity < 0 && mIsAllowOverScroll && mSpinner == 0f
                || velocity > 0 && mIsAllowOverScroll && mSpinner == 0f
            ) {
                mScroller.fling(0, 0, 0, (-velocity).toInt(), 0, 0, -Int.MAX_VALUE, Int.MAX_VALUE)
                mScroller.computeScrollOffset()
                val thisView: View = this
                thisView.invalidate()
            }
        }

        return false
    }
    // 这个方法会被多次调用，直至满足过度滑动的条件：
    // finalY < 0 && WidgetUtil.canRefresh(mRefreshContent, null)
    // || finalY > 0 && WidgetUtil.canLoadMore(mRefreshContent, null
    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            val finalY = mScroller.finalY
            if (finalY < 0 && WidgetUtil.canRefresh(mRefreshContent, null)
                || finalY > 0 && WidgetUtil.canLoadMore(mRefreshContent, null)
            ) {
                if (mVerticalPermit) {
                    val velocity = if (finalY > 0) -mScroller.currVelocity else mScroller.currVelocity
                    // 可以过度滑动后，通过动画模拟惯性滑动的过程
                    animSpinnerBounce(velocity)
                }
                mScroller.forceFinished(true)
            } else {
                mVerticalPermit = true
                val thisView = this
                thisView.invalidate()
            }
        }
    }

    /**
     * 惯性滑动后回弹动画
     * @param velocity 速度
     */
    protected fun animSpinnerBounce(velocity: Float) {
        // 模拟惯性滑动时，回弹动画必须已经停止
        if (mReboundAnimator == null) {
            Log.e(TAG, "animSpinnerBounce = $mSpinner")
            if (mSpinner == 0f && mIsAllowOverScroll) {
                // 执行 BounceRunnable
                mAnimationRunnable = BounceRunnable(velocity, 0)
            }
        }
    }
    private fun onNestedScrollInternal(dyUnconsumed: Int, type: Int, consumed: IntArray?) {
        if (dyUnconsumed == 0) return
        //dy>0 向上滚
        val dy = dyUnconsumed
        Log.e(TAG, "onNestedScrollInternal")
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            //fling不处理，直接消费
            if (consumed != null) {
                consumed[1] += dy
            }
        } else {
            if ((dy < 0 && mIsAllowOverScroll && WidgetUtil.canRefresh(mRefreshContent, null))
                || (dy > 0 && mIsAllowOverScroll && WidgetUtil.canLoadMore(
                    mRefreshContent,
                    null
                ))
            ) {
                mPreConsumedNeeded -= dy
                moveTranslation(computeDampedSlipDistance(mPreConsumedNeeded))
                if (consumed != null) {
                    consumed[1] += dy
                }
            }
        }
    }




    private fun reverseComputeFromDamped2Origin(dampedDistance: Float): Int {
        return if (dampedDistance >= 0) {
            //X = -H * log((1-y/m),100)
            val dragRate = 0.5f
            val m = if (mMaxDragRate < 10) mMaxDragRate * mMaxDragHeight else mMaxDragRate
            val h = (mScreenHeightPixels / 2).coerceAtLeast(this.height)
            val y = dampedDistance
            Log.e(TAG,"reverse ${(-h * log((1-y/m),100f))}")
            ((-h * log((1 - y / m), 100f)) / dragRate).roundToInt()
        } else {
            val dragRate = 0.5f
            val m = if(mMaxDragRate < 10) mMaxDragRate * mMaxDragHeight else mMaxDragRate
            val h = (mScreenHeightPixels / 2).coerceAtLeast(this.height)
            val y = -dampedDistance
            -((-h * log((1 - y / m), 100f)) / dragRate).roundToInt()
        }

    }

    /**
     * 计算阻尼滑动距离
     * @param originTranslation 原始应该滑动的距离
     * @return Float, 计算结果
     */
    private fun computeDampedSlipDistance(originTranslation: Int): Float {
        if (originTranslation >= 0) {
            /**
            final double M = mHeaderMaxDragRate < 10 ? mHeaderHeight * mHeaderMaxDragRate : mHeaderMaxDragRate;
            final double H = Math.max(mScreenHeightPixels / 2, thisView.getHeight());
            final double x = Math.max(0, spinner * mDragRate);
            final double y = Math.min(M * (1 - Math.pow(100, -x / (H == 0 ? 1 : H))), x);// 公式 y = M(1-100^(-x/H))
             */
            val dragRate = 0.5f
            val m = if (mMaxDragRate < 10) mMaxDragRate * mMaxDragHeight else mMaxDragRate
            val h = (mScreenHeightPixels / 2).coerceAtLeast(this.height)
            val x = (originTranslation * dragRate).coerceAtLeast(0f)
            val y = m * (1 - 100f.pow(-x / (if (h == 0) 1 else h)))
            return y
        } else {
            /**
            final float maxDragHeight = mFooterMaxDragRate < 10 ? mFooterHeight * mFooterMaxDragRate : mFooterMaxDragRate;
            final double M = maxDragHeight - mFooterHeight;
            final double H = Math.max(mScreenHeightPixels * 4 / 3, thisView.getHeight()) - mFooterHeight;
            final double x = -Math.min(0, (spinner + mFooterHeight) * mDragRate);
            final double y = -Math.min(M * (1 - Math.pow(100, -x / (H == 0 ? 1 : H))), x);// 公式 y = M(1-100^(-x/H))
             */
            val dragRate = 0.5f
            val m = if (mMaxDragRate < 10) mMaxDragRate * mMaxDragHeight else mMaxDragRate
            val h = (mScreenHeightPixels / 2).coerceAtLeast(this.height)
            val x = -(originTranslation * dragRate).coerceAtMost(0f)
            val y = -m * (1 - 100f.pow(-x / if (h == 0) 1 else h))
            return y
        }
    }


    private fun overSpinner() {
        animSpinner(0f, 0, mReboundInterpolator, 1000)
    }

    private fun animSpinner(
        endSpinner: Float,
        startDelay: Long,
        interpolator: Interpolator?,
        duration: Long
    ):ValueAnimator? {
        if (mSpinner != endSpinner) {
            Log.d(TAG, "start anim")
            mReboundAnimator?.let {
                it.duration = 0
                it.cancel()
            }
            mAnimationRunnable = null
            val endListener = object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    //cancel()会导致onAnimationEnd,通过设置duration = 0来标记动画被取消
                    if (animation != null && animation.duration == 0L) {
                        return
                    }
                    mReboundAnimator?.let {
                        it.removeAllUpdateListeners()
                        it.removeAllListeners()
                    }
                    mReboundAnimator = null
                }
            }
            val updateListener = ValueAnimator.AnimatorUpdateListener {
                val spinner = it.animatedValue as Int ?: 0
                moveTranslation(spinner.toFloat())
            }
            ValueAnimator.ofInt(mSpinner.roundToInt(), endSpinner.roundToInt()).also {
                mReboundAnimator = it
            }.let {
                it.duration = duration
                it.interpolator = interpolator
                it.startDelay = startDelay
                it.addListener(endListener)
                it.addUpdateListener(updateListener)
                it.start()
            }
            return mReboundAnimator
        }
        return null
    }

    private fun moveTranslation(dy: Float) {
        for (i in 0 until super.getChildCount()) {
            super.getChildAt(i).translationY = dy
        }
        mSpinner = dy
    }
    protected inner class BounceRunnable internal constructor(var mVelocity: Float, var mSmoothDistance: Int) :
        Runnable {
        var mFrame = 0
        var mFrameDelay = 10
        var mLastTime: Long
        var mOffset = 0f
        override fun run() {
            if (mAnimationRunnable === this) {
                mVelocity *= if (abs(mSpinner) >= abs(mSmoothDistance)) {
                    if (mSmoothDistance != 0) {
                        0.45.pow((++mFrame * 2).toDouble()).toFloat() //刷新、加载时回弹滚动数度衰减
                    } else {
                        0.85.pow((++mFrame * 2).toDouble()).toFloat() //回弹滚动数度衰减
                    }
                } else {
                    0.95.pow((++mFrame * 2).toDouble()).toFloat() //平滑滚动数度衰减
                }
                val now = AnimationUtils.currentAnimationTimeMillis()
                val t = 1f * (now - mLastTime) / 1000
                val velocity = mVelocity * t
                // 还有速度时，就加剧过度滑动
                if (abs(velocity) >= 1) {
                    mLastTime = now
                    mOffset += velocity
                    moveTranslation(computeDampedSlipDistance(mOffset.roundToInt()))
                    mHandler?.postDelayed(this, mFrameDelay.toLong())
                } else {
                    // 没有速度后，通过 reboundAnimator，回弹至初始位置
                    mAnimationRunnable = null
                    if (abs(mSpinner) >= abs(mSmoothDistance)) {
                        val duration = 10L * (context.dp2px(abs(mSpinner - mSmoothDistance).toInt()))
                            .coerceAtLeast(30).coerceAtMost(100)
                        animSpinner(mSmoothDistance.toFloat(), 0, mReboundInterpolator, duration)
                    }
                }
            }
        }

        init {
            mLastTime = AnimationUtils.currentAnimationTimeMillis()
            mHandler?.postDelayed(this, mFrameDelay.toLong())
        }
    }
}