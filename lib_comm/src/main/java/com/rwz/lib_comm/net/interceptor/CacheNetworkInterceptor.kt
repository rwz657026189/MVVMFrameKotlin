package com.rwz.lib_comm.net.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * date： 2019/12/4 14:16
 * author： rwz
 * description：maxAge 参数用于设置 一个多少秒内访问不重复请求接口的参数 单位为秒
 **/
class CacheNetworkInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        val maxAge = 20    // 在线缓存,单位:秒
        return originalResponse.newBuilder()
            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
            .removeHeader("Cache-Control")
            .header("Cache-Control", "public, max-age=$maxAge")
            .build()
    }
}