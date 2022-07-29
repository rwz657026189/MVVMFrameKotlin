package com.landon.debug.net.interceptor;

import androidx.annotation.NonNull;

import com.landon.debug.utils.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author Ren Wenzhang
 * @Date 2021/7/19/019 18:46
 * @Description 延迟请求拦截器
 */
public class TimeOutInterceptor implements Interceptor {
    private int mDelaySecond = 60;

    private static final TimeOutInterceptor instance = new TimeOutInterceptor();

    public static TimeOutInterceptor getInstance() {
        return instance;
    }

    private final List<String> mUrlList = new ArrayList<>();

    public TimeOutInterceptor setDelaySecond(int mDelaySecond) {
        this.mDelaySecond = mDelaySecond;
        return this;
    }

    public final TimeOutInterceptor register(String url) {
        mUrlList.add(url);
        return this;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String urlPath = request.url().encodedPath();
        if (urlPath.startsWith("/")) {
            urlPath = urlPath.substring(1);
        }
        for (String url : mUrlList) {
            if (urlPath.equals(url)) {
                try {
                    LogUtil.error("TimeOutInterceptor", url + " 模拟耗时加载，延时" + mDelaySecond + "s");
                    Thread.sleep(Math.max(mDelaySecond * 1000, 0));
                } catch (InterruptedException e) {
                }
            }
        }
        return chain.proceed(request);
    }
}
