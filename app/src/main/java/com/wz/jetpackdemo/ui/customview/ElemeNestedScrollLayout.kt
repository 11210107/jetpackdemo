package com.wz.jetpackdemo.ui.customview

import android.animation.ValueAnimator
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.animation.addListener
import androidx.core.math.MathUtils
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.wz.jetpackdemo.R
import com.wz.jetpackdemo.extension.dp2px


class ElemeNestedScrollLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NestedScrollingParent2 {

    val TAG = ElemeNestedScrollLayout::class.java.simpleName
    val ANIM_DURATION_FRACTION = 200L

    //Header部分
    private var mIvLogo: View? = null
    private var mVCollect: View? = null

    //Collapse content部分
    private var mClCollapsedHeader: View? = null
    private var mClCollapsedContent: View? = null
    private var mRvCollapsed: View? = null

    //TopBar部分
    private var mIvSearch: View? = null
    private var mIvShare: View? = null
    private var mTvSearch: View? = null
    private var mTopBar: View? = null
    private var mIvBack: ImageView? = null
    private var mIvAssemble: ImageView? = null

    //Content部分
    private var mLlContent: View? = null
    private var mIvClose: View? = null
    private var mViewPager: View? = null
    private var mStl: View? = null

    //ShopBar部分
    private var mShopBar: View? = null

    //topBar高度
    private var topBarHeight: Float = 0f

    //shopBar高度
    private var shopBarHeight: Float = 0f

    //滑动内容初始化TransY
    private var initContentTransY: Float = 0f

    //上滑时logo,收藏icon缩放，搜索icon，分享icon透明度临界值
    private var upAlphaScaleY: Float = 0f

    //上滑时搜索框，topBar背景，返回icon，拼团icon颜色渐变临界值
    private var upAlphaGradientY: Float = 0f

    //从折叠状态下滑产生fling时回弹到初始状态的临界值
    private var downFlingCutOffY: Float = 0f

    //下滑时折叠内容透明度临界值
    private var downCollapsedAlphaY: Float = 0f

    //下滑时购物内容位移临界值
    private var downShopBarTransY: Float = 0f

    //下滑时收起按钮和滑动内容透明度临界值
    private var downContentAlphaY: Float = 0f

    //下滑时终点值
    private var downEndY: Float = 0f

    var mParentHelper: NestedScrollingParentHelper? = null

    //回弹动画
    var reboundAnim: ValueAnimator? = null

    //收起或展开折叠内容时执行的动画
    var restoreOrExpandOrCloseAnimator: ValueAnimator? = null


    init {
        mParentHelper = NestedScrollingParentHelper(this)
        reboundAnim = ValueAnimator()
        reboundAnim?.interpolator = DecelerateInterpolator()
        reboundAnim?.addUpdateListener {
            translation(mLlContent, it.animatedValue as Float)
            //根据upAlphaScalePro,设置logo、收藏icon缩放、搜索icon、搜索框透明度
            val upAlphaScalePro = getUpAlphaScalePro()
            alphaScaleByPro(upAlphaScalePro)

            //alphaTransView(mNestedScrollView.getTranslationY())

        }
        reboundAnim?.duration = ANIM_DURATION_FRACTION

        restoreOrExpandOrCloseAnimator = ValueAnimator()
        restoreOrExpandOrCloseAnimator?.interpolator = AccelerateInterpolator()
        restoreOrExpandOrCloseAnimator?.addUpdateListener {
            translation(mLlContent, it.animatedValue as Float)
            //alphaTransView(mNestedScrollView.getTranslationY())

        }
        restoreOrExpandOrCloseAnimator?.addListener {
//            val alpha = if (mNestedScrollView.getTranslationY() >= measuredHeight) 0 else 1
//            setAlpha(mIvClose, alpha.toFloat())
//            setAlpha(mIvShare, alpha.toFloat())
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mIvLogo = findViewById(R.id.iv_logo)
        mVCollect = findViewById(R.id.iv_collect)
        mClCollapsedHeader = findViewById(R.id.cl_collapsed_header)
        mClCollapsedContent = findViewById(R.id.cl_collapsed_content)
        mRvCollapsed = findViewById(R.id.rv_collapsed)
        mIvSearch = findViewById(R.id.iv_search)
        mIvShare = findViewById(R.id.iv_share)
        mTvSearch = findViewById(R.id.tv_search)
        mTopBar = findViewById(R.id.cl_top_bar)
        mIvBack = findViewById(R.id.iv_back)
        mIvAssemble = findViewById(R.id.iv_assemble)
        mLlContent = findViewById(R.id.cl_content)
        mIvClose = findViewById(R.id.iv_close)
        mViewPager = findViewById(R.id.vp)
        mStl = findViewById(R.id.stl)
        mShopBar = findViewById(R.id.cl_shop_bar)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        topBarHeight = mTopBar?.measuredHeight?.toFloat() ?: 0f

        val layoutParams = mLlContent?.layoutParams
        layoutParams?.height = (measuredHeight - topBarHeight).toInt()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shopBarHeight = resources.getDimension(R.dimen.shop_bar_height)
        initContentTransY = resources.getDimension(R.dimen.content_trans_y)
        downShopBarTransY = initContentTransY + shopBarHeight
        upAlphaScaleY = initContentTransY - 32f.dp2px()
        upAlphaGradientY = initContentTransY - 64f.dp2px()
        downFlingCutOffY = initContentTransY + 28f.dp2px()
        downContentAlphaY = initContentTransY + 32f.dp2px()
        downContentAlphaY = resources.getDimension(R.dimen.down_content_alpha_y)
        downEndY = height - resources.getDimension(R.dimen.iv_close_height)

    }

    /**
     * NestedScrollingParent
     */
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return onStartNestedScroll(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH)
    }

