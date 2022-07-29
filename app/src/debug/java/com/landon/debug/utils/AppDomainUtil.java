package com.landon.debug.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.widget.Toast;

import com.landon.debug.inf.InfEvent;
import com.landon.debug.view.dialog.DebugChooseDialog;

/**
 * @Author rwz
 * @Date 2022/7/28/028 20:16
 * @Description
 */
public class AppDomainUtil {
    private static final String KEY_BASE_URL = "BASE_URL";

    private static final ArrayMap<String, String> mUrl = new ArrayMap<>();

    public static void putDomain(String key, String domain) {
        mUrl.put(key, domain);
    }

    public static String getCurrUrl() {
        String baseUrlKey = SpUtil.getInstance().getString(KEY_BASE_URL, null);
        if (!TextUtils.isEmpty(baseUrlKey)) {
            return mUrl.get(baseUrlKey);
        }
        return null;
    }

    private static String parseKey(String url) {
        for (int i = 0; i < mUrl.size(); i++) {
            if (TextUtils.equals(mUrl.valueAt(i), url)) {
                return mUrl.keyAt(i);
            }
        }
        return null;
    }

    public static void showChooseDomain(Activity activity, String defaultUrl, InfEvent<String> result) {
        String baseUrl = SpUtil.getInstance().getString(KEY_BASE_URL, parseKey(defaultUrl));
        DebugChooseDialog.get()
                .setTitle("选择域名")
                .addAllItemName(mUrl.keySet())
                .setDefault(baseUrl)
                .show(activity, item -> {
                    if (result != null) {
                        result.perform(mUrl.get(item.name));
                    }
                    SpUtil.getInstance().putString(KEY_BASE_URL, item.name);
                    Toast.makeText(ContextUtils.getContext(), "已切换" + item.name + ", 重启生效", Toast.LENGTH_LONG).show();
                });
    }

}
