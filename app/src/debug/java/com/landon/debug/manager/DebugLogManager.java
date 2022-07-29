/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.landon.debug.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rwx989128
 * @since 2022-01-25
 */
public class DebugLogManager {
    private static final int MAX_CACHE_LENGTH = 1000;

    private static final DebugLogManager INSTANCE = new DebugLogManager();

    public static DebugLogManager getInstance() {
        return INSTANCE;
    }

    private final List<String> cacheLog = new ArrayList<>();

    public synchronized void put(String msg) {
        if (msg == null) {
            return;
        }
        cacheLog.add(msg);
        if (cacheLog.size() > MAX_CACHE_LENGTH) {
            cacheLog.remove(0);
        }
    }

    public List<String> getCacheLog() {
        return cacheLog;
    }

    public void clear() {
        cacheLog.clear();
    }
}
