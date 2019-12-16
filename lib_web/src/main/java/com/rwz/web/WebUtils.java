package com.rwz.web;

import android.text.TextUtils;

/**
 * Created by rwz on 2018/4/17.
 */

public class WebUtils {

    /** 是否有效的地址 **/
    public static boolean isValidUrl(String url) {
        return !TextUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://"));
    }

}
