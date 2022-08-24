package com.rwz.lib_comm.abs

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver

/**
 * date： 2019/11/15 16:31
 * author： rwz
 * description：
 **/
interface IViewModule<V : IView> : LifecycleObserver {

    fun bindView(v : IView)

    fun initCompleted()

    fun onSaveInstanceState(outState: Bundle)

}
