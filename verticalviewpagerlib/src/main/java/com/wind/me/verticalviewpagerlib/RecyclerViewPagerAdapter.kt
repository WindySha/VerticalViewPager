package com.wind.me.verticalviewpagerlib

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

/**
 * Created by Wind on 2018/5/8.
 *
 * View可进行回收复用的ViewPager适配器
 */
abstract class RecyclerViewPagerAdapter<VH : RecyclerViewPagerAdapter.PagerViewHolder> : PagerAdapter() {

    companion object {
        val INVALID_TYPE = -1
    }

    private val TAG = RecyclerViewPagerAdapter::class.simpleName

    //保存已经显示的viewHolder
    private val holderList = ArrayList<VH>()

    //缓存的ViewHolder
    private val holderCachedArray = SparseArray<ArrayList<VH>>()

    override fun getCount(): Int {
        return itemCount
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === (`object` as VH).itemView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        LogI(TAG, "--ViewHolder  RecyclerViewPagerAdapter  instantiateItem  position="+position)
        val holder: VH
        val viewType = getItemViewType(position)
        val recycleHolders = holderCachedArray.get(viewType)
        if (recycleHolders != null && recycleHolders.size > 0) {
            holder = recycleHolders.removeAt(0)
            holder.position = PagerAdapter.POSITION_UNCHANGED
        } else {
            holder = createViewHolder(container, viewType)
        }
        bindViewHolder(holder, position)
        container.addView(holder.itemView, 0)
        return holder
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        LogI(TAG, " -- ViewHolder RecyclerViewPagerAdapter  destroyItem  position="+position)
        val holder = `object` as VH
        container.removeView(holder.itemView)
        holder.isRecycled = true
        holder.position = PagerAdapter.POSITION_NONE
        val viewType = getItemViewType(position)
        val recycleHolders = holderCachedArray.get(viewType, ArrayList())
        recycleHolders.add(holder)
        holderCachedArray.put(viewType, recycleHolders)
        onViewRecycled(holder)
    }

    @Deprecated("")
    override fun instantiateItem(container: View, position: Int): Any {
        return instantiateItem(container as ViewPager, position)
    }

    @Deprecated("")
    override fun destroyItem(container: View, position: Int, `object`: Any) {
        destroyItem(container as ViewPager, position, `object`)
    }

    override fun getItemPosition(`object`: Any): Int {
        var position = PagerAdapter.POSITION_UNCHANGED
        val holder = `object` as VH
        if (holderList.contains(holder)) {
            position = holder.position
            position = if (position >= itemCount) PagerAdapter.POSITION_NONE else position
        }
        return position
    }

    abstract val itemCount: Int

    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindViewHolder(holder: VH, position: Int)

    private fun createViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = onCreateViewHolder(parent, viewType)
        holder.mItemViewType = viewType
        holderList.add(holder)
        return holder
    }

    private fun bindViewHolder(holder: VH, position: Int) {
        holder.position = position
        holder.isRecycled = false
        onBindViewHolder(holder, position)
    }

    //获取不同位置的view类别，只有一个类别，则不用重写
    open fun getItemViewType(position: Int): Int {
        return 0
    }

    open fun onViewRecycled(holder: VH) {}

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        for (holder in holderList) {
            if (!holder.isRecycled && holder.position < itemCount) {
                onBindViewHolder(holder, holder.position)
            }
        }
    }

    fun notifyItemChanged(position: Int) {
        for (holder in holderList) {
            if (!holder.isRecycled && holder.position == position) {
                onBindViewHolder(holder, holder.position)
                break
            }
        }
    }

    open class PagerViewHolder(var itemView: View) {
        internal var position = PagerAdapter.POSITION_UNCHANGED
        internal var isRecycled = false
        var mItemViewType = INVALID_TYPE
    }
}
