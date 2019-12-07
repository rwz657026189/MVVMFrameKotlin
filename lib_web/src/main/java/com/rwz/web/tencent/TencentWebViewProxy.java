package com.rwz.web.tencent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.rwz.lib_comm.utils.show.LogUtil;
import com.rwz.web.AbsWebViewProxy;
import com.rwz.web.IPostEvent1;
import com.rwz.web.InfWebView;
import com.rwz.web.UrlManager;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import java.io.UnsupportedEncodingException;

/**
 * Created by rwz on 2017/8/31.
 * 参考： https://x5.tencent.com/tbs/guide/sdkInit.html
 */

public class TencentWebViewProxy extends AbsWebViewProxy {

    public static final String TAG = "TencentWebViewProxy";

    private WebView mWebView;
    private WebSettings mWebSetting;
    private Context mContext;
    private TencentWebChromeClient mWebChromeClient;
    private TencentWebViewClient mWebViewClient;
    private static boolean isInit = false;
    /**
     * 仅仅需要在application中初始化一次
     * @param context
     */
    public static void init(Context context) {
        isInit = true;
        //App 首次就可以加载 x5 内核
        QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                LogUtil.INSTANCE.d(TAG , "QbSdk", "onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                LogUtil.INSTANCE.d(TAG , "QbSdk", "onViewInitFinished, b = " + b);
            }
        });
    }

    public TencentWebViewProxy(Activity aty, WebView webView) {
        if (!isInit) {
            throw new RuntimeException("please call init() at application");
        }
        this.mWebView = webView;
        if (webView == null) {
            return;
        }
        mContext = aty;
        initSetting();
        initChromeClient(aty);
        mWebViewClient = new TencentWebViewClient(aty, onLoadUrlListener);
        mWebView.setWebViewClient(mWebViewClient);
        //为webView设置代理
//        boolean result = TencentSettingProxy.setProxy(mWebView, "127.0.0.1", 10808, null);
//        WebView.enablePlatformNotifications();
//        LogUtil.INSTANCE.d(TAG, "setProxy", "result = " + result);
    }

    final IPostEvent1<String> onLoadUrlListener = new IPostEvent1<String>() {
        @Override
        public void onEvent(String s) {
            loadUrl(s);
        }
    };

    @Override
    public void loadUrl(String url) {
        loadUrl(url, null, true);
    }

    @Override
    public void loadUrl(String url, String postParams, boolean isAddBackStack) {
        LogUtil.INSTANCE.d(TAG, "loadUrl", "url = " + url, "  postParams = " + postParams);
        if (url != null && mWebView != null) {
            if (UrlManager.getInstance().size() == 0 && isAddBackStack) {
                UrlManager.getInstance().push(url);
            }
            if (TextUtils.isEmpty(postParams)) {
                mWebView.loadUrl(url);
            } else {
                try {
                    mWebView.postUrl(url, postParams.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initChromeClient(Activity aty) {
        mWebChromeClient = new TencentWebChromeClient(aty);
        mWebView.setWebChromeClient(mWebChromeClient);
    }

    @Override
    public AbsWebViewProxy setInfWebView(InfWebView infWebView) {
        if (mWebChromeClient != null) {
            mWebChromeClient.setInfWebView(infWebView);
        }
        if (mWebViewClient != null) {
            mWebViewClient.setInfWebView(infWebView);
        }
        return this;
    }

    //加载进度
    @Override
    public AbsWebViewProxy setOnLoadProgress(IPostEvent1<Integer> onLoadProgress) {
        if (mWebChromeClient != null) {
            mWebChromeClient.setOnLoadProgress(onLoadProgress);
        }
        return this;
    }

    @Override
    public String getTitle() {
        return mWebChromeClient == null ? "" : mWebChromeClient.getTitle();
    }

    private void initSetting() {
        if (mWebView != null) {
            mWebSetting = mWebView.getSettings();
            mWebSetting.setJavaScriptEnabled(true);
            //设置该句导致该异常
            //android.app.IntentReceiverLeaked: Activity com.fjz.app.activity.web.NormalWebView has leaked IntentReceiver
            //android.widget.ZoomButtonsController$1@eaf84e1 that was originally registered here. Are you missing a call
            //to unregisterReceiver()?
            //使用localStorage则必须打开, 没有该语句会导致部分网站（如https://www.instagram.com）加载不出来
            mWebSetting.setDomStorageEnabled(true);
            mWebSetting.setLoadWithOverviewMode(false);
            //设置自适应屏幕，两者合用
//        mWebSetting.setUseWideViewPort(true); //将图片调整到适合webView的大小
//        mWebSetting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            mWebSetting.setAllowFileAccess(true); //设置可以访问文件
            mWebSetting.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            mWebSetting.setJavaScriptEnabled(true); //允许加载javascript
//        mWebSetting.setLoadsImagesAutomatically(true); //支持自动加载图片
            mWebSetting.setDefaultTextEncodingName("utf-8");//设置编码格式
//        mWebSetting.setBlockNetworkImage(false);
//        mWebSetting.setLoadsImagesAutomatically(false);
//        mWebSetting.setSupportZoom(true);
//        mWebSetting.setBuiltInZoomControls(true);
            mWebSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebSetting.setDatabaseEnabled(true);

            //不添加该句,部分网址(混合协议)图片加载不出来
            //https://github.com/HuarenYu/note/issues/2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                mWebSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            CookieManager.getInstance().setAcceptCookie(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
            }
            mWebSetting.setBlockNetworkImage(false);//是否阻塞网络图片加载
            mWebSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mWebChromeClient != null)
            mWebChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ViewParent parent = mWebView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                try {
                    ((ViewGroup) parent).removeAllViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
//        System.exit(0);
    }

    @Override
    public void reload() {
        if (mWebView != null) {
            String url = UrlManager.getInstance().lastElement();
            loadUrl(url);
        }
    }

    @Override
    public boolean goBack() {
        boolean canBack = false;
        if (UrlManager.getInstance().size() > 1) {
            canBack = true;
            UrlManager.getInstance().pop();
            loadUrl(UrlManager.getInstance().lastElement());
        }
        return canBack;
    }

    @Override
    public void turnMain() {
        String url = UrlManager.getInstance().firstElement();
        UrlManager.getInstance().clear();
        UrlManager.getInstance().push(url);
        loadUrl(url);
        if(mWebView != null)
            mWebView.postDelayed(new Runnable(){
                @Override
                public void run(){
                    if(mWebView != null)
                        mWebView.clearHistory();
                }
            }, 1000);
    }

    @Override
    public View getWebView() {
        return mWebView;
    }


}
