package com.rwz.lib_comm.base

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.annotation.Nullable

/**
 * date： 2019/11/25 17:12
 * author： rwz
 * description：
 **/
class BaseBindService : Service() {

    private var mBinder: BaseBindBinder? = null

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        mBinder = BaseBindService().BaseBindBinder()
    }

    inner class BaseBindBinder : Binder() {
        val service: Service
            get() = this@BaseBindService
    }
}
