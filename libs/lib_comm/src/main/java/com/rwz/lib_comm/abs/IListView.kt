package com.rwz.lib_comm.abs

import androidx.annotation.LayoutRes

/**
 * date： 2019/11/15 16:36
 * author： rwz
 * description：
 **/
interface IListView : IView {

    /**
     * 数据加载完成
     * @param isRefresh
     */
    fun loadDataComplete(isRefresh: Boolean)

    /**
     * 是否能加载更多
     * @param enabled
     */
    fun setLoadingMoreEnabled(enabled: Boolean)

    /**
     * 是否能刷新
     * @param enabled
     */
    fun setPullRefreshEnabled(enabled: Boolean)

    /** 刷新  */
    fun autoRefresh()

    /**
     * 更新列表
     */
    fun notifyDataSetChanged()

    /**
     * 更新数据
     * @param type
     * @param position
     */
    fun updateData(@ViewListUpdateType type: Int, position: Int)

    /**
     * 空视图类型
     */
    fun setTempType(type: Int)

    fun addHeaderView(@LayoutRes layoutId: Int)

    companion object {
        const val INSERTED = 0 //插入
        const val CHANGED = 1  //改变
        const val REMOVE = 2  //移除
        const val SCROLL_TO_BOTTOM = 3  //滚动到底部
        const val SCROLL_TO_TOP = 4  //滚动到底部
        const val SCROLL_TO_POSITION = 5  //滚动到指定位置
    }


}