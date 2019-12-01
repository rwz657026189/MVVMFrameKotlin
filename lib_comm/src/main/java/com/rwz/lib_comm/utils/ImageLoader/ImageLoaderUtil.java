package com.rwz.lib_comm.utils.ImageLoader;

/**
 * Created by rwz on 2017/3/11 0011.
 */

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rwz.lib_comm.R;

/**
 * Created by Anthony on 2016/3/3.
 * Class Note:
 * use this class to load image,single instance
 */
public class ImageLoaderUtil {

    public static final int PIC_LARGE = 0;
    public static final int PIC_MEDIUM = 1;
    public static final int PIC_SMALL = 2;

    public static final int LOAD_STRATEGY_NORMAL = 0;
    public static final int LOAD_STRATEGY_ONLY_WIFI = 1;

    private static ImageLoaderUtil mInstance;
    private BaseImageLoaderStrategy mStrategy;

    private ImageLoaderUtil(){
        mStrategy =new GlideImageLoaderProvider();
    }

    //single instance
    public static ImageLoaderUtil getInstance(){
        if(mInstance ==null){
            synchronized (ImageLoaderUtil.class){
                if(mInstance == null){
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }


    public void loadImage(Context context, ImageLoader img){
        mStrategy.loadImage(context,img);
    }

    public void loadImage(ImageView img, String imgUrl){
        mStrategy.loadImage(img.getContext(),new ImageLoader.Builder().imgView(img).url(imgUrl).build());
    }

    public void loadLastImage(ImageView img, String imgUrl) {
        Glide.with(img.getContext()).load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(img.getDrawable())
                .error(R.drawable.shape_loading)
                .fitCenter()
                .into(img);
    }

    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy){
        mStrategy =strategy;
    }

    public void cleanGlide(Context context) {
        if (context != null) {
            Glide.get(context).clearMemory();
        }
    }
}