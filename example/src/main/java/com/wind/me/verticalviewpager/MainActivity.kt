package com.wind.me.verticalviewpager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wind.me.verticalviewpagerlib.ViewPagerPageChangeProxy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mViewPagerAdapter: MyViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()
    }

    fun initView() {
        verticalviewpager.apply {
            setPageFactor(0.75f)   //设置滑动调转因子

            setOnPageChangeListener(object : ViewPagerPageChangeProxy() {
                override fun onPageScroll(enterPosition: Int, leavePosition: Int, percent: Float) {
                }

                override fun onPageTruelySelected(position: Int, isIncrease: Boolean) {
                    swipeRefreshLayout.isEnabled = (position == 0)
                    smartRefreshLayout.isEnableLoadMore = position == adapter.count - 1
                }

                override fun onPageScrollingStateChanged(state: Int) {
                }
            })

        }


        //监听上拉刷新数据
        swipeRefreshLayout.apply {
            setOnRefreshListener {
                //模拟网络请求，延时获取到数据
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            var arrayList = arrayListOf(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
                            mViewPagerAdapter.setData(arrayList)
                            isRefreshing = false
                        })
            }
        }

        smartRefreshLayout.apply {
            isEnableRefresh = false
            isEnableLoadMore = false
            setOnLoadMoreListener {
                Observable.timer(3000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            var arrayList = arrayListOf(10, 11, 12, 13, 14, 15, 16, 17, 18, 19)
                            mViewPagerAdapter.addData(arrayList)

                            if (mViewPagerAdapter.count - 1 != verticalviewpager.currentItem) {
                                isEnableLoadMore = false
                            }
                            finishLoadMore()

//                            verticalviewpager.arrowScroll(View.FOCUS_DOWN)
                        })
            }
        }
    }

    fun initData() {
        mViewPagerAdapter = MyViewPagerAdapter(this)

        var arrayList = arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        mViewPagerAdapter.addData(arrayList)

        verticalviewpager.adapter = mViewPagerAdapter

    }
}
