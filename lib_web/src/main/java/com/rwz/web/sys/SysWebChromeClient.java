package com.rwz.web.sys;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.rwz.lib_comm.utils.show.LogUtil;
import com.rwz.web.AbsWebViewProxy;
import com.rwz.web.IPostEvent1;
import com.rwz.web.InfWebView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by rwz on 2017/8/31.
 */

public class SysWebChromeClient extends WebChromeClient {

    private static final String TAG = SysWebViewProxy.TAG + "_SysWebChromeClient";

    private final static int FILE_CHOOSER_RESULT_CODE = 2;

    private String mTitle;
    private final Activity mActivity;
    //可实现上传文件
    private ValueCallback<Uri> mFilePathCallback4;
    private ValueCallback<Uri[]> mFilePathCallback5;
    private IPostEvent1<Integer> onLoadProgress;
    private InfWebView mInfWebView;
    private View mXCustomView;
    private CustomViewCallback mXCustomViewCallback;
    private FullscreenContainer mVideoFullView;

    public SysWebChromeClient(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setInfWebView(InfWebView mInfWebView) {
        this.mInfWebView = mInfWebView;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setOnLoadProgress(IPostEvent1<Integer> onLoadProgress) {
        this.onLoadProgress = onLoadProgress;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (onLoadProgress != null) {
            onLoadProgress.onEvent(newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        this.mTitle = title;
        LogUtil.INSTANCE.d(TAG, "onReceivedTitle", "title = " + title);
        super.onReceivedTitle(view, title);
        if (TextUtils.equals(AbsWebViewProxy.EMPTY_CONTENT, title)) {
            title = "";
        }
        if(mInfWebView != null)
            mInfWebView.onReceivedTitle(title);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        LogUtil.INSTANCE.d(TAG, "onJsAlert","isDialog = " + isDialog, "message =" + resultMsg);
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    public void openFileChooser(ValueCallback<Uri> filePathCallback) {
        mFilePathCallback4 = filePathCallback;
        chooseFile();
    }

    public void openFileChooser(ValueCallback filePathCallback, String acceptType) {
        mFilePathCallback4 = filePathCallback;
        chooseFile();
    }

    public void openFileChooser(ValueCallback<Uri> filePathCallback, String acceptType, String capture) {
        mFilePathCallback4 = filePathCallback;
        chooseFile();
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        mFilePathCallback5 = filePathCallback;
        //https://issuetracker.google.com/issues/36983532#c120 解决不能上传文件
        chooseFile();
        return true;
    }

    private void chooseFile() {
        if (mActivity != null) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            mActivity.startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode,Intent intent) {
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null != mFilePathCallback4) {
                Uri result = intent == null || resultCode != RESULT_OK ? null: intent.getData();
                mFilePathCallback4.onReceiveValue(result);
                mFilePathCallback4 = null;
            }
            if (null != mFilePathCallback5) {
                Uri result = intent == null || resultCode != RESULT_OK ? null: intent.getData();
                if (result != null) {
                    mFilePathCallback5.onReceiveValue(new Uri[]{result});
                }else{
                    mFilePathCallback5.onReceiveValue(null);
                }
                mFilePathCallback5 = null;
            }
        }
    }

    public void onDestroy() {

    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
        showFullScreen(view, callback);
    }

    @Override
    public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
        super.onShowCustomView(view, requestedOrientation, callback);
        showFullScreen(view, callback);
    }

    private void showFullScreen(View view, CustomViewCallback callback) {
        LogUtil.INSTANCE.d(TAG, "showFullScreen", "mXCustomView = " + mXCustomView);
        // 如果一个视图已经存在，那么立刻终止并新建一个
        if (mXCustomView != null) {
            callback.onCustomViewHidden();
            return;
        }
        this.mXCustomView = view;
        this.mXCustomViewCallback = callback;
        if (mInfWebView != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
            mVideoFullView = new FullscreenContainer(mActivity);
            mVideoFullView.addView(view);
            mInfWebView.onFullScreenChanged(true, mVideoFullView);
        }
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
        LogUtil.INSTANCE.d(TAG, "onHideCustomView", "mXCustomView = " + mXCustomView);
        if (mXCustomView == null)// 不是全屏播放状态
            return;
        mXCustomView.setVisibility(View.GONE);
        mXCustomView = null;
        mXCustomViewCallback.onCustomViewHidden();
        mXCustomViewCallback = null;
        mVideoFullView.removeView(mXCustomView);
        if (mInfWebView != null) {
            mInfWebView.onFullScreenChanged(false, mVideoFullView);
        }
    }


}
