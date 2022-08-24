package com.rwz.lib_comm.utils.system;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;


import com.rwz.lib_comm.utils.show.LogUtil;

import java.util.Collection;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by rwz on 2016/10/20.
 */
public class StringHelp {

    private static final String DEF_STR = "0";

    public static void judgeNull(@Nullable TextView tv , String str, String defStr) {
        if (tv != null) {
            tv.setText(TextUtils.isEmpty(str) ? defStr : str);
        } else {
            LogUtil.INSTANCE.d("tag","------------>> TextView is NULL <<----------------" );
        }
    }
    public static void judgeNull(@Nullable TextView tv , String str) {
        judgeNull(tv,str,DEF_STR);
    }

    public static void judgeNull(@Nullable TextView tv , String str, String nullStr, boolean isHintIfNull) {
        if (tv != null) {
            if (TextUtils.isEmpty(str) || str.equals(nullStr)) {
                if (isHintIfNull) {
                    tv.setVisibility(View.GONE);
                }else{
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(DEF_STR);
                }
            }else{
                tv.setText(str);
                tv.setVisibility(View.VISIBLE);
            }
        } else {
            LogUtil.INSTANCE.d("tag","------------>> TextView is NULL <<----------------" );
        }

    }
    public static void judgeNull(@Nullable TextView tv , String str, boolean isHintIfNull) {
        if (tv != null) {
            if (TextUtils.isEmpty(str)) {
                if (isHintIfNull) {
                    tv.setVisibility(View.GONE);
                }else{
                    tv.setText(DEF_STR);
                    tv.setVisibility(View.VISIBLE);
                }
            }else{
                tv.setText(str);
                tv.setVisibility(View.VISIBLE);
            }
        } else {
            LogUtil.INSTANCE.d("tag","------------>> TextView is NULL <<----------------" );
        }

    }

    public static String judgeNull(String str, String defStr) {
        return TextUtils.isEmpty(str) ? defStr : str;
    }

    public static String judgeNull(String str) {
        return judgeNull(str,"0");
    }


    //处理点赞等
    public static void addOne(TextView tv, String numStr) {
        if (tv != null) {
            tv.setText(addOne(numStr));
        }
    }
    //处理点赞等
    public static String addOne(String numStr) {
        LogUtil.INSTANCE.d("tag","--numStr------>> " + numStr);
        if (!TextUtils.isEmpty(numStr)) {
            if (!numStr.contains("万")) {
                try {
                    int number = Integer.parseInt(numStr);
                    return number + 1 +"";
                } catch (Exception e) {
                    e.printStackTrace();
                    return numStr;
                }
            }else{
                return numStr;
            }
        }else{
            return "1";
        }
    }
    //处理点赞等
    public static String removeOne(String numStr) {
        LogUtil.INSTANCE.d("tag","--numStr------>> " + numStr);
        if (!TextUtils.isEmpty(numStr)) {
            if (!numStr.contains("万")) {
                try {
                    int number = Integer.parseInt(numStr);
                    if (number > 0) {
                        return number - 1 +"";
                    }else {
                        return "0";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return numStr;
                }
            }else{
                return numStr;
            }
        }else{
            return "1";
        }
    }



    /**
     * 捕获异常的字符串转整型
     * @param numStr
     * @return
     */
    public static int parseInt(String numStr, int defValue) {
        try {
            if (TextUtils.isEmpty(numStr)) {
                return defValue;
            }
            return Integer.parseInt(numStr.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public static long parseLong(String numStr) {
        return parseLong(numStr, 0L);
    }
    public static long parseLong(String numStr, long defValue) {
        try {
            if (TextUtils.isEmpty(numStr)) {
                return defValue;
            }
            return Long.parseLong(numStr.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public static Double parseDouble(String numStr) {
        return parseDouble(numStr, 0.0d);
    }
    public static Double parseDouble(String numStr, Double defValue) {
        try {
            if (TextUtils.isEmpty(numStr)) {
                return defValue;
            }
            return Double.parseDouble(numStr.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public static Float parseFloat(String numStr, Float defValue) {
        try {
            if (TextUtils.isEmpty(numStr)) {
                return defValue;
            }
            return Float.parseFloat(numStr.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String formatTime(String secondStr) {
        try {
            int time = Integer.parseInt(secondStr);
            return stringForTime(time * 1000);
        } catch (Exception e) {
            LogUtil.INSTANCE.d("tag","--Exception------->> " + e);
            return "00:00";
        }
    }

    private static String handleTime(int time, boolean isLast) {
        if (isLast) {
            return time > 0 ? (time > 10 ? time + "" : "0" + time + "") : "00";
        }
        return time > 0 ? (time > 10 ? time + ":" : "0" + time + ":") : "00:";
    }

    public static void setFormatStr(@Nullable TextView tv, @StringRes int idRes, Object... args) {
        if (tv != null) {
            tv.setText(String.format(tv.getContext().getString(idRes),args));
        }
    }

    /** 取出集合的元素转化为字符串，并以","隔开 ***/
    public static String getStringFromCollection(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            LogUtil.INSTANCE.e("collection isEmpty ", true);
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object next : collection) {
            sb.append(next.toString()).append(",");
        }
        if (sb.length() > 0) {
            String result = sb.toString();
            return result.substring(0, result.length() - 1);
        }
        return "";
    }


}
