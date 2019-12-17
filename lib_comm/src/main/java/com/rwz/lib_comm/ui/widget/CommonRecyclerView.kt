package com.rwz.lib_comm.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * date： 2019/11/29 13:29
 * author： rwz
 * description：
 **/
class CommonRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    /**
     * 获取第一个可见item位置
     * @return
     */
    fun findFirstVisibleItemPosition(): Int {
        var firstVisibleItemPosition = -1
        when (val manager = layoutManager) {
            is LinearLayoutManager -> firstVisibleItemPosition = manager.findFirstVisibleItemPosition()
            is GridLayoutManager -> firstVisibleItemPosition = manager.findFirstVisibleItemPosition()
        }
        return firstVisibleItemPosition
    }

    fun findLastVisibleItemPosition(): Int {
        var lastVisibleItemPosition = -1
        when (val manager = layoutManager) {
            is LinearLayoutManager -> lastVisibleItemPosition = manager.findLastVisibleItemPosition()
            is GridLayoutManager -> lastVisibleItemPosition = manager.findLastVisibleItemPosition()
        }
        return lastVisibleItemPosition
    }

    /**
     * 根据位置获取该View
     */
    fun getItemView(position: Int): View? {
        val firstVisibleItemPosition = findFirstVisibleItemPosition()
        val lastVisibleItemPosition = findLastVisibleItemPosition()
        if (firstVisibleItemPosition != -1 && lastVisibleItemPosition != -1 && position >= firstVisibleItemPosition && position <= lastVisibleItemPosition) {
            val index = position - firstVisibleItemPosition
            return getChildAt(index)
        }
        return null
    }


}