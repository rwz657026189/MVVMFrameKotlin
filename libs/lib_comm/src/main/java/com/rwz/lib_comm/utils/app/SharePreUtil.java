package com.rwz.lib_comm.utils.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.rwz.lib_comm.manager.ContextManager;

import java.lang.reflect.Type;

/**
 * 配置文件工具类
 *
 * @author Administrator
 */
public class SharePreUtil {

    /**
     * 删除键值对
     */
    public static void removeKey(String preName, Context context, String key) {
        if (context == null) {
            return;
        }
        SharedPreferences pre    = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void removeKey(String key) {
        Context application = ContextManager.context;
        removeKey(application.getPackageName(), application, key);

    }
        /**
         * 从配置文件读取字符串
         *
         * @param preName 配置文件名
         * @param context
         * @param key     字符串键值
         * @return 键值对应的字符串, 默认返回""
         */
    public static String getString(String preName, Context context, String key) {
        if (context != null) {
            SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
            return pre.getString(key, "");
        }
        return "";
    }

    public static String getString(String key) {
        Context application = ContextManager.context;
        return getString(application.getPackageName(), application, key);
    }

    /**
     * 从配置文件读取int数据
     *
     * @param preName 配置文件名
     * @param context
     * @param key     int的键值
     * @return 键值对应的int, 默认返回-1
     */
    public static int getInt(String preName, Context context, String key) {
        return getInt(preName, context, key, -1);
    }
    public static int getInt(String preName, Context context, String key , int DEF_VALUE) {
        if(context == null) return DEF_VALUE;
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        return pre.getInt(key, DEF_VALUE);
    }

    public static int getInt(String key) {
        Context application = ContextManager.context;
        return getInt(application.getPackageName(), application, key, -1);
    }

    public static int getInt(String key, int DEF_VALUE) {
        Context application = ContextManager.context;
        return getInt(application.getPackageName(), application, key, DEF_VALUE);
    }

    /**
     * 从配置文件读取Boolean值
     *
     * @return 如果没有，默认返回false
     */
    public static Boolean getBoolean(String preName, Context context, String key) {
        if(context == null) return false;
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        return pre.getBoolean(key, false);
    }
    public static Boolean getBoolean(String preName, Context context, String key , boolean DEF_VALUE) {
        if(context == null) return DEF_VALUE;
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        return pre.getBoolean(key, DEF_VALUE);
    }
    public static Boolean getBoolean(String key, boolean DEFVALUE) {
        Context application = ContextManager.context;
        return getBoolean(application.getPackageName(), application, key, DEFVALUE);
    }

    /**
     * 从配置文件获取float数据
     *
     * @return 默认返回0.0f
     */
    public static float getFloat(String preName, Context context, String key) {
        if(context == null) return 0.0f;
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        return pre.getFloat(key, 0.0f);
    }
    public static float getFloat(String key) {
        Context application = ContextManager.context;
        return getFloat(application.getPackageName(), application, key);
    }
    /**
     * 从配置文件获取对象
     */
    public static <T> T getObject(String preName, Context context, String key, Class<T> clazz) {
        if(context == null) return null;
        SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        String str = pre.getString(key, "");
        return TextUtils.isEmpty(str) ? null : new Gson().fromJson(str, clazz);
    }
    public static <T> T getObject(String key, Class<T> clazz) {
        Context application = ContextManager.context;
        return getObject(application.getPackageName(), application, key,clazz);
    }

    /**
     * 存储字符串到配置文件
     *
     * @param preName 配置文件名
     * @param context
     * @param key     存储的键值
     * @param value   需要存储的字符串
     * @return 成功标志
     */
    public static Boolean putString(String preName, Context context, String key, String value) {
        if(context == null) return false;
        SharedPreferences pre    = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static Boolean putString(String key, String value) {
        Context application = ContextManager.context;
        return putString(application.getPackageName(), application, key, value);
    }

    /**
     * 保存Float数据到配置文件
     */
    public static Boolean putFloat(String preName, Context context, String key, float value) {
        if(context == null) return false;
        SharedPreferences pre    = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static Boolean putFloat(String key, float value) {
        Context application = ContextManager.context;
        return putFloat(application.getPackageName(), application, key, value);
    }

    /**
     * 存储数字到配置文件
     *
     * @param preName 配置文件名
     * @param context
     * @param key     存储的键值
     * @param value   需要存储的数字
     * @return 成功标志
     */
    public static Boolean putInt(String preName, Context context, String key, int value) {
        if(context == null) return false;
        SharedPreferences pre    = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static Boolean putInt(String key, int value) {
        Context application = ContextManager.context;
        return putInt(application.getPackageName(), application, key, value);
    }

    /**
     * 存储Boolean值到配置文件
     *
     * @param preName 配置文件名
     * @param context
     * @param key     键值
     * @param value   需要存储的boolean值
     * @return
     */
    public static Boolean putBoolean(String preName, Context context, String key, Boolean value) {
        if(context == null) return false;
        SharedPreferences pre    = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static Boolean putBoolean(String key, Boolean value) {
        Context application = ContextManager.context;
        return putBoolean(application.getPackageName(), application, key, value);
    }

    /**
     * 存放对象
     */
    public static Boolean putObject(String preName, Context context, String key, Type clazz, Object obj) {
        if(context == null) return false;
        String str    = new Gson().toJson(obj, clazz);
        SharedPreferences pre    = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        Editor editor = pre.edit();
        editor.putString(key, str);
        return editor.commit();
    }

    public static Boolean putObject(String key, Type clazz, Object obj) {
        Context application = ContextManager.context;
        return putObject(application.getPackageName(), application, key, clazz, obj);
    }

}
