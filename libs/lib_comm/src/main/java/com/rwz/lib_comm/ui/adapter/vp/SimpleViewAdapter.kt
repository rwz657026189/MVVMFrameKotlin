package com.rwz.lib_comm.ui.adapter.vp

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import java.util.*

/**
 * date： 2019/11/15 16:38
 * author： rwz
 * description：
 **/
class SimpleViewAdapter<F> : PagerAdapter() {
    private val mViewList = ArrayList<F>()
    private val mFragmentTitleList = ArrayList<String>()


    override fun getCount() = mViewList.size

    fun update(list: List<F>?) {
        if (list != null && list.isNotEmpty()) {
            mViewList.clear()
            mViewList.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun getData(pos: Int): F? {
        return if (pos < mViewList.size) {
            mViewList[pos]
        } else null
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return mViewList.takeIf { mViewList.size > position }
            ?.get(position).takeIf { it is View }
            ?.apply {
                container.addView(this as View)
            }!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }
}