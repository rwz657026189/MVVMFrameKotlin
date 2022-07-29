package com.landon.debug.utils;

import android.content.Context;

/**
 * @Author Ren Wenzhang
 * @Date 2022/6/22/022 9:26
 * @Description
 */
public class ContextUtils {
    private static Context context;

    public static void init(Context context) {
        ContextUtils.context = context;
    }

    public static Context getContext() {
        return context;
    }
}
