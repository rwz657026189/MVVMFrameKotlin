package com.rwz.web.tencent;

import android.content.Context;
import android.util.AttributeSet;

import com.rwz.lib_comm.utils.show.LogUtil;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;

/**
 * Created by rwz on 2017/8/31.
 * https://x5.tencent.com/tbs/guide/sdkInit.html
 */

public class TencentWebView extends WebView {

    public TencentWebView(Context context) {
        super(context);
    }

    public TencentWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TencentWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public TencentWebView(Context context, AttributeSet attributeSet, int i, boolean b) {
        super(context, attributeSet, i, b);
    }

    public TencentWebView(Context context, AttributeSet attributeSet, int i, Map<String, Object> map, boolean b) {
        super(context, attributeSet, i, map, b);
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
