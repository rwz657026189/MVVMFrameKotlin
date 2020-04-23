package com.rwz.lib_comm.utils.app;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.rwz.lib_comm.manager.ContextManager;

/**
 * Created by rwz on 2017/3/9.
 * @function 资源获取工具类
 */

public class ResourceUtil {

    private ResourceUtil() {
        throw new RuntimeException("不能被实例化");
    }

    /**
     * 获取字符串
     * @param stringRes 资源id
     * @param formatArgs 格式化参数
     * @return
     */
    @NonNull
    public static String getString(@StringRes int stringRes, Object... formatArgs) {
        if (formatArgs == null || formatArgs.length == 0) {
            return stringRes == 0 ? "" : ContextManager.context.getString(stringRes);
        } else {
            return stringRes == 0 ? "" : ContextManager.context.getString(stringRes, formatArgs);
        }
    }

    /**
     * 获取颜色值
     * @param colorRes
     * @return
     */
    public static int getColor(@ColorRes int colorRes) {
        return  colorRes == 0 ? Color.WHITE : ContextCompat.getColor(ContextManager.context, colorRes);
    }
    /**
     * 获取尺寸值
     * @param dimenRes
     * @return
     */
    public static int getDimen(@DimenRes int dimenRes) {
        return dimenRes == 0 ? 0 : (int) ContextManager.context.getResources().getDimension(dimenRes);
    }

    /**
     * 获取数值
     * @param value
     * @return
     */
    public static int getInteger(@IntegerRes int value) {
        return value == 0 ? 0 : ContextManager.context.getResources().getInteger(value);
    }

    /**
     * 获取数组
     * @param intArr
     * @return
     */
    public static int[] getIntArr(@ArrayRes int intArr) {
        return intArr == 0 ? null : ContextManager.context.getResources().getIntArray(intArr);
    }
    /**
     * 获取数组
     * @param stringArr
     * @return
     */
    public static String[] getStringArr(@ArrayRes int stringArr) {
        return stringArr == 0 ? null : ContextManager.context.getResources().getStringArray(stringArr);
    }

    public static Drawable getDrawable(@DrawableRes int drawableRes) {
        return drawableRes == 0 ? null : ContextCompat.getDrawable(ContextManager.context, drawableRes);
    }

    public static Drawable getDrawable(@DrawableRes int unCheckDrawableRes,@DrawableRes int checkDrawableRes, boolean isChecked) {
        return getDrawable(isChecked ? checkDrawableRes : unCheckDrawableRes);
    }

    /**
     * 根据资源文件获取其尺寸
     * @param drawableRes
     * @return
     */
    public static int[] getImgResSize(@DrawableRes int drawableRes) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(ContextManager.context.getResources(), drawableRes, opts);
        return new int[]{opts.outWidth, opts.outHeight};
    }



}
