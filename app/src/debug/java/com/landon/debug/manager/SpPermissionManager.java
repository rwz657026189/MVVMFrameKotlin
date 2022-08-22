/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2022. All rights reserved.
 */

package com.landon.debug.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.landon.debug.view.DebugNetActivity;

/**
 * @author rwx989128
 * @since 2022-01-25
 */
public class SpPermissionManager {
    public static final String VOLUME_CHANGED = "android.media.VOLUME_CHANGED_ACTION";

    private static final SpPermissionManager INSTANCE = new SpPermissionManager();

    public static SpPermissionManager getInstance() {
        return INSTANCE;
    }

    private final static class MyVolumeReceiver extends BroadcastReceiver {
        private final AudioManager audioManager;

        private int mCurrVolume;

        public MyVolumeReceiver(Context context) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(VOLUME_CHANGED)){
                int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) ;// 当前的媒体音量
                if (currVolume > mCurrVolume) {
                    SpPermissionManager.getInstance().turnLog(context);
                }
                mCurrVolume = currVolume;
            }
        }
    }

    private void turnLog(Context context) {
        Intent intent = new Intent(context, DebugNetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 注册当音量发生变化时接收的广播
     */
    public void registerReceiver(Context context) {
        MyVolumeReceiver mVolumeReceiver = new MyVolumeReceiver(context) ;
        IntentFilter filter = new IntentFilter() ;
        filter.addAction(VOLUME_CHANGED) ;
        context.registerReceiver(mVolumeReceiver, filter) ;
    }
}
