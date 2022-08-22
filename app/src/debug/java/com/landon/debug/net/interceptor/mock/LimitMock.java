package com.landon.debug.net.interceptor.mock;

import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Request;

public class LimitMock extends CommMock {
    // 延迟n次执行
    private int delayCount = 0;

    // 运行执行次数
    private int performCount = 1;

    private AtomicInteger currCount = new AtomicInteger(0);

    public LimitMock(String url) {
        super(url);
    }

    public LimitMock(String url, String jsonFile) {
        super(url, jsonFile);
    }

    public LimitMock(String url, String method, String jsonFile) {
        super(url, method, jsonFile);
    }

    public LimitMock setDelayCount(int delayCount) {
        this.delayCount = delayCount;
        return this;
    }

    public LimitMock setPerformCount(int performCount) {
        this.performCount = performCount;
        return this;
    }

    @Override
    public String responseData(Request request) {
        String result = super.responseData(request);
        // 说明被拦截了
        if (result != null) {
            int count = currCount.incrementAndGet();
            // 超过执行次数
            if (count - delayCount > performCount) {
                // 移除mock
                MockInterceptor.getInstance().unregister(this);
                return null;
            }
            // 没达到延迟次数
            if (count < delayCount) {
                return null;
            }
        }
        return result;
    }
}
