package com.rwz.lib_comm.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * date： 2019/11/29 16:42
 * author： rwz
 * description：解决 RecyclerView Bug： Inconsistency detected. Invalid view holder adapter
 **/
class SafeLinearLayoutManager @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            //手动catch住
            e.printStackTrace()
        }

    }

}
