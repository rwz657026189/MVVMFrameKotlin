package com.rwz.lib_comm.abs

/**
 * date： 2019/11/15 16:29
 * author： rwz
 * description：
 **/
interface IView {
    /**
     * 通信
     */
    fun onPostEvent(@PostEventType type: Int, params: Any?)

    companion object {
        //结束当前activity
        const val FINISH_ATY = 0
        //显示dialog
        const val SHOW_DIALOG = 1
        //请求完成更新数据
        const val UPDATE_DATA = 2
        //显示正在加载中dialog
        const val SHOW_LOADING = 3
        //消失正在加载中dialog
        const val DISMISS_LOADING = 4
        //更新状态
        const val UPLOAD_EDIT_STATE = 5
        //是否禁用触摸屏幕
        const val FORBID_TOUCH_SCREEN = 6
    }


}
