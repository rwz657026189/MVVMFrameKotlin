package com.rwz.lib_comm.utils.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by 赵晨璞
 */
public class StatusBarUtil {

    /**
     * 修改状态栏为全透明
     * @param activity
     */
    public static void transparencyBar(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window =activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void transparencyBar(Window window){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置深色状态栏文字
     * @param activity
     */
    public static void setDarkStatusBar(Activity activity, boolean isFullScreen, boolean isDarkStatus) {
        if (activity != null) {
            int type = StatusBarLightMode(activity);
            StatusBarLightMode(activity, type, isFullScreen, isDarkStatus);
        }
    }

    /**
     *状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity){
        int result = 0;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        }
        if(MIUISetStatusBarLightMode(activity, true)){
            result = 1;
        }else if(FlymeSetStatusBarLightMode(activity.getWindow(), true)){
            result = 2;
        }else*/
        //小米note系列 会出现bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            result = 3;
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity
     * @param type 1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Activity activity, int type, boolean isFullScreen, boolean isDarkStatus){
        switch (type) {
            case 1:
//            MIUISetStatusBarLightMode(activity, isDarkStatus);
                break;
            case 2:
//            FlymeSetStatusBarLightMode(activity.getWindow(), isDarkStatus);
                break;
            case 3:
                if (isFullScreen) {
                    int visibility = activity.getWindow().getDecorView().getSystemUiVisibility();
                    int visible = isDarkStatus ? (visibility | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) :
                            ((visibility | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    activity.getWindow().getDecorView().setSystemUiVisibility(visible);
                } else {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                break;
        }

    }

    /**
     * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标
     */
    public static void StatusBarDarkMode(Activity activity, int type){
        switch (type) {
            case 1:
                MIUISetStatusBarLightMode(activity, false);
                break;
            case 2:
                FlymeSetStatusBarLightMode(activity.getWindow(), false);
                break;
            case 3:
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                break;
        }

    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     * @param activity
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window=activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if(dark){
                        activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }catch (Exception e){
            }
        }
        return result;
    }

    /**
     * 隐藏菜单。沉浸式阅读
     */
    public static void hideSystemUI(Window window) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );*/
    }

    /**
     * 设置状态栏的显示/隐藏
     * @param window
     * @param enable
     */
    public static void fullscreen(Window window, boolean enable) {
        if (enable) { //显示状态栏
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else { //隐藏状态栏
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(lp);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

}
