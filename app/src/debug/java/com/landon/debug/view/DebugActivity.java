/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package com.landon.debug.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.landon.debug.adapter.DebugAdapter;
import com.landon.debug.manager.SpPermissionManager;
import com.landon.debug.utils.ContextUtils;
import com.landon.debug.utils.StackUtils;
import com.landon.debug.view.page.ActivityCallback;

/**
 * 入口activity， 【仅供测试】
 *
 * @author rwx989128
 * @since 2020-11-20
 *        <p>
 *        1. D:\android\android-sdk-windows\emulator
 *        2. emulator -avd Pixel_3a_API_28 -dns-server huawei.com
 *        <p>
 *        手机通过usb连接内网
 *        D:\program\gnirehtet-rust-win64\gnirehtet-run.cmd
 *
 */
public class DebugActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StackUtils.safeRun(() -> {
            ContextUtils.init(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Debug App", Toast.LENGTH_SHORT).show();
            getApplication().registerActivityLifecycleCallbacks(ActivityCallback.getInstance());
            SpPermissionManager.getInstance().registerReceiver(this);
            DebugAdapter.perform(this);
        });
    }

    private void copy() {
        String text = "copy";
        ClipboardManager cmb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("label", text));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("android:support:fragments", null);
    }
}
