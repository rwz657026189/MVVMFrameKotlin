/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.landon.debug.net.interceptor.mock;

import okhttp3.Request;

/**
 * @author rwx989128
 * @since 2022-01-18
 */
public interface InfMock {
    /**
     * 拦截请求
     *
     * @param request 请求
     * @return null则不拦截，标准的json格式字符串，则直接返还，否则认为是asserts目录下的文件路径，需要读文件
     */
    String responseData(Request request);
}
