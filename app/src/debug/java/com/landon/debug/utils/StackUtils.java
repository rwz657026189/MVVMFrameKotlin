/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package com.landon.debug.utils;

import android.os.Looper;
import android.util.Log;

/**
 * @author rwx989128
 * @since 2020-11-29
 */
public class StackUtils {
    private static final String TAG = "StackUtils";

    private static void debug(String TAG, Object text) {
        Log.d(TAG, String.valueOf(text));
    }


    public static void stackTraces(String tag){
        stackTraces(tag, Thread.currentThread().getStackTrace());
        if (Looper.getMainLooper() != Looper.myLooper()) {
            debug(TAG, "StackUtils stackTraces：not main thread");
            stackTraces(tag, Looper.getMainLooper().getThread().getStackTrace());
        }
    }

    public static void stackTraces(String tag, StackTraceElement[] trace){
        stackTraces(tag, trace, 1000, 0);
    }

    public static void stackTraces(String tag, int methodCount){
        stackTraces(tag, Thread.currentThread().getStackTrace(), methodCount, 0);
    }

    public static void stackTraces(String tag, StackTraceElement[] trace, int methodCount, int methodOffset) {
        if (trace == null) {
            trace = new StackTraceElement[0];
        }
        methodCount = Math.min(methodCount, trace.length);
        debug(TAG, "--------- logStackTraces start " + tag + " ----------");
        debug(TAG, "StackUtils stackTraces：" + "length = " + trace.length + ", threadId = " + Thread.currentThread().getId()
                + ", mainThreadId = " + Looper.getMainLooper().getThread().getId());
        String level = "";
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + methodOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].getClassName())
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            debug(TAG, builder.toString());
            level = "   ";
        }
        debug(TAG, "--------- logStackTraces end ----------");
    }

    public static void safeRun(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
