package com.rwz.lib_comm.utils.system;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.rwz.lib_comm.utils.app.CommUtils;
import com.rwz.lib_comm.utils.show.LogUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * android系统工具类
 */
public class AndroidUtils {
    private static final String TAG = "AndroidUtils";

    private AndroidUtils() {

    }

    /**
     * 检查权限
     *
     * @param permission android.permission.WRITE_EXTERNAL_STORAGE
     * @return manifest 已经定义了则返回true
     */
    public static boolean checkPermission(@NonNull Context context, @NonNull String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 申请root权限
     *
     * @return 应用程序是/否获取Root权限
     */
    public static boolean getRootPermission(String pkgCodePath) {
        Process          process = null;
        DataOutputStream os      = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定包名的包信息
     */
    public static PackageInfo getAppInfo(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取未安装软件包的包名
     */
    public static String getApkPackageName(Context context, String apkPath) {
        PackageManager pm   = context.getPackageManager();
        PackageInfo    info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            return appInfo.packageName;
        }
        return "";
    }

    /**
     * 判断是否安装
     */
    public static boolean apkIsInstall(Context context, String apkPath) {
        PackageManager pm   = context.getPackageManager();
        PackageInfo    info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                pm.getPackageInfo(appInfo.packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                return true;
            } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断服务是否后台运行
     *
     * @param context Context
     * @param clazz
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRun(Context context, Class<?> clazz) {
        boolean                                  isRun           = false;
        ActivityManager                          activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager == null)
            return false;
        List<ActivityManager.RunningServiceInfo> serviceList     = activityManager.getRunningServices(Integer.MAX_VALUE);
        int                                      size            = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(clazz.getName())) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }
    /** 判断当前app是否处于前台 **/
    public static boolean isRunningForeground(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null)
            return false;
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return currentPackageName != null && currentPackageName.equals(context.getPackageName());
    }

    /**
     * 启动另外一个App
     */
    public static void startOtherApp(Context context, String packageName) {
        PackageManager pm             = context.getPackageManager();
        Intent         launcherIntent = pm.getLaunchIntentForPackage(packageName);
        if (CommUtils.canTurn(context, launcherIntent))
            context.startActivity(launcherIntent);
    }

