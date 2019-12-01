package com.rwz.lib_comm.base.proxy

import android.app.Activity
import android.os.Build
import com.rwz.lib_comm.R
import com.rwz.lib_comm.utils.app.ResourceUtil
import com.rwz.lib_comm.utils.app.StatusBarUtil

/**
 * date： 2019/11/29 11:57
 * author： rwz
 * description：状态栏
 **/
class StatusBarProxy(private var activity: Activity){

    //是否设置状态栏颜色
    var isSetBarColor = true

    private var isFullScreen: Boolean = false
    private var isDarkStatus: Boolean = false

    init {
        //设置状态栏颜色
        themeColorSetting(ResourceUtil.getColor(R.color.toolbar_bg))
    }

    /**
     * 主题颜色设置
     * @param color
     */
    fun themeColorSetting(color: Int) {
        if (isSetBarColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = color
        }
    }

    /**
     * 设置深色状态栏文字
     * @param isDarkStatus : true : 黑字;  false 白字
     */
    fun setDarkStatusBar(isFullScreen: Boolean, isDarkStatus: Boolean) {
        if (this.isFullScreen != isFullScreen || this.isDarkStatus != isDarkStatus) {
            this.isFullScreen = isFullScreen
            this.isDarkStatus = isDarkStatus
            StatusBarUtil.setDarkStatusBar(activity, isFullScreen, isDarkStatus)
        }
    }

}
