package com.rwz.lib_comm.abs

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.rwz.lib_comm.utils.show.LogUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * date： 2019/11/15 16:33
 * author： rwz
 * description：管理ViewModule的生命周期
 **/

abstract class RxViewModule<V : IView> : IViewModule<V> {

    val TAG = javaClass.simpleName
    var mView: V? = null
    var mCompositeSubscription: CompositeDisposable? = null

    protected val isAlive: Boolean
        get() = mView != null

    protected fun dispose() {
        mCompositeSubscription?.dispose()
    }

    fun addDisposable(disposable: Disposable) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeDisposable()
        }
        mCompositeSubscription!!.add(disposable)
    }

    override fun bindView(v: IView) {
        this.mView = v as V
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //非必须重写
    }

    @JvmOverloads
    protected fun postEvent(@PostEventType type: Int, params: Any? = null) {
        mView?.onPostEvent(type, params)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        LogUtil.d(TAG, "onCreate: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        LogUtil.d(TAG, "onStart: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        LogUtil.d(TAG, "onResume: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        LogUtil.d(TAG, "onDestroy ")
        this.mView = null
        dispose()
    }

}