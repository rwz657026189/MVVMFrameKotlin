/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package com.landon.debug.net;

import android.util.Log;

import com.landon.debug.adapter.NetDevManager;
import com.landon.debug.utils.ContextUtils;
import com.landon.debug.utils.LogUtil;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @author rwx989128
 * @since 2020-09-23
 */
public class LogInterceptor implements Interceptor {
    private static final String TAG = "HttpLog";

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    private final String cacheDir;

    public LogInterceptor() {
        String dir = ContextUtils.getContext().getExternalFilesDir("") + File.separator + "cache";
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        File file = new File(dir + File.separator + date);
        if (!file.exists()) {
            file.mkdirs();
        }
        cacheDir = dir;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        long startTime = System.currentTimeMillis();
        // 这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        RequestBody body = request.body();
        StringBuilder sb = new StringBuilder();
        if (body instanceof FormBody) {
            FormBody formBody = (FormBody) body;
            int size = formBody.size();
            for (int i = 0; i < size; i++) {
                sb.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
            }
        } else if (body instanceof MultipartBody) {
            sb.append("MultipartBody");
        } else if (body != null) {
            MediaType mediaType = body.contentType();
            if (mediaType != null && (mediaType.toString().contains("stream") || mediaType.toString().contains("image"))) {
                sb.append(mediaType);
            } else {
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                if (isValid(buffer)) {
                    sb.append(buffer.readString(StandardCharsets.UTF_8));
                } else {
                    sb.append(body.contentLength());
                }
            }
        } else {
            sb.append("");
        }
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        String contentType = response.header("Content-Type");
        String resp;
        if (contentType != null && contentType.contains("image")) {
            resp = "Content-Type: " + contentType;
        } else {
            resp = responseBody.string();
        }
        String header = request.headers() + "";
        String url = request.url() + "";
        String method = request.method();
        long dt = System.currentTimeMillis() - startTime;

        LogUtil.http(TAG, header, sb.toString(), url + System.lineSeparator() + "method：" + method
                + System.lineSeparator() + "dt：" + dt + "ms", resp);
//        cache(startTime, header, sb.toString(), url, resp, request.url().encodedPath());
        NetDevManager.INSTANCE.addNetResponse(url, sb.toString(), method, dt, resp);
        return response;
    }

    private boolean isValid(Buffer buff) {
        try {
            Buffer pref = new Buffer();
            long count = Math.min(buff.size(), 64);
            buff.copyTo(pref, 0, count);
            for (int i = 0; i < 16; i++) {
                if (pref.exhausted()) {
                    break;
                }
                int codePoint = pref.readUtf8CodePoint();
                boolean isoControl = Character.isISOControl(codePoint);
                boolean whitespace = Character.isWhitespace(codePoint);
                if (isoControl && !whitespace) {
                    return false;
                }
            }
            return true;
        } catch (EOFException ex) {
            return false;
        }
    }

    private void cache(long timestamp, String header, String body, String url, String response, String encodePath) {
        String lineSeparator = System.lineSeparator();
        String time = FORMAT.format(new Date(timestamp));
        String text = url;
        text += lineSeparator + "start-time: " + time;
        text += lineSeparator + "header: " + lineSeparator + header;
        text += lineSeparator + "body: " + lineSeparator + body;
        text += lineSeparator + lineSeparator +
            "────────────────────────────────────────────────────────────────────────────────────────" + lineSeparator;
        text += LogUtil.formatJson(response);
        String fileName = time + "-" + encodePath + ".txt";
        fileName = fileName.replaceAll(" ", "_");
        fileName = fileName.replaceAll("/", "_");
        Log.d(TAG, "cache: " + (cacheDir + File.separator + fileName));
//        NetEcoFileUtil.writeToFile(cacheDir + File.separator + fileName, text, false);
    }
}
