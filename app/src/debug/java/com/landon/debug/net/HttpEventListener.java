///*
// * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
// */
//
//package com.landon.debug.net;
//
//import android.text.TextUtils;
//
//import androidx.annotation.Nullable;
//
//import com.landon.debug.utils.LogUtil;
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Proxy;
//import java.net.Socket;
//import java.net.SocketAddress;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.Connection;
//import okhttp3.EventListener;
//import okhttp3.Handshake;
//import okhttp3.HttpUrl;
//import okhttp3.Protocol;
//import okhttp3.Request;
//import okhttp3.Response;
//import okio.Timeout;
//
///**
// * @author rwx989128
// * @since 2020-11-16
// */
//public class HttpEventListener extends EventListener {
//    // 网络请求耗时提示阈值s
//    private static final int WARN_LIMIT = 30;
//    private static final String TAG = "HttpEventListener";
//    private static final String LINE = System.lineSeparator();
//    private final Map<String, Entity> mCacheMap = new HashMap<>();
//    /**
//     * 监听特定的请求
//     */
//    private String url;
//
//    public HttpEventListener() {
//    }
//
//    public HttpEventListener(String url) {
//        this.url = url;
//    }
//
//    private void recordEventLog(Call call, String name) {
//         Request req = call.request();
//         String reqUrl = req.url().toString();
//         if (!TextUtils.isEmpty(this.url) && !reqUrl.contains(this.url)) {
//             return;
//         }
//         Entity pair = mCacheMap.get(reqUrl);
//         StringBuilder sbLog;
//         long time;
//         if (pair == null) {
//             sbLog = new StringBuilder();
//             time = System.currentTimeMillis();
//             pair = new Entity(System.currentTimeMillis(), sbLog);
//             mCacheMap.put(reqUrl, pair);
//         } else {
//             sbLog = pair.second;
//             time = pair.first;
//         }
//         long curr = System.currentTimeMillis();
//         long elapseNanos = curr - time;
//         pair.first = curr;
//         float totalTime = (curr - pair.callTime) / 1000f;
//         String format = String.format(Locale.ENGLISH, "%.3fs | %.3fs | %s", elapseNanos / 1000f, totalTime, name);
//         LogUtil.error(TAG, format);
//         sbLog.append(format).append(LINE);
//         if (name.startsWith("callEnd") || name.startsWith("callFailed")) {
//             Request request = call.request();
//             Timeout timeout = call.timeout();
//             String msg = request.url() + " timeout = " + timeout.timeoutNanos() + "ns " + name +  LINE;
//             String text = "HttpEventListener recordEventLog：" + LINE
//                     + msg
//                     + sbLog.toString()
//                     + "duration = " + String.format(Locale.ENGLISH, "%.3fs", totalTime)  +  LINE
//                     + "---------------------------------------------------------------------------------------------------------------------";
//             // 打印出每个步骤的时间点
//             if (totalTime > WARN_LIMIT) {
//                 LogUtil.error(TAG, text);
//             } else {
//                 LogUtil.debug(TAG, text);
//             }
//             mCacheMap.remove(reqUrl);
//         }
//    }
//
//    @Override
//    public void requestFailed(Call call, IOException ioe) {
//        super.requestFailed(call, ioe);
//        recordEventLog(call, "requestFailed " + (ioe == null ? null : ioe.getMessage()));
//    }
//
//    @Override
//    public void responseFailed(Call call, IOException ioe) {
//        super.responseFailed(call, ioe);
//        recordEventLog(call, "responseFailed " + (ioe == null ? null : ioe.getMessage()));
//    }
//
//    @Override
//    public void callStart(Call call) {
//        super.callStart(call);
//        recordEventLog(call, "callStart");
//    }
//
//    @Override
//    public void dnsStart(Call call, String domainName) {
//        super.dnsStart(call, domainName);
//        recordEventLog(call, "dnsStart " + domainName);
//    }
//
//    @Override
//    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
//        super.dnsEnd(call, domainName, inetAddressList);
//        recordEventLog(call, "dnsEnd " + domainName + ", " + inetAddressList);
//    }
//
//    @Override
//    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
//        super.connectStart(call, inetSocketAddress, proxy);
//        recordEventLog(call, "connectStart");
//    }
//
//    @Override
//    public void secureConnectStart(Call call) {
//        super.secureConnectStart(call);
//        recordEventLog(call, "secureConnectStart");
//    }
//
//    @Override
//    public void secureConnectEnd(Call call, @Nullable Handshake handshake) {
//        super.secureConnectEnd(call, handshake);
//        recordEventLog(call, "secureConnectEnd");
//    }
//
//    @Override
//    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, @Nullable Protocol protocol) {
//        super.connectEnd(call, inetSocketAddress, proxy, protocol);
//        recordEventLog(call, "connectEnd");
//    }
//
//    @Override
//    public void connectFailed(
//            Call call, InetSocketAddress inetSocketAddress, Proxy proxy, @Nullable Protocol protocol, IOException ioe) {
//        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
//        recordEventLog(
//                call,
//                "connectFailed "
//                        + inetSocketAddress.getAddress().getHostAddress()
//                        + ", ioe = "
//                        + (ioe == null ? null : ioe.getMessage()));
//    }
//
//    @Override
//    public void connectionAcquired(Call call, Connection connection) {
//        super.connectionAcquired(call, connection);
//        recordEventLog(call, "connectionAcquired");
//        Socket socket = connection.socket();
//        SocketAddress address = socket.getLocalSocketAddress();
//        HttpUrl url = call.request().url();
//        LogUtil.debug(TAG, " address = " + address + ", url = " + url.encodedPath());
//
//        // HttpEventListener|SessionOutInterceptor
//
//    }
//
//    @Override
//    public void connectionReleased(Call call, Connection connection) {
//        super.connectionReleased(call, connection);
//        recordEventLog(call, "connectionReleased");
//    }
//
//    @Override
//    public void requestHeadersStart(Call call) {
//        super.requestHeadersStart(call);
//        recordEventLog(call, "requestHeadersStart");
//    }
//
//    @Override
//    public void requestHeadersEnd(Call call, Request request) {
//        super.requestHeadersEnd(call, request);
//        recordEventLog(call, "requestHeadersEnd");
//    }
//
//    @Override
//    public void requestBodyStart(Call call) {
//        super.requestBodyStart(call);
//        recordEventLog(call, "requestBodyStart");
//    }
//
//    @Override
//    public void requestBodyEnd(Call call, long byteCount) {
//        super.requestBodyEnd(call, byteCount);
//        recordEventLog(call, "requestBodyEnd");
//    }
//
//    @Override
//    public void responseHeadersStart(Call call) {
//        super.responseHeadersStart(call);
//        recordEventLog(call, "responseHeadersStart");
//    }
//
//    @Override
//    public void responseHeadersEnd(Call call, Response response) {
//        super.responseHeadersEnd(call, response);
//        recordEventLog(call, "responseHeadersEnd");
//    }
//
//    @Override
//    public void responseBodyStart(Call call) {
//        super.responseBodyStart(call);
//        recordEventLog(call, "responseBodyStart");
//    }
//
//    @Override
//    public void responseBodyEnd(Call call, long byteCount) {
//        super.responseBodyEnd(call, byteCount);
//        recordEventLog(call, "responseBodyEnd");
//    }
//
//    @Override
//    public void callEnd(Call call) {
//        super.callEnd(call);
//        recordEventLog(call, "callEnd");
//    }
//
//    @Override
//    public void callFailed(Call call, IOException ioe) {
//        super.callFailed(call, ioe);
//        recordEventLog(call, "callFailed " + (ioe == null ? null : ioe.getMessage()));
//    }
//
//    private static final class Entity {
//        private final long callTime;
//        private long first;
//        private StringBuilder second;
//
//        public Entity(long first, StringBuilder second) {
//            this.callTime = first;
//            this.first = first;
//            this.second = second;
//        }
//    }
//}
