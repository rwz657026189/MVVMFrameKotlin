package com.rwz.lib_comm.manager

import android.text.TextUtils
import androidx.annotation.StringRes
import com.rwz.lib_comm.config.OPEN_STATISTICS
import com.rwz.lib_comm.utils.app.ResourceUtil

/**
 * date： 2019/11/25 17:32
 * author： rwz
 * description：
 **/
object StatisticsManager {

    /**
     * 统计自定义事件
     * @param name
     */
    fun onEvent(@StringRes name: Int) {
        if (OPEN_STATISTICS && name != 0) {
            onEvent(ResourceUtil.getString(name))
        }
    }

    fun onEvent(name: String) {
        if (OPEN_STATISTICS && !TextUtils.isEmpty(name)) {
            onEvent(name, null)
        }
    }

    fun onEvent(name: String, label: String?) {}

    /**
     * 统计页面停留时间
     * @param name
     */
    fun onPageStart(@StringRes name: Int) {
        if (name != 0) {
            onPageStart(ResourceUtil.getString(name))
        }
    }

    fun onPageStart(name: String) {}

    fun onPageEnd(@StringRes name: Int) {
        if (name != 0) {
            onPageEnd(ResourceUtil.getString(name))
        }
    }

    fun onPageEnd(name: String) {}


}