/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.landon.debug.net.interceptor;


import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author rwx989128
 * @since 2022-01-18
 */
public class LocalIpInterceptor implements Interceptor {
    private static final String TAG = "LocalIpInterceptor";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().encodedPath();
        Connection connection = chain.connection();
        if (connection != null) {
            Socket socket = connection.socket();
            String hostName = socket.getLocalAddress().getHostName();
            int localPort = socket.getLocalPort();
            Log.d(TAG, hostName + ":" + localPort + ", url: " + path);
        } else {
            Log.d(TAG, "url: " + path + ", empty ip:port");
        }
        return chain.proceed(request);
    }
}
