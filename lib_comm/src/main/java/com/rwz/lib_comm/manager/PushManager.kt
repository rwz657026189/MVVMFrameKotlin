package com.rwz.lib_comm.manager

import android.content.Context

/**
 * date： 2019/11/25 17:30
 * author： rwz
 * description：
 **/
object PushManager {

    val TAG = "PushManager"

    var sDeviceToken: String? = null//友盟设备唯一id(与服务器账号绑定, 登录注册传递)

    /**
     * 每个activity中需要调用
     * @param context
     */
    fun onCreate(context: Context) {}

}
