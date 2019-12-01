package com.rwz.lib_comm.bindingadapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;

import com.rwz.lib_comm.entity.params.CommandEntity;
import com.rwz.lib_comm.ui.adapter.rv.mul.IBaseEntity;

import io.reactivex.functions.Consumer;

/**
 * Created by rwz on 2017/3/15.
 * 切记：BindingAdapter 给的标注与参数必须一一对应
 */

public class ViewBindingAdapter {
    /**
     * 给View设置显示比例
     * @param view
     * @param scale : 高宽比例
     */
    @BindingAdapter({"scale"})
    public static void setViewScale(final View view, final float scale) {
        if (scale > 0) {
        }
    }

    @BindingAdapter({"scale","viewWidth"})
    public static void setViewScaleAndWidth(final View view, final float scale, int width) {
        if (scale > 0) {
            final ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null && width > 0) {
                params.width = width;
                params.height = (int) (scale * width);
            }else{
                setViewScale(view,scale);
            }
        }
    }

    /**
     * 点击事件的监听,该名避免和系统的混淆
     * @param v
     * @param command
     */
    @BindingAdapter({"clickCommand","setEntity"})
    public static void onClick(View v , final Consumer<CommandEntity> command, final IBaseEntity entity) {
        if(command == null)
            return;
        final CommandEntity commandEntity = new CommandEntity(v.getId(), entity);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    command.accept(commandEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 点击事件的监听,该名避免和系统的混淆
     * @param v
     * @param command
     */
    @BindingAdapter({"clickCommand"})
    public static void onClick(View v , final Consumer<CommandEntity> command) {
        onClick(v, command, null);
    }

    /**
     * 设置控件宽高， 特别注意，为了支持宽高可选, 该值不能为0
     */
    @BindingAdapter("android:layout_width")
    public static void setViewWidth(View view, int width) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_height")
    public static void setViewHeight(View view, int height) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setTopMargin(View view, float topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.setMargins(layoutParams.leftMargin, (int) topMargin,
                    layoutParams.rightMargin,layoutParams.bottomMargin);
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_marginLeft")
    public static void setLeftMargin(View view, float leftMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.setMargins((int) leftMargin, layoutParams.topMargin,
                    layoutParams.rightMargin,layoutParams.bottomMargin);
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_marginRight")
    public static void setRightMargin(View view, float rightMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                    (int) rightMargin,layoutParams.bottomMargin);
            view.setLayoutParams(layoutParams);
        }
    }
    @BindingAdapter("android:layout_marginBottom")
    public static void setBottomMargin(View view, float bottomMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin,
                    layoutParams.rightMargin, (int) bottomMargin);
            view.setLayoutParams(layoutParams);
        }
    }

   /* @BindingAdapter("android:background")
    public static void setBottomMargin(View view, int color) {
        view.setBackgroundColor(color);
    }*/

}
