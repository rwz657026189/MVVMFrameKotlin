/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.landon.debug.net.interceptor.mock;

import android.text.TextUtils;

import com.landon.debug.utils.AssetUtil;
import com.landon.debug.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 拦截之后，不会再发送网络请求
 *
 * @author rwx989128
 * @since 2022-01-18
 */
public class MockInterceptor implements Interceptor {
    private static final String GET = "get";
    private static final String POST = "post";
    private static final String PUT = "put";
    private static final String DELETE = "delete";

    private static final MockInterceptor instance = new MockInterceptor();

    public static MockInterceptor getInstance() {
        return instance;
    }

    private final List<InfMock> mMockList = new ArrayList<>();

    private MockInterceptor() {
        // 无有效数据list
        register(RandomDataMock.getListMock());
        // 无有效数据object
        register(RandomDataMock.getObjectMock());
    }

    public void register(InfMock mock) {
        mMockList.add(mock);
    }

    public void unregister(InfMock mock) {
        mMockList.remove(mock);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        for (InfMock infMock : mMockList) {
            String result = infMock.responseData(request);
            if (result != null) {
                String json = "";
                if (result.startsWith("{") || result.startsWith("[")) {
                    json = result;
                } else {
                    json = AssetUtil.readFile(result);
                    if (TextUtils.isEmpty(json)) {
                        LogUtil.error("MockInterceptor", "path: " + result + " content is empty");
                        json = "";
                    }
                }

                MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
                ResponseBody responseBody = ResponseBody.create(mediaType, json);
                return new Response.Builder()
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("success")
                    .header(InfMock.RESP_TYPE, getRespPrintText(infMock.getRespType(), result))
                    .request(request)
                    .body(responseBody)
                    .build();
            }
        }
        return chain.proceed(request);
    }

    private String getRespPrintText(int respType, String fileName) {
        String result;
        if (respType == InfMock.RESP_MOCK_ASSETS) {
            result = "mock by assets: " + fileName;
        } else if (respType == InfMock.RESP_MOCK_CODE) {
            result = "mock by code";
        } else if (respType == InfMock.RESP_MOCK_RANDOM) {
            result = "mock by random";
        } else {
            return "";
        }
        return result;
    }
}
