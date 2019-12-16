package com.rwz.web.tencent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;

import com.rwz.lib_comm.utils.show.LogUtil;
import com.rwz.web.AbsWebViewProxy;
import com.rwz.web.IPostEvent1;
import com.rwz.web.InfWebView;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by rwz on 2017/8/31.
 */

public class TencentWebChromeClient extends WebChromeClient {

    private static final String TAG = TencentWebViewProxy.TAG + "_TencentWebChromeClient";

    private final static int FILE_CHOOSER_RESULT_CODE = 2;

    private String mTitle;
    private final Activity mActivity;
    //可实现上传文件
    private ValueCallback<Uri> mFilePathCallback4;
    private ValueCallback<Uri[]> mFilePathCallback5;
    private IPostEvent1<Integer> onLoadProgress;
    private InfWebView mInfWebView;

    public TencentWebChromeClient(Activity mActivity) {
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
        mInfWebView.onReceivedTitle(title);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
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
}
