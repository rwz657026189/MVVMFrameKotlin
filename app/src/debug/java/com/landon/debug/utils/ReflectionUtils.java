/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2021. All rights reserved.
 */

package com.landon.debug.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rwx989128
 * @since 2021-06-22
 */
public class ReflectionUtils {
    public static <T> T getObj(Object obj, String field) {
        return getObj(obj.getClass(), obj, field);
    }

    public static <T> T getObj(Class cls, Object obj, String field) {
        try {
            Field declaredField = cls.getDeclaredField(field);
            declaredField.setAccessible(true);
            return (T) declaredField.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setField(Object obj, String field, Object value) {
        try {
            Field declaredField = obj.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            declaredField.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> T performMethod(Object obj, String name, Object... args) {
        return performMethod(obj.getClass(), obj, name, args);
    }

    public static <T> T performMethod(Class cls, Object obj, String name, Object... args) {
        try {
            final Class[] parameterTypes;
            if (args != null) {
                parameterTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg == null) {
                        throw new NullPointerException("参数为null不能使用该方法");
                    }
                    parameterTypes[i] = arg.getClass();
                }
            } else {
                parameterTypes = new Class[0];
            }
            Method method = cls.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return (T) method.invoke(obj, args);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printAll(Class cls) {
        if (cls == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        Field[] fields = cls.getDeclaredFields();
        sb.append("Field:[");
        for (Field field : fields) {
            sb.append(field.getName()).append(",");
        }
        sb.append("]");
        Method[] methods = cls.getDeclaredMethods();
        sb.append("\nMethod:[");
        for (Method method : methods) {
            sb.append(method.getName()).append(",");
        }
        sb.append("]");
        Class[] classes = cls.getDeclaredClasses();
        sb.append("\nClass:[");
        for (Class aClass : classes) {
            sb.append(aClass.getName()).append(",");
        }
        sb.append("]");
        Log.d("ReflectionUtils", "printAll: " + sb);
    }

    /**
     * 将object转化为map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                map.put(field.getName(), e.getMessage());
            }
        }
        return map;
    }
}
