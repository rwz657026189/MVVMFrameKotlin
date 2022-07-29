package com.landon.debug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.landon.debug.inf.InfEvent;
import com.landon.debug.inf.InfParse;
import com.landon.debug.net.NetInject;
import com.landon.debug.net.interceptor.TimeOutInterceptor;
import com.landon.debug.net.interceptor.mock.InfMock;
import com.landon.debug.net.interceptor.mock.MockInterceptor;
import com.landon.debug.utils.AppDomainUtil;
import com.landon.debug.view.page.ActivityCallback;
import com.landon.debug.view.page.PageCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * @Author rwz
 * @Date 2022/7/28/028 19:59
 * @Description
 */
public class DebugManager {
    public static final String TAG = "debug";

    private static final DebugManager instance = new DebugManager();

    public static DebugManager getInstance() {
        return instance;
    }

    public static List<String> mRandImageUrl = new ArrayList<>();

    /**
     * 注册需要mock的接口数据
     *
     * @param mock
     * @return
     */
    public DebugManager registerMock(InfMock mock) {
        MockInterceptor.getInstance().register(mock);
        return this;
    }

    /**
     * 注册超时的接口
     *
     * @param url
     * @return
     */
    public DebugManager registerTimeOut(String url) {
        TimeOutInterceptor.getInstance().register(url);
        return this;
    }

    /**
     * 注册超时时长
     *
     * @param second
     * @return
     */
    public DebugManager setTimeOutSecond(int second) {
        TimeOutInterceptor.getInstance().setDelaySecond(second);
        return this;
    }

    /**
     * 设置网络请求域名
     *
     * @param key debug/release……
     * @param domain 域名
     * @return
     */
    public DebugManager putUrl(String key, String domain) {
        AppDomainUtil.putDomain(key, domain);
        return this;
    }

    /**
     * 根据保存的域名初始化
     *
     * @param event 域名
     * @return
     */
    public DebugManager initUrl(InfEvent<String> event) {
        String url = AppDomainUtil.getCurrUrl();
        if (!TextUtils.isEmpty(url)) {
            event.perform(url);
        }
        return this;
    }

    /**
     * 展示选择域名的弹窗，依赖{@link DebugManager#putUrl(String, String)}
     *
     * @param activity
     * @param defaultUrl
     * @param result
     * @return
     */
    public DebugManager showChooseUrl(Activity activity, String defaultUrl, InfEvent<String> result) {
        AppDomainUtil.showChooseDomain(activity, defaultUrl, result);
        return this;
    }

    /**
     * 随机的图片地址，用于mock数据
     *
     * @param url 图片地址
     * @return
     */
    public DebugManager putMockImageUrl(String url) {
        mRandImageUrl.add(url);
        return this;
    }

    /**
     * 转换okhttpClient, 用于网络请求
     *
     * @param event
     * @return
     */
    public DebugManager parseOkHttpClient(InfEvent<InfParse<OkHttpClient>> event) {
        event.perform(NetInject::initOkhttpClient);
        return this;
    }

    /**
     * 检测activity
     *
     * @param activityName
     * @param callback
     * @return
     */
    public DebugManager observerActivity(String activityName, PageCallback callback) {
        ActivityCallback.getInstance().register(activityName, callback);
        return this;
    }

    /**
     * 调整Launcher页面
     *
     * @param activity
     * @param cls
     */
    public void turnLauncher(Activity activity, Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        Bundle extras = activity.getIntent().getExtras();
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.finish();
    }
}
