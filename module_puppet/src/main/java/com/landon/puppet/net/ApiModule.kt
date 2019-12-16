package com.landon.puppet.net

import com.rwz.lib_comm.entity.response.SimpleResponse
import com.rwz.lib_comm.utils.app.NetUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * date： 2019/12/6 14:33
 * author： rwz
 * description：
 **/
object ApiModule {

    fun login(account: String , password: String): Observable<SimpleResponse>{
        return if (NetUtils.isConnected()) {
            Observable.just(account to password)
                .delay((Math.random() * 700).toLong() + 300, TimeUnit.MICROSECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map {
                    SimpleResponse(true, "登录成功")
                }
        } else {
            Observable.error<SimpleResponse>(Throwable("网络未连接"))
        }
    }

    fun register(account: String , password: String): Observable<SimpleResponse>{
        return login(account, password)
    }

}