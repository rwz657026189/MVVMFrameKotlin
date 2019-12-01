package com.rwz.lib_comm.manager

import android.annotation.SuppressLint
import android.content.Context
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity

/**
 * date： 2019/11/7 16:44
 * author： rwz
 * description：
 **/
@SuppressLint("StaticFieldLeak")
object ContextManager{

    lateinit var context: Context

    /**
     * 获取一个activity 实例
     * @param context
     * @return
     */
    fun getAppCompActivity(context: Context?): AppCompatActivity? {
        if (context == null) return null
        if (context is AppCompatActivity) {
            return context
        } else if (context is ContextThemeWrapper) {
            return getAppCompActivity(context.baseContext)
        }
        return null
    }

}