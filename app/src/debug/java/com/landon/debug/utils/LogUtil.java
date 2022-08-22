/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package com.landon.debug.utils;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.landon.debug.DebugManager;
import com.landon.debug.manager.DebugLogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Set;

/**
 * 日志工具类
 *
 * @author qwx790670
 * @since 2019-11-11
 */
public class LogUtil {
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String TAG = "LogUtil";
    /**
     * 是否保存日志到文件
     */
    private static final boolean IS_DEBUG = true;
    /**
     * 是否输出日志及保存日志到文件
     */
    private static final boolean SHOW_LOG = true;
    private static boolean isLogExist = false;

    /**
     * error
     *
     * @param tag     tag
     * @param content content
     * @param <T>     泛型
     */
    public static <T> void error(String tag, T content) {
        if (!SHOW_LOG) {
            return;
        }
        if (tag == null) {
            tag = TAG;
        }
        final String msg = content == null ? "null" : content.toString();
        Log.e(tag, DebugManager.TAG + " | " + msg);
        writeLog(tag, msg);
    }

    /**
     * info
     *
     * @param tag     tag
     * @param content content
     * @param <T>     泛型
     */
    public static <T> void info(String tag, T content) {
        if (!SHOW_LOG) {
            return;
        }
        if (tag == null) {
            tag = TAG;
        }
        final String msg = content == null ? "null" : content.toString();
        Log.i(tag, msg);
        writeLog(tag, msg);
    }

    /**
     * verbose
     *
     * @param tag     tag
     * @param content content
     */
    public static <T> void verbose(String tag, T content) {
        if (!SHOW_LOG) {
            return;
        }
        if (tag == null) {
            tag = TAG;
        }
        if (content != null) {
            Log.v(tag, content.toString());
            writeLog(tag, content.toString());
        }
    }

    /**
     * debug
     *
     * @param tag     tag
     * @param content content
     * @param <T>     泛型
     */
    public static <T> void debug(String tag, T content) {
        if (!SHOW_LOG) {
            return;
        }
        if (tag == null) {
            tag = TAG;
        }
        final String msg = content == null ? "null" : content.toString();
        Log.d(tag, DebugManager.TAG + " | " + msg);
        writeLog(tag, msg);
    }

    /**
     * debug
     *
     * @param content content
     * @param <T>     泛型
     */
    public static <T> void debug(T content) {
        debug(TAG, content);
    }

    /**
     * warn
     *
     * @param tag     tag
     * @param content content
     * @param <T>     泛型
     */
    public static <T> void warn(String tag, T content) {
        if (!SHOW_LOG) {
            return;
        }
        if (tag == null) {
            tag = TAG;
        }
        if (content != null) {
            Log.w(tag, content.toString());
            writeLog(tag, content.toString());
        }
    }

    /**
     * writeLog
     *
     * @param tag     tag
     * @param content content
     */
    private static void writeLog(String tag, String content) {
    }

    /**
     * 替换日志不安全的内容 待修改
     *
     * @param log 日志
     * @return 安全打印
     */
    public static String getSafeLog(String log) {
        if (log != null) {
            if (log.contains(File.separator) || log.contains("Exception")) {
                return "hava a exception";
            }
        }
        return log;
    }

    /**
     * 网络请求日志打印
     *
     * @param tag      标签
     * @param header   头信息
     * @param body     请求体
     * @param url      url
     * @param response 响应体
     * @param respType 响应体数据类型
     */
    public static void http(String tag, String header, String body, String url, String response, String respType) {
        if (!SHOW_LOG) {
            return;
        }
        if (!IS_DEBUG) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DOUBLE_DIVIDER).append(LINE_SEPARATOR);
        if (!TextUtils.isEmpty(respType)) {
            stringBuilder.append("response data type: ").append(respType).append(LINE_SEPARATOR);
        }
        stringBuilder.append("url: ").append(url).append(LINE_SEPARATOR)
                .append("header: ").append(LINE_SEPARATOR)
                .append(header).append(LINE_SEPARATOR)
                .append("body: ").append(body).append(LINE_SEPARATOR)
                .append(SINGLE_DIVIDER).append(LINE_SEPARATOR)
                .append("response: ").append(LINE_SEPARATOR)
                .append(formatJson(response)).append(LINE_SEPARATOR)
                .append(DOUBLE_DIVIDER);
        String content = stringBuilder.toString();
        int count = 0;
        int maxWords = 3800; // 不要超过4k，还需要考虑额外字段以及换行符
        int length = content.length();
        while (count < length) {
            debug(tag, LINE_SEPARATOR + content.substring(count, Math.min(length, count + maxWords)));
            count += maxWords;
        }
        DebugLogManager.getInstance().put(content);
    }

    /**
     * 格式化json
     *
     * @param jsonStr json字符串
     * @return 格式化后的数据
     */
    public static String formatJson(String jsonStr) {
        String message;
        try {
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                message = jsonObject.toString(4);
            } else if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                message = jsonArray.toString(4);
            } else {
                message = jsonStr;
            }
        } catch (JSONException e) {
            message = jsonStr;
        }
        message = LINE_SEPARATOR + message;
        return message;
    }

    /**
     * 打印设备基本信息
     */
    public static void printDeviceInfo() {
//        StringBuffer sb = new StringBuffer();
//        Context context = ContextUtils.getContext();
//        String versionNo = AppInfoUtil.getInstance().getVersionNo(context);
//        String buildTime = Kits.getApplicationMetaData("build_time");
//        String lastCommit = Kits.getApplicationMetaData("last_commit");
//        sb.append(LINE_SEPARATOR).append(TOP_LEFT_CORNER + DOUBLE_DIVIDER)
//                .append(LINE_SEPARATOR).append(HORIZONTAL_DOUBLE_LINE)
//                .append("SDK：").append(android.os.Build.VERSION.SDK_INT)
//                .append(LINE_SEPARATOR).append(HORIZONTAL_DOUBLE_LINE)
//                .append("VERSION_CODE：").append(versionNo)
//                .append(LINE_SEPARATOR).append(HORIZONTAL_DOUBLE_LINE)
//                .append("BUILD_TIME：").append(buildTime)
//                .append(LINE_SEPARATOR).append(HORIZONTAL_DOUBLE_LINE)
//                .append("LAST_COMMIT：").append(lastCommit)
//                .append(LINE_SEPARATOR).append(BOTTOM_LEFT_CORNER).append(DOUBLE_DIVIDER);
//        debug(null, sb);
    }

    /**
     * 在super.onCreate(savedInstanceState)方法之前调用
     *
     * @param bundle
     * @return
     */
    public static String toJsonString(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        Set<String> keySets = bundle.keySet();
        if (keySets.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String key : keySets) {
            Object obj = bundle.get(key);
            sb.append(",");
            final String value;
            if (obj instanceof Bundle) {
                value = toJsonString((Bundle) obj);
            } else {
                value = objToString(obj);
            }
            sb.append("\"").append(key).append("\":").append(TextUtils.isEmpty(value) ? null : value);
        }
        return "{" + sb.substring(1) + "}";
    }

    public static String objToString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof Number || obj instanceof String || obj instanceof Boolean) {
                return obj.toString();
            } else if (obj.getClass().isArray()) {
                final int length = Array.getLength(obj);
                JSONArray array = new JSONArray();
                for (int i = 0; i < length; i++) {
                    array.put(new JSONObject(ReflectionUtils.objToMap(Array.get(obj, i))).toString());
                }
                return length == 0 ? null : array.toString();
            } else {
                return new JSONObject(ReflectionUtils.objToMap(obj)).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
