/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package com.landon.debug.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rwx989128
 * @since 2021-02-24
 */
public class MainEventUtils {
    //>>>>> Dispatching to Handler (android.view.Choreographer$FrameHandler) {260bca2} android.view.Choreographer$FrameDisplayEventReceiver@3f5dd33: 0
    //<<<<< Finished to Handler (android.view.Choreographer$FrameHandler) {260bca2} android.view.Choreographer$FrameDisplayEventReceiver@3f5dd33

    private static final Map<String, Long> cacheMap = new HashMap<>();
    public static String getDt(String text) {
        if (text == null) {
            return null;
        }
        String startPrefix = ">>>>> Dispatching to Handler ";
        String endPrefix = "<<<<< Finished to Handler ";
        boolean start = text.startsWith(startPrefix);
        if (start) {
            int index = text.lastIndexOf(": ");
            cacheMap.put(text.substring(startPrefix.length(), index > 0 ? index : text.length()), System.currentTimeMillis());
            return null;
        } else if (text.startsWith(endPrefix)) {
            String key = text.substring(endPrefix.length());
            Long value = cacheMap.get(key);
            if (value != null) {
                long dt = System.currentTimeMillis() - value;
                cacheMap.remove(key);
                if (dt > 16) {
                    return key + " => " + dt;
                }
            }
        }
        return null;
    }

}
