package com.rwz.web;

import android.view.View;

/**
 * Created by rwz on 2017/9/12.
 */

public interface InfWebView {

    void onPageStarted(String url);

    void onPageFinished(String url);

    void onReceivedError(String url);

    boolean shouldOverrideUrlLoading(String url);

    void onReceivedTitle(String title);

    //全屏设置改变时
    void onFullScreenChanged(boolean isFullScreen, View view);

}
