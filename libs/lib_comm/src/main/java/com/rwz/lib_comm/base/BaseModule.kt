package com.rwz.lib_comm.base

import com.rwz.lib_comm.config.CODE_SUCCESS
import com.rwz.lib_comm.entity.response.Response
import com.rwz.lib_comm.net.NetException
import com.rwz.lib_comm.net.RetrofitManager
import com.rwz.lib_comm.utils.show.ToastUtil
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * date： 2019/11/7 17:27
 * author： rwz
 * description：
 **/
open class BaseModule{

    fun <T>getService(cls : Class<T>) = RetrofitManager.getService(cls)

    /**
     * 判读是否正确结果
     * @param response
     * @return
     */
    private fun <T>isRequestSuccess(response: Response<T>?): Boolean {
        return isRequestSuccess(response, true)
    }

    private fun <T>isRequestSuccess(response: Response<T>?, showToastIfFail: Boolean): Boolean {
        var isSuccess = false
        //请求成功
        if (response != null && response.code == CODE_SUCCESS) {
            isSuccess = true
        } else { // 请求失败
            response?.let {
                if (showToastIfFail)
                    ToastUtil.showShort(it.msg)
            }
        }
        return isSuccess
    }

    /**
     * 获取数据
     * @param response
     * @param <T>
     * @return
    </T> */
    private fun <T> getData(response: Response<T>, showToast: Boolean): T {
        return response.let {
            if (isRequestSuccess(response, showToast))
                response.data
            else
                throw NetException(response.code, response.msg)
        }
    }

    /**
     * 从子线程切换到主线程
     * 跟compose()配合使用,比如ObservableUtils.wrap(obj).compose(toMain())
     * @param <T>
     * @return
    </T> */
    fun <T> toMainThread(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 对结果的一般处理
     * @param <T>
     * @return
    </T> */
    fun <T> transformerCommon(): ObservableTransformer<Response<T>, T> {
        return transformerCommon(showToast = true, isSwitchThread = true)
    }

    /**
     * @param showToast 异常情况下， 是否打印toast
     * @param isSwitchThread 是否切换线程 （如果本来在子线程请求， 可以给false）
     */
    fun <T> transformerCommon(
        showToast: Boolean,
        isSwitchThread: Boolean
    ): ObservableTransformer<Response<T>, T> {
        return ObservableTransformer { upstream ->
            val map = upstream
                .map { t ->
                    getData(t, showToast)
                }
            if (isSwitchThread) {
                map.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            } else {
                map
            }
        }
    }


}