    /**
     * 判断手机是否拥有Root权限。
     *
     * @return 有root权限返回true，否则返回false。
     */
    public static boolean isRoot() {
        String binPath  = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        return new File(binPath).exists() && isExecutable(binPath) || new File(xBinPath).exists() && isExecutable(xBinPath);
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String str = in.readLine();
            LogUtil.INSTANCE.d(TAG, str);
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result     = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources  = context.getResources();
        int       resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取当前窗口的高度， 该高度是不包含导航栏和状态栏的
     */
    public static int getWindowHeight(Activity activity) {
        return activity.getWindow().getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 获取当前窗口的宽度
     *
     * @param activity
     * @return
     */
    public static int getWindowWidth(Activity activity) {
        return activity.getWindow().getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕参数，这个获取到的高是包含导航栏和状态栏的
     *
     * @param context
     * @return
     */
    public static int[] getScreenParams(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return new int[]{wm != null ? wm.getDefaultDisplay().getWidth() : 0, wm.getDefaultDisplay().getHeight()};
    }

    /**
     * 检查手机是否安装了指定的APK
     *
     * @param packageName 该APK的包名
     * @return
     */
    public static boolean checkApkExists(Context context, String packageName) {
        List<PackageInfo> packageInfos = getAllApps(context);
        for (PackageInfo pi : packageInfos) {
            if (pi.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询手机内所有应用
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps     = new ArrayList<>();
        PackageManager    pManager = context.getPackageManager();
        //获取手机内所有应用
        return pManager.getInstalledPackages(0);
    }

    /**
     * 查询手机内非系统应用
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllNoSystemApps(Context context) {
        List<PackageInfo> apps     = new ArrayList<>();
        PackageManager    pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    /**
     * 获取目录
     */
    public static String getSourcePath(Context context, String packageName) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            return appInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取包名
     */
    public static String getPackageName(Context context) {
        return getPackageInfo(context).packageName;
    }

    /**
     * 获取应用名
     */
    public static String getAppName(Context context) {
        try {
            return context.getString(context.getApplicationInfo().labelRes);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取设备的唯一ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 返回版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 返回版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 打开一个隐藏了图标的APP
     *
     * @param context
     */
    public static void openAppWithAction(Context context, String packageName, String activity) {
        ComponentName componentName = new ComponentName(packageName, activity);
        try {
            Intent intent = new Intent();
            intent.setComponent(componentName);
            if(CommUtils.canTurn(context, intent))
                context.startActivity(intent);
        } catch (Exception e) {
            LogUtil.INSTANCE.e(TAG, "没有找到应用程序:packageName:" + packageName + "  activity:" + activity);
        }
    }

    /**
     * 应用是否被安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstall(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            //            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 安装APP
     *
     * @param file
     */
    public static void install(Context context, File file) {
        LogUtil.INSTANCE.e(TAG, "install Apk:" + file.getName());
        Intent intent = getInstallIntent(file);
        if(CommUtils.canTurn(context, intent))
            context.startActivity(intent);
    }

    /**
     * 卸载APk
     *
     * @param context
     * @param packageName 包名
     */
    public static void uninstall(Context context, String packageName) {
        Uri    packageURI      = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(CommUtils.canTurn(context, uninstallIntent))
            context.startActivity(uninstallIntent);
    }

    /**
     * 获取安装应用的Intent
     *
     * @param file
     * @return
     */
    public static Intent getInstallIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取版本信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo pkg = null;
        if (context == null) {
            return null;
        }
        try {
            pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkg;
    }

    /**
     * 获取设备的显示属性
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取设备型号(Nexus5)
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机品牌
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取电话通讯管理（可以通过这个对象获取手机号码等）
     */
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取版本号
     */
    public static String getSystemVersion() {
        // 获取android版本号
        return Build.VERSION.RELEASE;
    }

    /**
     * 返回ApplicationInfo（可以通过这个读取meta-data等等）
     */
    public static ApplicationInfo getApplicationInfo(Context context) {
        if (context == null) {
            return null;
        }
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationInfo;
    }

    /**
     * 获取MetaData的Bundle
     */
    public static Bundle getMetaData(Context context) {
        ApplicationInfo applicationInfo = getApplicationInfo(context);
        if (applicationInfo == null) {
            return new Bundle();
        }
        return applicationInfo.metaData;
    }

    /**
     * 应用是否启动
     */
    public static boolean appIsRunning(Context context) {
        boolean                               isAppRunning = false;
        ActivityManager                       am           = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null)
            return false;
        List<ActivityManager.RunningTaskInfo> list         = am.getRunningTasks(100);
        String                                packageName  = getPackageInfo(context).packageName;
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                //find it, break
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 检查系统是否有这个Intent，在启动Intent的时候需要检查，因为启动一个没有的Intent程序会Crash
     */
    public static boolean isIntentSafe(Context context, Intent intent) {
        PackageManager    packageManager = context.getPackageManager();
        List<ResolveInfo> activities     = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }

    /**
     * 获取当前包的系统缓存目录
     * "/Android/data/" + mContext.getPackageName() + "/cache"
     */
    public static String getDiskCacheDir(Context context) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        File cacheDirPath = getExternalCacheDir(context);
        if (cacheDirPath == null) {
            cacheDirPath = context.getCacheDir();
        }
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !isExternalStorageRemovable() ? cacheDirPath.getPath() : context.getCacheDir().getPath();
    }

    /**
     * 获取当前包的外置路径
     * "/Android/data/" + mContext.getPackageName()
     */
    public static String getDiskPackage(Context context) {
        return new File(getDiskCacheDir(context)).getParent();
    }

    /**
     * Check if external storage is built-in or removable.
     *
     * @return True if external storage is removable (like an SD card), false
     * otherwise.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        return !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) || Environment.isExternalStorageRemovable();
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The mContext to use
     * @return The external cache dir
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            File cacheDir;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                // /sdcard/Android/data/<application package>/cache
                if (context == null) {
                    NullPointerException ex = new NullPointerException("mContext == null");
                    LogUtil.INSTANCE.e(TAG, ex+"");
                    throw ex;
                }
                cacheDir = context.getExternalCacheDir();
            } else {
                // /data/data/<application package>/cache
                cacheDir = context.getCacheDir();
            }
            return cacheDir;
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * Check how much usable space is available at a given path.
     *
     * @param path The path to check
     * @return The space available in bytes
     */
    public static long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * 获取系统分配最大内存
     * @param context
     * @return
     */
    public static int getAppMemory(Context context) {
        if (context != null) {
            ActivityManager manager = (ActivityManager)context.getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            return manager.getMemoryClass();
        }
        return -1;
    }

}
