/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.landon.debug.manager;

import com.landon.debug.utils.ContextUtils;
import com.landon.debug.utils.FileUtil;
import com.landon.debug.utils.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    private final String downloadDir;

    public DebugLogManager() {
        String dir = ContextUtils.getContext().getExternalFilesDir("") + File.separator + "cache";
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        File file = new File(dir + File.separator + date);
        if (!file.exists()) {
            file.mkdirs();
        }
        downloadDir = dir;
    }

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

    public String download(long timestamp, String header, String body, String url, String response, String encodePath) {
        String lineSeparator = System.lineSeparator();
        String time = FORMAT.format(new Date(timestamp));
        String text = url;
        text += lineSeparator + "start-time: " + time;
        text += lineSeparator + "header: " + lineSeparator + header;
        text += lineSeparator + "body: " + lineSeparator + body;
        text += lineSeparator + lineSeparator +
                "────────────────────────────────────────────────────────────────────────────────────────" + lineSeparator;
        text += LogUtil.formatJson(response);
        String fileName = time + "-" + encodePath + ".txt";
        fileName = fileName.replaceAll(" ", "_");
        fileName = fileName.replaceAll("/", "_");
        String filePath = downloadDir + File.separator + fileName;
        FileUtil.writeText(filePath, text);
        return filePath;
    }
}
