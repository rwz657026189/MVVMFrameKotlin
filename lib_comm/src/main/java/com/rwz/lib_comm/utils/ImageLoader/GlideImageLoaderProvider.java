package com.rwz.lib_comm.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/11 0011.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.target.Target;
import com.rwz.lib_comm.utils.app.NetUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * Created by Anthony on 2016/3/3.
 * Class Note:
 * provide way to load image
 */
public class GlideImageLoaderProvider implements BaseImageLoaderStrategy {

    @Override
    public void loadImage(Context ctx, ImageLoader img) {
        int strategy =img.getWifiStrategy();
//        LogUtil.d("strategy",strategy);
        //如果是在wifi,或者采取的策略不只wifi加载就加载图片
        if (NetUtils.isWifi() || strategy != ImageLoaderUtil.LOAD_STRATEGY_ONLY_WIFI) {
            loadNormal(ctx, img);
        }else{
            loadCache(ctx, img);
        }
    }


    /**
     * load image with Glide
     */
    private void loadNormal(Context ctx, final ImageLoader imageLoader) {
//        imageLoader.getImgView().setTag(null);
//        LogUtil.d("GlideImageLoaderProvider,loadNormal", "imageLoader = " + imageLoader);
        String url = imageLoader.getUrl();
        ImageView imgView = imageLoader.getImgView();
        DrawableTypeRequest load;
        if(!TextUtils.isEmpty(url))
            load = Glide.with(ctx).load(url);
        else if(imageLoader.getImgRes() != 0)
            load = Glide.with(ctx).load(imageLoader.getImgRes());
        else load = Glide.with(ctx).load(url);

        DrawableRequestBuilder builder =
                load.diskCacheStrategy(imageLoader.getCacheStrategy() == ImageLoader.CACHE_NORMAL ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                        .skipMemoryCache(imageLoader.getCacheStrategy() == ImageLoader.CACHE_NONE);
        //占位图
        if (imageLoader.getPlaceHolderDrawable() != null) {
            builder.placeholder(imageLoader.getPlaceHolderDrawable());
        } else if (imageLoader.getPlaceHolder() == ImageLoader.PLACE_HOLDER_PREV) { //采用上一张图片作为占位图
            Drawable drawable = imgView.getDrawable();
            builder.placeholder(drawable);
        } else if (imageLoader.getPlaceHolder() != ImageLoader.PLACE_HOLDER_NONE) {
            builder.placeholder(imageLoader.getPlaceHolder());
        }
        //错误图
        if (imageLoader.getErrorDrawable() != ImageLoader.PLACE_HOLDER_NONE) {
            builder.error(imageLoader.getErrorDrawable());
            builder.fallback(imageLoader.getErrorDrawable());
        }
        int width = imageLoader.getWidth();
        int height = imageLoader.getHeight();
        if (isValidDimensions(width) && isValidDimensions(height)) {
            if (width == ImageLoader.ORIGINAL_SIZE) {
                width = Target.SIZE_ORIGINAL;
            }
            if (height == ImageLoader.ORIGINAL_SIZE) {
                height = Target.SIZE_ORIGINAL;
            }
            builder.override(width, height);
        }
        //设置动画
        if (imageLoader.getCrossFade() > 0) {
            builder.crossFade(imageLoader.getCrossFade());//渐显动画
        } else {
            builder.dontAnimate(); //不设置动画效果
        }
        List<Transformation<Bitmap>> list = new ArrayList<>();
        //设置裁剪模式
        if (imageLoader.isCenterCrop()) {
            list.add(new CenterCrop(ctx));
        } else {
            list.add(new FitCenter(ctx));
        }
        //模糊
        int blur = imageLoader.getBlur();
        if (blur > 0) {
            //将图片缩小8倍(经验值)后模糊，效果更佳
            list.add(new BlurTransformation(ctx, blur, blur * 8 / ImageLoader.MAX_BLUR));
        }
        //圆角
        if (imageLoader.getRounded() > 0) {
            list.add(new RoundedCornersTransformation(ctx, imageLoader.getRounded(), 0));
        }
        //圆形
        if (imageLoader.isCircle()) {
            list.add(new CropCircleTransformation(ctx));
        }
        //方形
        if (imageLoader.getSquare()) {
            list.add(new CropSquareTransformation(ctx));
        }
        builder.bitmapTransform(new MultiTransformation<>(list));
        builder.into(imgView);

//        LogUtil.d("imageLoader = " + imageLoader);

    }

    private boolean isValidDimensions(int size) {
        return size == ImageLoader.ORIGINAL_SIZE || size > 0;
    }

    /**
     *load cache image with Glide
     */
    private void loadCache(Context ctx, ImageLoader img) {
        Glide.with(ctx).using(new StreamModelLoader<String>() {
            @Override
            public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
                return new DataFetcher<InputStream>() {
                    @Override
                    public InputStream loadData(Priority priority) throws Exception {
                        throw new IOException();
                    }

                    @Override
                    public void cleanup() {

                    }

                    @Override
                    public String getId() {
                        return model;
                    }

                    @Override
                    public void cancel() {

                    }
                };
            }
        })
                .load(img.getUrl())
                .placeholder(img.getPlaceHolder())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img.getImgView());
    }
}
