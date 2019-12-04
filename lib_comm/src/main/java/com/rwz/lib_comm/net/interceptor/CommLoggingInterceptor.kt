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
        val sb = StringBuilder()
        sb.append("【url】：${request.url()}").append("\n")
        sb.append("【header】").append("\n")
            .append("     ").append(request.headers()).append("\n")
        sb.append("【body】").append("\n")
        if (body is FormBody) {
            val size = body.size()
            for (i in 0 until size) {
                sb.append("     ").append(body.name(i)).append(" = ")
                    .append(body.value(i))
            }
        }
        sb.append("\n")
        LogUtil.ok(sb.toString())

        val startTime = System.nanoTime()//请求发起的时间
        val response = chain.proceed(request)
        val endTime = System.nanoTime()//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        val responseBody = response.peekBody((1024 * 1024).toLong())
        sb.clear()
        sb.append("【url】：${response.request().url()}").append("\n")
        sb.append("【响应时间】：${endTime - startTime}ms")
        LogUtil.ok(sb.toString())
        LogUtil.j(jsonStr = responseBody.string())
        return response
    }
}