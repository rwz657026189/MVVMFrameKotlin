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
    String RESP_TYPE = "resp-type";

    // assets中的mock数据
    int RESP_MOCK_ASSETS = 101;
    // 硬编码的mock数据
    int RESP_MOCK_CODE = 102;
    // 随机mock数据
    int RESP_MOCK_RANDOM = 103;

    /**
     * 拦截请求
     *
     * @param request 请求
     * @return null则不拦截，标准的json格式字符串，则直接返还，否则认为是asserts目录下的文件路径，需要读文件
     */
    String responseData(Request request);

    /**
     * 获取mock数据类型，即来源标识
     *
     * @return RESP_TYPE
     */
    int getRespType();
}
