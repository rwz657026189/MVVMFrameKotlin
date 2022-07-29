package com.landon.debug.net;

import com.landon.debug.DebugConfig;
import com.landon.debug.net.interceptor.LocalIpInterceptor;
import com.landon.debug.net.interceptor.TimeOutInterceptor;
import com.landon.debug.net.interceptor.mock.MockInterceptor;

import okhttp3.OkHttpClient;

/**
 * @Author Ren Wenzhang
 * @Date 2022/6/22/022 19:26
 * @Description
 */
public class NetInject {
    public static OkHttpClient initOkhttpClient(OkHttpClient client) {
        OkHttpClient.Builder builder = client.newBuilder();
        if (DebugConfig.NET_TIME) {
            builder.eventListener(new HttpEventListener());
        }
        if (DebugConfig.NET_LOGCAT) {
            builder.addInterceptor(new LogInterceptor());
        }
        if (DebugConfig.NET_MOCK) {
            builder.addInterceptor(MockInterceptor.getInstance());
        }
        if (DebugConfig.NET_TIMEOUT) {
            builder.addInterceptor(TimeOutInterceptor.getInstance());
        }
        if (DebugConfig.NET_LOCAL_IP) {
            builder.addNetworkInterceptor(new LocalIpInterceptor());
        }
        return builder.build();
    }
}
