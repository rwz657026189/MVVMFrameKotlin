package com.landon.debug;

/**
 * @Author Ren Wenzhang
 * @Date 2022/6/24/024 19:05
 * @Description
 */
public final class DebugConfig {
    // 接口加载耗时监听
    public static boolean NET_TIME = false;
    // 日志打印
    public static boolean NET_LOGCAT = true;
    // 数据mock
    public static boolean NET_MOCK = true;
    // 接口延迟加载
    public static boolean NET_TIMEOUT = true;
    // 本地ip
    public static boolean NET_LOCAL_IP = true;
    // 重置url
    public static boolean NET_CHANGE_URL = true;

    // 是否支持截屏
    public static boolean SUPPORT_SCREENSHOT = true;
    // 是否支持打印参数
    public static boolean SUPPORT_ACTIVITY_ARGS = true;
    // 是否支持Activity注入
    public static boolean SUPPORT_ACTIVITY_INJECT = true;
    // 是否支持打印Fragment
    public static boolean SUPPORT_FRAGMENT = true;
    // 是否支持打印异常修复的参数
    public static boolean SUPPORT_SAVED_INSTANCE_STATE = true;
}