    /**
     * NestedScrollingParent2
     */
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.e(TAG, "child:${child} target:${target} axes:$axes type:$type ")
        return child.id == mLlContent?.id && axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {

    }

    override fun onStopNestedScroll(target: View, type: Int) {
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.e(TAG, "onNestedPreScroll target:$target dy:$dy")
        val contentTransY = mLlContent?.translationY?.minus(dy)

        if (dy > 0) {
            //处理上滑
            contentTransY?.let {
                if (contentTransY >= topBarHeight) {
                    Log.e(
                        TAG,
                        "up contentTransY:${contentTransY} topBarHeight:${topBarHeight} consumeDy:${dy}"
                    )
                    translateByConsume(mLlContent, contentTransY, consumed, dy)
                } else {
                    Log.e(
                        TAG,
                        "up contentTransY:${contentTransY} topBarHeight:${topBarHeight} consumeDy:${
                            mLlContent?.translationY?.minus(topBarHeight)
                        } "
                    )
                    translateByConsume(
                        mLlContent, topBarHeight, consumed,
                        (mLlContent?.translationY?.minus(topBarHeight))?.toInt() ?: 0
                    )
                }
            }

        }
        if (dy < 0) {
            //下滑时处理Fling,完全折叠时，下滑RecyclerView或者NestedScrollView Fling滚动到列表顶部或者视图顶部停止Fling
            if (type == ViewCompat.TYPE_NON_TOUCH && mLlContent?.translationY == topBarHeight) {
                return
            }
            //处理下滑动
            if (contentTransY != null) {
                if (contentTransY in topBarHeight..downEndY && !target.canScrollVertically(-1)) {
                    Log.e(TAG, "target:$target")

                    Log.e(
                        TAG,
                        "down contentTransY:${contentTransY} topBarHeight:${topBarHeight} downEndY:${downEndY} consumeDy:${dy}"
                    )
                    translateByConsume(mLlContent, contentTransY, consumed, dy)
                } else if (contentTransY > downEndY) {
                    Log.e(
                        TAG,
                        "down downEndY:${downEndY} consumeDy:${downEndY - mLlContent!!.translationY} "
                    )
                    translateByConsume(
                        mLlContent,
                        downEndY,
                        consumed,
                        (downEndY - mLlContent!!.translationY).toInt()
                    )
                    if (target is RecyclerView) {
                        target.stopScroll()
                    }
                    if (target is NestedScrollView) {
                        //模拟DOWN事件停止滚动，注意会触发onNestedScrollAccepted()
                        val motionEvent = MotionEvent.obtain(
                            SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN,
                            0f,
                            0f,
                            0
                        )
                        target.onTouchEvent(motionEvent)
                    }
                }
                if (contentTransY in topBarHeight..initContentTransY) {
                    Log.e(TAG, "down contentTransY:${contentTransY} consumeDy:${dy}")
                    if (target.canScrollVertically(-1)) {
                        translateByConsume(mLlContent, topBarHeight, consumed, 0)
                    } else {
                        translateByConsume(mLlContent, contentTransY, consumed, dy)
                    }
                }
            }
        }
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if (velocityY < 0) {
            val translationY = mLlContent?.translationY
            val dy = translationY?.minus(velocityY)

        }
        return super.onNestedPreFling(target, velocityX, velocityY)
    }
    /**
     * 更加upAlphaScalePro，设置logo、收藏icon、搜索icon、分享icon透明度
     */
    private fun alphaScaleByPro(upAlphaScalePro: Float) {
        val alpha = 1 - upAlphaScalePro
        val scale = 1 - upAlphaScalePro
        mIvSearch?.alpha = alpha
        mIvShare?.alpha = scale
        setScaleAlpha(mIvLogo, scale, scale, alpha)
        setScaleAlpha(mVCollect, scale, scale, alpha)

    }

    private fun setScaleAlpha(view: View?, scaleY: Float, scaleX: Float, alpha: Float) {
        view?.alpha = alpha
        setScale(view,scaleY,scaleX)
    }

    private fun setScale(view: View?, scaleY: Float, scaleX: Float) {
        view?.scaleX = scaleX
        view?.scaleY = scaleY
    }

    //控件位移
    private fun translateByConsume(
        view: View?,
        translationY: Float,
        consumed: IntArray,
        consumedDy: Int
    ) {
        consumed[1] = consumedDy
        view?.translationY = translationY
    }

    private fun translation(view: View?, translationY: Float) {
        view?.translationY = translationY
    }

    private fun getUpAlphaScalePro(): Float {
        return (initContentTransY - MathUtils.clamp(
            mLlContent?.translationY ?: 0f,
            upAlphaGradientY,
            upAlphaScaleY
        )) / (upAlphaScaleY - upAlphaGradientY)

    }
}