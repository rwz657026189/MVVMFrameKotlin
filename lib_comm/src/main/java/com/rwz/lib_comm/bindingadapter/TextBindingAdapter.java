package com.rwz.lib_comm.bindingadapter;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

/**
 * Created by rwz on 2018/7/27.
 */

public class TextBindingAdapter {

    @BindingAdapter("android:drawableLeft")
    public static void setDrawableLeft(TextView view, int resId) {
        view.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    @BindingAdapter("android:drawableRight")
    public static void setDrawableRight(TextView view, int resId) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
    }

    @BindingAdapter("android:drawableTop")
    public static void setDrawableTop(TextView view, int resId) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
    }

    @BindingAdapter("android:drawableBottom")
    public static void setDrawableBottom(TextView view, int resId) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, resId);
    }

}
