/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.landon.debug.net.interceptor.mock;

import okhttp3.Request;

/**
 * 模拟维护公告
 *
 * @author rwx989128
 * @since 2022-01-18
 */
class AnyUrlMock implements InfMock{

    @Override
    public String responseData(Request request) {
        return "json/maintain.json";
    }

    @Override
    public int getRespType() {
        return InfMock.RESP_MOCK_ASSETS;
    }
}
