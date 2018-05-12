package com.wind.me.verticalviewpagerlib

import android.support.v4.view.ViewPager

/**
 * Created by Wind on 2018/5/4.
 *
 * 由于原生的OnPageChangeListener中的onPageSelected监听滑动到指定页并不准确
 * 所以重写之，准确判断滑到当前页
 *
 */
abstract class ViewPagerPageChangeProxy : ViewPager.OnPageChangeListener {

    private var mLastPositionOffsetSum: Float = 0F  // 上一次滑动总的偏移量

    private val TAG = ViewPagerPageChangeProxy::class.simpleName

    //滑动时是否切换了页面
    private var mIsSelectedWhileScrolling = false

    //上次滑动的位置
    private var mLastScrolledPosition = 0

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        LogI(TAG, "  onPageScrolled  position = $position positionOffset =$positionOffset positionOffsetPixels = $positionOffsetPixels")
        // 当前总的偏移量
        val currentPositionOffsetSum = position + positionOffset
        // 上次滑动的总偏移量大于此次滑动的总偏移量，页面从右向左进入(手指从右向左滑动)
        val bottomToTop = mLastPositionOffsetSum <= currentPositionOffsetSum
        if (currentPositionOffsetSum == mLastPositionOffsetSum) {
            return
        }
        val enterPosition: Int
        val leavePosition: Int
        val percent: Float
        if (bottomToTop) {  // 从右向左滑
            enterPosition = if (positionOffset == 0.0f) position else position + 1
            leavePosition = enterPosition - 1
            percent = if (positionOffset == 0.0f) 1.0f else positionOffset
        } else {            // 从左向右滑
            enterPosition = position
            leavePosition = position + 1
            percent = 1 - positionOffset
        }
        onPageScroll(enterPosition, leavePosition, percent)
        mLastPositionOffsetSum = currentPositionOffsetSum

        if (mIsSelectedWhileScrolling && positionOffset == 0.0f && positionOffsetPixels == 0) {
            mIsSelectedWhileScrolling = false
            if (position == mLastScrolledPosition + 1) {
                //真正向上滑动了
                onPageTruelySelected(position, true)
                LogI(TAG, " onPageScrollStateChanged 你向上滑动了 ！！！ 是吧？")
            } else {
                //真正向下滑动了
                onPageTruelySelected(position, false)
                LogI("xia", " onPageScrollStateChanged 你向下滑动了 ！！！ 是吧？")
            }
        }
        mLastScrolledPosition = position
    }

    override fun onPageSelected(position: Int) {
        LogI(TAG, "  onPageSelected  position = $position")
        mIsSelectedWhileScrolling = true
    }

    /**
     * @param state 当前滑动状态
     * ViewPager.SCROLL_STATE_IDLE     页面处于闲置、稳定状态，即没被拖动也没惯性滑动
     * ViewPager.SCROLL_STATE_DRAGGING 页面正在被用户拖动，即手指正在拖动状态
     * Viewpager.SCROLL_STATE_SETTLING 页面处于即将到达最终状态的过程，即手指松开后惯性滑动状态
     */
    override fun onPageScrollStateChanged(state: Int) {
        LogI(TAG, " onPageScrollStateChanged  state = $state")
        onPageScrollingStateChanged(state)
    }


    /**
     * 页面滚动时调用
     *
     * @param enterPosition 进入页面的位置
     * @param leavePosition 离开的页面的位置
     * @param percent       滑动百分比
     */
    open abstract fun onPageScroll(enterPosition: Int, leavePosition: Int, percent: Float)

    /**
     * 页面真正被选中时调用
     *
     * @param position 选中页面的位置
     * @param isIncrease 向上滑动为true，向下为false
     */
    open abstract fun onPageTruelySelected(position: Int, isIncrease: Boolean)

    /**
     * 页面滚动状态变化时调用
     *
     * @param state 页面的滚动状态
     */
    open abstract fun onPageScrollingStateChanged(state: Int)
}
