package com.rwz.mvvm_kotlin_demo.base

import android.app.Application
import com.rwz.lib_comm.manager.ContextManager

/**
 * date： 2019/11/18 16:57
 * author： rwz
 * description：
 **/
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextManager.context = this
    }

}