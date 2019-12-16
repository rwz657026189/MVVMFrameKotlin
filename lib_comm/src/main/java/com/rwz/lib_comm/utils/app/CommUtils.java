package com.rwz.lib_comm.utils.app;

import android.animation.ArgbEvaluator;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;


import com.rwz.lib_comm.R;
import com.rwz.lib_comm.manager.ContextManager;
import com.rwz.lib_comm.utils.show.LogUtil;
import com.rwz.lib_comm.utils.show.ToastUtil;
import com.rwz.lib_comm.utils.system.AndroidUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by rwz on 2018/6/12.
 */

public class CommUtils {

    /**
     * 判断intent是否可以安全跳转(主要是隐式跳转)
     */
    public static boolean canTurn(Context context, Intent intent) {
        return context != null && intent != null && intent.resolveActivity(context.getPackageManager()) != null;
    }

    /**
     * 复制文字到剪切板
     */
    public static void copyText(CharSequence text) {
        ClipboardManager cmb = (ClipboardManager) ContextManager.context.getSystemService(CLIPBOARD_SERVICE);
        String label = ResourceUtil.getString(R.string.copy);
        cmb.setPrimaryClip(ClipData.newPlainText(label, text));
        ToastUtil.INSTANCE.showShort(R.string.copy_text_completed);
    }

    public static String subString(String text, int num){
        String content = "";
        if (text.length() > num){
            content = text.substring(0, num -1) + "...";
        }else{
            content = text;
        }
        return content;
    }

    /**
     * @return 判断服务是否开启
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        if (TextUtils.isEmpty(serviceName) || context == null)
            return false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //android 26之后只返回本应用的服务，之前会返回所有正在运行的服务，故值需要足够大
        int maxNum = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 10 : 200;
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) (myManager != null ? myManager
                .getRunningServices(maxNum) : null);
        for (int i = 0; i < runningService.size(); i++) {
            if (TextUtils.equals(runningService.get(i).service.getClassName(), serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return 判断某个进程是否开启
     */
    public static boolean isProcessRunning(Context context, String processName) {
        if(context == null || processName == null)
            return false;
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info : lists){
            if(info.processName.equals(processName)){
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * @param isMute 值为true时为关闭背景音乐
     * @return
     */
    public static boolean muteBackgroundAudio(boolean isMute) {
        boolean bool;
        AudioManager am = (AudioManager)ContextManager.context.getSystemService(Context.AUDIO_SERVICE);
        if(isMute){
            int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }else{
            int result = am.abandonAudioFocus(null);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        return bool;
    }

    /**
     * 从文件获取uri
     */
    public static Uri getUriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            String packageName = AndroidUtils.getPackageName(context);
            return FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
        } else
            return Uri.fromFile(file);
    }

    /**
     * 设置editText 不可编辑(同TextView)
     */
    public static void setNotEditable(EditText editText) {
        if(editText == null) return;
        editText.setCursorVisible(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }

    /**
     * 根据首尾色值、进度获取一个中间过渡色值 参考{@link ArgbEvaluator#evaluate(float, Object, Object)}
     * @param startColor    开始颜色
     * @param endColor      结束颜色
     * @param progress      进度 （0f ~ 1f）
     */
    public static int getTransitionColor(int startColor, int endColor, float progress) {
        int startA = (startColor >> 24) & 0xff;
        int startR = (startColor >> 16) & 0xff;
        int startG = (startColor >> 8) & 0xff;
        int startB = startColor & 0xff;

        int endA = (endColor >> 24) & 0xff;
        int endR = (endColor >> 16) & 0xff;
        int endG = (endColor >> 8) & 0xff;
        int endB = endColor & 0xff;

        return ((startA + (int)(progress * (endA - startA))) << 24) |
                ((startR + (int)(progress * (endR - startR))) << 16) |
                ((startG + (int)(progress * (endG - startG))) << 8) |
                ((startB + (int)(progress * (endB - startB))));
    }

    /**
     * android程序间的分享文本
     */
    public static void shareTextToSystem(String content) {
        Context context = ContextManager.context;
        if (content != null) {
            Intent intent = new Intent();
//            intent.setComponent(new ComponentName("目标app包名", "目标app类名"));
            intent.setAction(Intent.ACTION_SEND);
            //分享文本内容
            intent.setType("text/plain");
            //添加分享标题
//            if(!TextUtils.isEmpty(title))
            //添加分享内容
//            intent.putExtra(Intent.EXTRA_TITLE, "title");
            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
            if (CommUtils.canTurn(context, intent)) {
                context.startActivity(Intent.createChooser(intent, ResourceUtil.getString(R.string.share_to)));
            }
        }
    }

}
