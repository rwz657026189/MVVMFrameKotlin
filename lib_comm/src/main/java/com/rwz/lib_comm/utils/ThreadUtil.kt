package com.rwz.lib_comm.utils

import android.os.Handler
import android.os.Looper

/**
 * date： 2019/11/7 16:34
 * author： rwz
 * description：
 **/
object ThreadUtil{

    private val mainHandler : Handler = Handler(Looper.getMainLooper())

    /**
     * 是否在主线程
     */
    fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()

    /**
     * 提交到主线程
     */
    fun postMain(runnable: Runnable) : Boolean = mainHandler.post(runnable)


}