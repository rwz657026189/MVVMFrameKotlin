package com.rwz.lib_comm.net

import com.google.gson.GsonBuilder
import com.rwz.lib_comm.config.SHOW_NET_ERROR
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * date： 2019/11/7 16:41
 * author： rwz
 * description：
 **/
object RetrofitManager{

    private const val TIME_OUT = 10000
    private var mRetrofit: Retrofit? = null
    //头信息
    private var mHeaderMap: MutableMap<String, String>? = null

    fun init(host: String, headerMap: HashMap<String, String>? = null) {
        mHeaderMap = headerMap
        mRetrofit = createRetrofit(TIME_OUT, host)
    }

    private fun createRetrofit(timeOutMillSeconds: Int, host: String) : Retrofit{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(host)
            .client(getClient(timeOutMillSeconds))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun getClient(timeOutMillSeconds: Int): OkHttpClient {
        var builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val newBuilder = request.newBuilder()
                val header = mHeaderMap
                if (header != null && header.isNotEmpty()) {
                    val keys = header.keys
                    for (key in keys) {
                        header[key]?.let { newBuilder.addHeader(key, it).build() }
                    }
                }
                val build = newBuilder.build()
                chain.proceed(build)
            }
            .retryOnConnectionFailure(true) //失败后重连
            .connectTimeout(timeOutMillSeconds.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(timeOutMillSeconds.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(timeOutMillSeconds.toLong(), TimeUnit.MILLISECONDS)
        if (SHOW_NET_ERROR)
            builder = builder.addInterceptor(CommLoggingInterceptor())
        return builder.build()
    }

    fun <T> getService(c: Class<T>?): T = mRetrofit!!.create(c)

    fun addHeaderParams(headerMap: MutableMap<String, String>) {
        if (this.mHeaderMap == null)
            this.mHeaderMap = HashMap()
        this.mHeaderMap?.putAll(headerMap)
    }

    fun addHeaderParam(key: String, value: String): RetrofitManager {
        if (this.mHeaderMap == null)
            this.mHeaderMap = HashMap()
        this.mHeaderMap?.let { it[key] = value }
        return this
    }

}