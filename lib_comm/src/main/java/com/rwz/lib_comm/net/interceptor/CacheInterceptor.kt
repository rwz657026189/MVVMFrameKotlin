package com.rwz.lib_comm.net.interceptor

import com.rwz.lib_comm.utils.app.NetUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * date： 2019/12/4 14:11
 * author： rwz
 * description：缓存拦截， maxStale保存时长，单位s
 **/
class CacheInterceptor(private val maxStale: Int) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (!NetUtils.isConnected()) {
            //如果断网 这里返回 缓存数据 直接结束这次访问
            val tempCacheControl = CacheControl.Builder()
                .onlyIfCached()
                .maxStale(maxStale, TimeUnit.SECONDS)
                .build()

            request = request.newBuilder()
                .cacheControl(tempCacheControl)
                .build()

        }
        return chain.proceed(request)
    }
}