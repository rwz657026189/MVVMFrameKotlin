package com.rwz.web;


import android.content.Intent;
import android.view.View;



/**
 * Created by rwz on 2017/9/12.
 *
 */

public abstract class AbsWebViewProxy {

    public static final String EMPTY_CONTENT = "about:blank";

    public AbsWebViewProxy() {
        UrlManager.getInstance().clear();
    }

    /**
     * 设置进度监听
     * @param onLoadProgress
     */
    public AbsWebViewProxy setOnLoadProgress(IPostEvent1<Integer> onLoadProgress){
        return this;
    }

    /**
     * 获取进度
     * @return
     */
    public String getTitle(){
        return null;
    }

    public String getCurrUrl() {
        return UrlManager.getInstance().firstElement();
    }

    /**
     * 设置加载url监听
     * @param webViewClient
     */
    public AbsWebViewProxy setInfWebView(InfWebView webViewClient){
        return this;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {}

    public void onDestroy() {
        UrlManager.getInstance().clear();
    }

    public abstract void loadUrl(String url);

    /**
     * @param url 加载地址
     * @param postParams    post参数
     * @param isAddBackStack    是否加入返回栈（仅限首页）
     */
    public abstract void loadUrl(String url, String postParams, boolean isAddBackStack);

    /** 重新加载 **/
    public abstract void reload();

    /** 返回上一页 **/
    public abstract boolean goBack();

    /** 返回首页 **/
    public abstract void turnMain();

    public abstract View getWebView();

}
