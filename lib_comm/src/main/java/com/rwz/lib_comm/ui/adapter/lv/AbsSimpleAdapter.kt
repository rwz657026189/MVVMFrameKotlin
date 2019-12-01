package com.rwz.lib_comm.ui.adapter.lv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.LayoutRes

/**
 * date： 2019/11/11 15:03
 * author： rwz
 * description：
 **/
abstract class AbsSimpleAdapter<T>(
    private var mContext: Context,
    private var mData: MutableList<T>, @param:LayoutRes protected val mItemLayoutId: Int
) :
    BaseAdapter() {
    /**
     * 请求码
     */
    protected var mRequestCode: Int = 0
    /**
     * 返回码
     */
    protected var mResultCode: Int = 0
    protected var mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): T {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val viewHolder = getViewHolder(
            position, convertView,
            parent
        )
        val view = viewHolder.convertView
        convert(position, viewHolder, getItem(position))
        return view
    }

    /**
     * 设置每一个item的控件的虚函数
     *
     * @param helper
     * @param item
     */
    abstract fun convert(position: Int, helper: AbsSimpleViewHolder, item: T)

    /**
     * 设置每个item的控件数据
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private fun getViewHolder(
        position: Int, convertView: View,
        parent: ViewGroup
    ): AbsSimpleViewHolder {
        return AbsSimpleViewHolder.getViewHolder(
            mContext, convertView, parent, mItemLayoutId,
            position
        )
    }

    /**
     * 设置数据
     *
     * @param list 数据
     */
    fun setData(list: MutableList<T>) {
        mData?.let {
            it.clear()
            it.addAll(list)
        }
        notifyDataSetChanged()
    }

    /**
     * 添加item
     *
     * @param item
     */
    fun addItem(item: T) {
        mData.add(item)
        notifyDataSetChanged()
    }

    /**
     * 删除Item
     *
     * @param position
     */
    fun removeItem(position: Int) {
        mData.removeAt(position)
        notifyDataSetChanged()
    }
}