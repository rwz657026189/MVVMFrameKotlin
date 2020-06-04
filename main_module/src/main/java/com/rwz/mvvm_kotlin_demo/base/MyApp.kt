package com.rwz.mvvm_kotlin_demo.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.rwz.lib_comm.config.ONLINE_HTTP
import com.rwz.lib_comm.manager.ContextManager
import com.rwz.lib_comm.net.RetrofitManager

/**
 * date： 2019/11/18 16:57
 * author： rwz
 * description：
 **/
open class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextManager.context = this
        RetrofitManager.init(ONLINE_HTTP)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}