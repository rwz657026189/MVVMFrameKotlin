package com.rwz.lib_comm.bindingadapter;


import android.widget.SeekBar;

import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;

import com.rwz.lib_comm.utils.app.ResourceUtil;

/**
 * Created by rwz on 2018/8/23.
 */

public class ProgressBarBindingAdapter {

    @BindingAdapter({"android:thumb"})
    public static void setImageViewResource(SeekBar progressBar, @DrawableRes int resource) {
        if(resource != 0) //thumb
            progressBar.setThumb(ResourceUtil.getDrawable(resource));

    }

}
