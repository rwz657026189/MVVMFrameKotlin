package com.rwz.lib_comm.comm

import com.rwz.lib_comm.utils.show.LogUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * date： 2019/11/29 14:10
 * author： rwz
 * description：特别注意, 建议订阅者都是独立对象, 因在onSubscribe()调用了destroy(),
 * 用同一对象可能引起一些潜在问题
 **/


abstract class CommonObserver<T> : Observer<T> {

    private var mDisposable: Disposable? = null
    private val TAG = "OkHttp"

    override fun onSubscribe(d: Disposable) {
        mDisposable = d
    }

    override fun onError(e: Throwable) {
        LogUtil.d(TAG, "CommonObserver", "onError", e)
        e.printStackTrace()
    }

    override fun onComplete() {
        LogUtil.d(TAG, "CommonObserver", "onComplete")
    }

    fun destroy() {
        LogUtil.d(TAG, "CommonObserver", "destroy")
        mDisposable?.takeUnless {
            it.isDisposed
        }?.let {
            mDisposable = null
        }
    }

}