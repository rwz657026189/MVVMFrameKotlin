package com.rwz.web;

import android.text.TextUtils;


import com.rwz.lib_comm.utils.show.LogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Created by rwz on 2018/4/17.
 */

public class UrlManager {

    private final Stack<String> mUrlStack;

    private final List<String> mCacheUrlArr = new ArrayList<>();//每单次请求缓存的地址，解决重定向的问题

    private final Set<String> mBlacklistHost = new HashSet<>();//黑名单域名，不加入历史记录（比如某些支付域名）

    private static UrlManager INSTANCE;

    public static UrlManager getInstance() {
        if(INSTANCE == null)
            synchronized (UrlManager.class) {
                if(INSTANCE == null)
                    INSTANCE = new UrlManager();
            }
        return INSTANCE;
    }

    private UrlManager() {
        mUrlStack = new Stack<>();
    }

    public void push(String url) {
        if (!TextUtils.isEmpty(url) && !isContainBlacklistHost(url)) {
            if (mUrlStack != null && !mUrlStack.contains(url)) {
                mUrlStack.push(url);
            }
            if (mCacheUrlArr != null && !mCacheUrlArr.contains(url)) {
                mCacheUrlArr.add(url);
            }
        }
    }

    private boolean isContainBlacklistHost(String url) {
        if(mBlacklistHost == null || url == null)
            return false;
        for (String s : mBlacklistHost) {
            if (url.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加黑名单(不加入历史栈中)
     */
    public void addBlacklistHost(String url) {
        if(mBlacklistHost != null)
            mBlacklistHost.add(url);
    }

    public void clear() {
        if(mUrlStack != null)
            mUrlStack.clear();
        if(mCacheUrlArr != null)
            mCacheUrlArr.clear();
    }

    public void pop() {
        clearCacheUrl();
        if(mUrlStack != null)
            mUrlStack.pop();
    }

    public String lastElement() {
        return size() == 0 ? null : mUrlStack.lastElement();
    }

    public String firstElement() {
        return size() == 0  ? null : mUrlStack.firstElement();
    }

    public int size() {
        return mUrlStack == null ? 0 : mUrlStack.size();
    }

    private int cacheUrlSize() {
        return mCacheUrlArr == null ? 0 : mCacheUrlArr.size();
    }

    public void clearCacheUrl() {
        if (mCacheUrlArr != null && mUrlStack != null) {
            //解决重定向的问题
            int size = mCacheUrlArr.size();
            if (size > 1) {
                //移除本次重定向之前的所有地址
                mCacheUrlArr.remove(size - 1);
                mUrlStack.removeAll(mCacheUrlArr);
            }
            mCacheUrlArr.clear();
        }
    }

    public void print() {
        LogUtil.INSTANCE.d("SysWebViewProxy, print", "size = " + size(), "cacheUrlSize = " + cacheUrlSize(),
                "\n UrlStack ：" + mUrlStack, "\n==============================================\n CacheUrlArr : " + mCacheUrlArr);
    }
}
