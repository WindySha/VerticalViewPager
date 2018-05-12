package com.wind.me.verticalviewpager

import android.content.Context
import android.view.ViewGroup
import com.wind.me.verticalviewpagerlib.RecyclerViewPagerAdapter

/**
 * Created by Wind on 2018/5/12.
 */
class MyViewPagerAdapter(val context: Context) : RecyclerViewPagerAdapter<MyItemViewHolder>() {

    val mData: ArrayList<Int> = ArrayList()

    public fun setData(data: ArrayList<Int>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    public fun addData(data: ArrayList<Int>) {
        mData.addAll(data)
        notifyDataSetChanged()
    }

    override val itemCount: Int
        get() = mData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyItemViewHolder {
        return MyItemViewHolder(context)
    }

    override fun onBindViewHolder(holder: MyItemViewHolder, position: Int) {
        if (position % 2 == 0) {
            holder.itemLayout.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
        } else {
            holder.itemLayout.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
        }
        holder.mTextView.text = mData.get(position).toString()
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
    }
}