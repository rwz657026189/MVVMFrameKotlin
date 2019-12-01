package com.rwz.lib_comm.utils.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.FileProvider;


import com.rwz.lib_comm.R;
import com.rwz.lib_comm.utils.show.LogUtil;
import com.rwz.lib_comm.utils.system.AndroidUtils;

import java.io.File;

/**
 * Created by rwz on 2017/8/29.
 */

public class InstallHelp {

    /**
     * 当前使用databinding时不能直接安装APK,否则坑爹的谷歌会把你坑的不要不要的！！！
     * @param context
     * 安装Apk(适配android 7.0)
     */
    public static void installApk(Context context, String apkPath) {
        LogUtil.INSTANCE.d("installApk","context = " + context, "apkPath = "+apkPath);
        if (context == null || TextUtils.isEmpty(apkPath) || (!apkPath.contains(ResourceUtil.getString(R.string.apk)))) {
            return;
        }
        File file = new File(apkPath);
        installApk(context, file);
    }

    public static void installApk(Context context, File file) {
        LogUtil.INSTANCE.d("installApk", "context = " + context, "file = " + file);
        if (context != null && file != null && file.exists()) {
            try {
                ///storage/emulated/0/Android/data/com.touchrom.yuliao/files/storage/emulated/0/yuliao/temp/yuliao(3).apk
                LogUtil.INSTANCE.d("installApk", "context = " + context, "path = " + file.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                    //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                    //file:///storage/emulated/0/fjz/update/fanjianzhi.apk
                    String packageName = AndroidUtils.getPackageName(context);
                    LogUtil.INSTANCE.d("installApk", "context = " + context, "path = " + file.getAbsolutePath(), "packageName = " + packageName);
                    Uri apkUri = FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                }else{
                    intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                }
                boolean result = CommUtils.canTurn(context, intent);
                LogUtil.INSTANCE.d("installApk", "install result = " + result);
                if(result)
                    context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (file != null) {
            LogUtil.INSTANCE.d("installApk", "context = " + context, "file.exists = " + file.exists());
        }
    }

    /**
     * 安装app
     * @param context
     * @param uri
     */
    public static void installApk(Context context, Uri uri) {
        if (context != null && uri != null) {
            try {
                ///storage/emulated/0/Android/data/com.touchrom.yuliao/files/storage/emulated/0/yuliao/temp/yuliao.apk
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
