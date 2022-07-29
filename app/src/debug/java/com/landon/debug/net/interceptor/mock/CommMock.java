/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.landon.debug.net.interceptor.mock;

import okhttp3.Request;

/**
 * 通用
 *
 * @author rwx989128
 * @since 2022-01-18
 */
public class CommMock implements InfMock {
    protected String url;
    protected String method;
    protected String jsonFile;

    public CommMock(String url) {
        this(url, "json/" + url + ".json");
    }

    public CommMock(String url, String jsonFile) {
        this(url, null, jsonFile);
    }

    public CommMock(String url, String method, String jsonFile) {
        if (url != null && url.startsWith("/")) {
            url = url.substring(1);
        }
        this.url = url;
        this.method = method;
        this.jsonFile = jsonFile;
    }

    @Override
    public String responseData(Request request) {
        if (this.url == null) {
            return null;
        }
        String urlPath = request.url().encodedPath();
        String method = request.method();
        if (urlPath.startsWith("/")) {
            urlPath = urlPath.substring(1);
        }
        if (url.equals(urlPath) && (this.method == null || this.method.equalsIgnoreCase(method))) {
            return jsonFile;
        } else {
            return null;
        }
    }
}
