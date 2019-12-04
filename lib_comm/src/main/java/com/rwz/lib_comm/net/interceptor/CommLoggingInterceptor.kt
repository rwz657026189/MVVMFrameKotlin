package com.rwz.lib_comm.net.interceptor

import com.rwz.lib_comm.utils.show.LogUtil
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * date： 2019/12/1 11:58
 * author： rwz
 * description：
 **/
class CommLoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        val request = chain.request()
        val body = request.body()
        LogUtil.ok(TAG, "heads = " + request.headers())
        if (body is FormBody) {
            val formBody = body as FormBody?
            val size = formBody!!.size()
            val sb = StringBuilder()
            for (i in 0 until size) {
                sb.append("║   ").append(formBody.name(i)).append(" = ").append(formBody.value(i)).append("\n")
            }
            LogUtil.ok(TAG, "url : \n║   " + request.url(), "\n║   请求body : \n$sb")
        }
        val t1 = System.currentTimeMillis()//请求发起的时间
        val response = chain.proceed(request)
        val t2 = System.currentTimeMillis()//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理

        val responseBody = response.peekBody((1024 * 1024).toLong())

        val url = response.request().url()
        LogUtil.ok(
            TAG, "接收响应: \n║   " +
                    url + "\n║   " +
                    "\n║   " +
                    "响应头：" + response.headers()
        )
        LogUtil.ok(TAG + ", " + responseBody.string())
        return response
    }

    companion object {
        private const val TAG = "CommLoggingInterceptor"
    }
}