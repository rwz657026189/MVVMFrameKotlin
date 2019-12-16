package com.rwz.web.sys;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.rwz.lib_comm.utils.show.LogUtil;


/**
 * Created by rwz on 2017/8/31.
 * https://x5.tencent.com/tbs/guide/sdkInit.html
 */

public class SysWebView extends WebView {

    public SysWebView(Context context) {
        super(context);
    }

    public SysWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SysWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            LogUtil.INSTANCE.d("VideoWebView","销毁webView");
            removeAllViews();
            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
