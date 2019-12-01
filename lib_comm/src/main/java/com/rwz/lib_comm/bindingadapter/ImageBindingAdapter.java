package com.rwz.lib_comm.bindingadapter;


import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.databinding.BindingAdapter;

import com.rwz.lib_comm.utils.ImageLoader.ImageLoader;
import com.rwz.lib_comm.utils.ImageLoader.ImageLoaderUtil;

/**
 * Created by rwz on 2017/3/14.
 */

public final class ImageBindingAdapter {

    @BindingAdapter(value = {"url","scale","isCenterCrop","viewWidth","placeHolder","errorDrawable","circle",
            "rounded","blur","square","crossFade", "imgRes"}, requireAll=false)
    public static void setImage(ImageView img, String url, float scale, boolean isCenterCrop, int width,
                                @DrawableRes int placeHolder,
                                @DrawableRes int errorDrawable, boolean circle, float rounded, int blur,
                                boolean square,
                                int crossFade, int imgRes) {
       /* if (GlobalConfig.isDebug && TextUtils.isEmpty(url)) {
            url = "http://pic1.win4000.com/tj/2017-12-07/5a28b54584b99.jpg";
        }*/
        ViewBindingAdapter.setViewScaleAndWidth(img,scale,width);
        ImageLoaderUtil.getInstance().loadImage(img.getContext(),new ImageLoader.Builder()
                .placeHolder(placeHolder).setErrorDrawable(errorDrawable).setCrossFade(crossFade)
                .setBlur(blur).setSquare(square).setRounded((int) rounded).setCircle(circle)
                .imgView(img).isCenterCrop(isCenterCrop).url(url).setImgRes(imgRes).build());
    }

    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        if(resource != 0)
            imageView.setImageResource(resource);
    }

}
