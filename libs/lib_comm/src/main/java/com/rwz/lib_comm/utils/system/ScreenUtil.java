package com.rwz.lib_comm.utils.system;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rwz.lib_comm.utils.show.LogUtil;

/**
 * Created by AriaLyy on 2015/1/22.
 * 屏幕工具
 */
public class ScreenUtil {

    private volatile static ScreenUtil mUtil = null;
    private static final    Object     LOCK  = new Object();

    private ScreenUtil() {
    }

    public static ScreenUtil getInstance() {
        if (mUtil == null) {
            synchronized (LOCK) {
                if (mUtil == null) {
                    mUtil = new ScreenUtil();
                }
            }
        }
        return mUtil;
    }

    /**
     * 设置灰度
     *
     * @param v
     * @param greyScale true:灰度
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setGreyScale(View v, boolean greyScale) {
        if (greyScale) {
            // Create a paint object with 0 saturation (black and white)
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            Paint greyScalePaint = new Paint();
            greyScalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
            // Create a hardware layer with the greyScale paint
            v.setLayerType(View.LAYER_TYPE_HARDWARE, greyScalePaint);
        } else {
            // Remove the hardware layer
            v.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }

    /**
     * 获得屏幕高度
     */
    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if(wm == null)
            return 0;
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if(wm == null)
            return 0;
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     */
    public int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz  = Class.forName("com.android.internal.R$dimen");
            Object   object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @return error return null
     */
    public Bitmap snapShotWithStatusBar(Activity activity) {
        final View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        int width  = getScreenWidth(activity);
        int height = getScreenHeight(activity);
//        view.layout(0, 0, width, height);

        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp   = view.getDrawingCache();
        Rect   frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int    width  = getScreenWidth(activity);
        int    height = getScreenHeight(activity);
        Bitmap bp     = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public Bitmap snapView(View view) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        // android8之后bitmap内存分配到native heap
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Runtime runtime = Runtime.getRuntime();
            long remainMemory = runtime.maxMemory() - runtime.totalMemory();
            int requireMemory = width * height * 2;
            if (requireMemory > remainMemory) {
                LogUtil.INSTANCE.e("ScreenUtil snapView: 剩余内存不足, remainMemory = " + remainMemory,
                        "requireMemory = " + requireMemory);
                return null;
            }
        }
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 判断是否开启了自动亮度调节
     */
    public boolean isAutoBrightness(ContentResolver aContentResolver) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    /**
     * @return 获取屏幕的亮度( 0 ~ 255)
     */
    public int getScreenBrightness(Activity activity) {
        int             nowBrightnessValue = 0;
        ContentResolver resolver           = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 设置亮度(手动设置亮度，需要关闭自动设置亮度的开关)
     *
     * @param brightness 0 ~ 1f
     */
    public void setBrightness(Activity activity, float brightness) {
        stopAutoBrightness(activity);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = brightness;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 停止自动亮度调节
     */
    public void stopAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * 开启亮度自动调节
     */
    public void startAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * 保存亮度设置状态
     */
    public void saveBrightness(ContentResolver resolver, int brightness) {
        Uri uri = Settings.System
                .getUriFor("screen_brightness");
        Settings.System.putInt(resolver, "screen_brightness",
                brightness);
        // resolver.registerContentObserver(uri, true, myContentObserver);
        resolver.notifyChange(uri, null);
    }

    /**
     * 设置透明状态栏
     * @param aty
     * @param on
     */
    public static void setTranslucentStatus(Activity aty, boolean on) {
        if (aty == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window win = aty.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    public static void setTranslucentStatus(Dialog dialog, boolean on) {
        if (dialog == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window win = dialog.getWindow();
            if(win ==  null)
                return;
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    public static void setTranslucentNavigation(Activity aty, boolean on) {
        if (aty == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = aty.getWindow();
            if (on) {
                win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

    }

    public static void setWindowManagerFullScreen(Activity aty, boolean on) {
        if (aty == null) {
            return;
        }
        if (on) {
            //隐藏顶部状态栏
            aty.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            aty.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void setWindowManagerFullScreen(Dialog dialog, boolean on) {
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        if(window == null)
            return;
        if (on) {
            //隐藏顶部状态栏
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

}
