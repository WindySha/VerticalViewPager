package com.wind.me.verticalviewpager

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.wind.me.verticalviewpagerlib.RecyclerViewPagerAdapter

/**
 * Created by Wind on 2018/5/8.
 * view的容器 ViewHolder
 */
class MyItemViewHolder(itemView: View) : RecyclerViewPagerAdapter.PagerViewHolder(itemView) {

    internal val TAG = MyItemViewHolder::class.java.simpleName

    var itemLayout: ViewGroup

    var mTextView: TextView

    init {
        itemLayout = itemView.findViewById(R.id.item_layout)
        mTextView = itemView.findViewById(R.id.textview)
    }

    constructor(context: Context, layoutId: Int) : this(LayoutInflater.from(context).inflate(layoutId, null, false)) {}

    constructor(context: Context) : this(LayoutInflater.from(context).inflate(R.layout.viewpager_item, null, false)) {}

}
