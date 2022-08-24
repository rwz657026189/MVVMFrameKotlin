package com.rwz.lib_comm.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/11 0011.
 */

import android.content.Context;

/**
 * Created by Anthony on 2016/3/3.
 * Class Note:
 * abstract class/interface defined to load image
 * (Strategy Pattern used here)
 */
public interface BaseImageLoaderStrategy {

    void loadImage(Context ctx, ImageLoader img);
}
