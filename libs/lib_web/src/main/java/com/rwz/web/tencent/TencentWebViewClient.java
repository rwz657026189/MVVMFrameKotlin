package com.rwz.web.tencent;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.rwz.lib_comm.utils.show.LogUtil;
import com.rwz.web.IPostEvent1;
import com.rwz.web.InfWebView;
import com.rwz.web.UrlManager;
import com.rwz.web.WebUtils;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by rwz on 2017/8/31.
 *
 *  http://blog.csdn.net/nic7968/article/details/47038947
 *
 */

class TencentWebViewClient extends WebViewClient {

    private static final String TAG = TencentWebViewProxy.TAG + "_TencentWebViewClient";
    private String mCurrUrl = "";//当前加载的url(必须有效的)
    private InfWebView mInfWebView;
    private final IPostEvent1<String> onLoadUrlListener;
    private final Activity aty;

    public TencentWebViewClient(Activity aty, IPostEvent1<String> onLoadUrlListener) {
        this.onLoadUrlListener = onLoadUrlListener;
        this.aty = aty;
    }

    public void setCurrUrl(String mCurrUrl) {
        if(TextUtils.isEmpty(this.mCurrUrl))
            this.mCurrUrl = mCurrUrl;
    }

    public void setInfWebView(InfWebView mInfWebView) {
        this.mInfWebView = mInfWebView;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LogUtil.INSTANCE.d(TAG, "onPageStarted", "url = " + url);
        if (mInfWebView != null) {
            mInfWebView.onPageStarted(url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LogUtil.INSTANCE.d(TAG, "onPageFinished", "url = " + url);
        if (WebUtils.isValidUrl(url)) {
            if (mInfWebView != null)
                mInfWebView.onPageFinished(url);
        } else {
            if(mInfWebView != null)
                mInfWebView.onReceivedError(url);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        LogUtil.INSTANCE.d(TAG, "【onReceivedError】", "errorCode = " + errorCode, "description = " + description, "failingUrl = " + failingUrl);
        if(onLoadUrlListener != null)
            onLoadUrlListener.onEvent("");
        if (mInfWebView != null) {
            mInfWebView.onReceivedError(failingUrl);
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        String url = request == null ? "" : request.getUrl() + "";
        LogUtil.INSTANCE.d(TAG, "【onReceivedError】", "url = " + url,"Description = " + error.getDescription(), "ErrorCode = " + error.getErrorCode());
        //资源加载失败不用处理
        if (TextUtils.equals(url, mCurrUrl)) {
            if(onLoadUrlListener != null)
                onLoadUrlListener.onEvent("");
            if (mInfWebView != null) {
                mInfWebView.onReceivedError(mCurrUrl);
            }
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtil.INSTANCE.d(TAG, "【shouldOverrideUrlLoading】", "url = " + url);
        //检查微信唤醒客户端
        if (checkWXPayInstalled(url)) {
            return true;
        }
        //检查支付宝唤醒客户端
        if (checkAliPayInstalled(url)) {
            return true;
        }
        //拦截自定义协议
        if (mInfWebView != null && mInfWebView.shouldOverrideUrlLoading(url)) {
            return true;
        }
        //如果不是http协议/https协议 直接不予以响应（拦截淘宝打开客户端失败等请求）
        if (WebUtils.isValidUrl(url)) {
            mCurrUrl = url;
            //拦截需要登录的地址
            view.loadUrl(url);
            UrlManager.getInstance().push(url);
        }
        return true;
    }


    //判断是否安装支付宝app
    private boolean checkAliPayInstalled(String url) {
        Context context = aty;
        if (!TextUtils.isEmpty(url) && url.contains("alipays://platformapi") && context != null) {
            Uri uri = Uri.parse ("alipays://platformapi/startApp");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            boolean result = componentName != null;
            if (result) {
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent2);
            }
            return result;
        }
        return false;
    }

    //判断是否安装微信app
    private boolean checkWXPayInstalled(String url) {
        Context context = aty;
        if (!TextUtils.isEmpty(url) && url.contains("weixin://wap/pay") && context != null) {
            Uri uri = Uri.parse ("weixin://wap/pay/startApp");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            boolean result = componentName != null;
            if (result) {
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent2);
            }
            return result;
        }
        return false;
    }

    public String getCurrUrl() {
        return mCurrUrl;
    }

    @Override
    public void onLoadResource(WebView webView, String s) {
        super.onLoadResource(webView, s);
    }





}
