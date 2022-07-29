package com.landon.debug.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author Ren Wenzhang
 * @Date 2022/6/25/025 10:13
 * @Description
 */
public class SpUtil {
    private static final String SP_NAME = "debug";

    private static final SpUtil instance = new SpUtil();
    private final SharedPreferences mSp;

    private SpUtil() {
        Context context = ContextUtils.getContext();
        mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static SpUtil getInstance() {
        return instance;
    }

    public void putString(String key, String value) {
        mSp.edit().putString(key, value).commit();
    }

    public String getString(String key, String defaultValue) {
        return mSp.getString(key, defaultValue);
    }
}
