package com.landon.debug.net;

import com.landon.debug.DebugConfig;
import com.landon.debug.net.interceptor.LocalIpInterceptor;
import com.landon.debug.net.interceptor.TimeOutInterceptor;
import com.landon.debug.net.interceptor.mock.MockInterceptor;

import java.util.List;

import okhttp3.Interceptor;
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
//            builder.eventListener(new HttpEventListener());
        }
        if (DebugConfig.NET_LOGCAT) {
            addInterceptor(builder.interceptors(), new LogInterceptor());
        }
        if (DebugConfig.NET_MOCK) {
            addInterceptor(builder.interceptors(), MockInterceptor.getInstance());
        }
        if (DebugConfig.NET_TIMEOUT) {
            addInterceptor(builder.interceptors(), TimeOutInterceptor.getInstance());
        }
        if (DebugConfig.NET_LOCAL_IP) {
            builder.addNetworkInterceptor(new LocalIpInterceptor());
        }
        return builder.build();
    }

    public static void addInterceptor(List<Interceptor> interceptors, Interceptor interceptor) {
        interceptors.add(interceptors.size() - 1, interceptor);
    }
}
