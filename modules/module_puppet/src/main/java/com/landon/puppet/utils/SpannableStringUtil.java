package com.landon.puppet.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

/**
 * Created by Administrator on 2017/2/7.
 */

public class SpannableStringUtil extends SpannableString {

    private String text;

    public SpannableStringUtil(CharSequence source) {
        super(source);
        this.text = source+"";
    }

    public static SpannableStringUtil getInstance(String text) {
        return new SpannableStringUtil(text);
    }

    /**
     *   Spanned.SPAN_EXCLUSIVE_EXCLUSIVE —— (a,b)
     Spanned.SPAN_EXCLUSIVE_INCLUSIVE —— (a,b]
     Spanned.SPAN_INCLUSIVE_EXCLUSIVE —— [a,b)
     Spanned.SPAN_INCLUSIVE_INCLUSIVE —— [a,b]
     * @param context
     * @param idStyle
     * @param start
     * @param end
     * @return
     * @function 添加字体样式
     */
    public SpannableStringUtil addTextAppearanceSpan(Context context, @StyleRes int idStyle , int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            setSpan(new TextAppearanceSpan(context, idStyle), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /**
     * @function 添加字体相对大小
     * @param relativeSize
     * @param start
     * @param end
     * @return
     */
    public SpannableStringUtil addRelativeSizeSpan(float relativeSize, int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            setSpan(new RelativeSizeSpan(relativeSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    /***
     * @function 添加字体颜色
     * @param color
     * @param start
     * @param end
     * @return
     */
    public SpannableStringUtil addForegroundColorSpan(@ColorInt int color, int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            setSpan(new ForegroundColorSpan(color),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    public SpannableStringUtil addForegroundColorSpan(@ColorInt int color, String targetStr) {
        if (!TextUtils.isEmpty(targetStr) && !TextUtils.isEmpty(text)) {
            int index = text.indexOf(targetStr);
            if (index >= 0) {
                return addForegroundColorSpan(color, index, index + targetStr.length());
            }
        }
        return this;
    }

    public SpannableStringUtil addImageSpan(Context context, @DrawableRes int drawableId, int start , int end) {
        if (!TextUtils.isEmpty(text) && start >= 0 && end <= text.length()) {
            ImageSpan span = new ImageSpan(context, BitmapFactory.decodeResource(context.getResources(), drawableId));
            setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }
}
