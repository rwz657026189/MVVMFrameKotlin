package com.rwz.lib_comm.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/11 0011.
 */

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.rwz.lib_comm.R;


/**
 * Created by Anthony on 2016/3/3.
 * Class Note:
 * encapsulation of ImageView,Build Pattern used
 */
public class ImageLoader {

    public static final int ORIGINAL_SIZE = -1; //设置图片原始尺寸
    public static final int PLACE_HOLDER_NONE = -1; //无占位图
    public static final int ERROR_NONE = -1; //无占位图
    public static final int PLACE_HOLDER_PREV = -2; //采用前一张图片作为占位图(适合同一控件不断轮换图片)


    public static final int CACHE_NORMAL = 0;
    public static final int CACHE_NONE = 1;
    public static final int MAX_BLUR = 25; //最多模糊程度

    private int type;  //类型 (大图，中图，小图)
    private String url; //需要解析的url
    private int placeHolder; //当正在加载显示的图片
    private Drawable placeHolderDrawable;//当正在加载显示的图片
    private int errorDrawable; //当没有成功加载的时候显示的图片
    private ImageView imgView; //ImageView的实例
    private int wifiStrategy;//加载策略，是否在wifi下才加载
    private float scale; //图片比例
    private int cacheStrategy;//缓存策略
    private boolean isCenterCrop;//是否对图片缩放裁剪
    private boolean isCacheSize;//是否缓存图片尺寸,(先获取bitmap,在设置图片)
    private int width;  //需要显示的图片宽
    private int height; //需要显示的图片高
    private int crossFade;//渐显动画时长
    private int imgRes; //图片资源(url为空会用imgRes替代)

    private boolean circle;     //是否圆形
    private int rounded;        //是否圆角
    private int blur;           //模糊值
    private boolean square;     //是否方向

    private ImageLoader(Builder builder) {
        this.type = builder.type;
        this.url = builder.url;
        this.placeHolder = builder.placeHolder;
        this.imgView = builder.imgView;
        this.wifiStrategy = builder.wifiStrategy;
        this.scale = builder.scale;
        this.cacheStrategy = builder.cacheStrategy;
        this.isCenterCrop = builder.isCenterCrop;
        this.isCacheSize = builder.isCacheSize;
        this.width = builder.width;
        this.height = builder.height;
        this.errorDrawable = builder.errorDrawable;
        this.circle = builder.circle;
        this.blur = builder.blur;
        this.rounded = builder.rounded;
        this.square = builder.square;
        this.imgRes = builder.imgRes;
        this.crossFade = builder.crossFade;
        this.placeHolderDrawable = builder.placeHolderDrawable;
    }

    public int getCrossFade() {
        return crossFade;
    }

    public int getCacheStrategy() {
        return cacheStrategy;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public int getImgRes() {
        return imgRes;
    }

    public Drawable getPlaceHolderDrawable() {
        return placeHolderDrawable;
    }

    public int getErrorDrawable() {
        return errorDrawable;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public int getWifiStrategy() {
        return wifiStrategy;
    }

    public float getScale() {
        return scale;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public boolean isCacheSize() {
        return isCacheSize;
    }

    public boolean isCircle() {
        return circle;
    }

    public boolean getSquare() {
        return square;
    }

    public int getRounded() {
        return rounded;
    }

    public int getBlur() {
        return blur;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static class Builder {
        private int type;
        private String url;
        private int imgRes; //图片资源(url为空会用imgRes替代)
        private int placeHolder; //当正在加载显示的图片
        private int errorDrawable; //当没有成功加载的时候显示的图片
        private ImageView imgView;
        private int wifiStrategy;
        private float scale; //图片比例
        private int cacheStrategy;//缓存策略
        private boolean isCacheSize;//是否缓存图片尺寸,(先获取bitmap,在设置图片)
        private boolean isCenterCrop;
        private int width;
        private int height;
        private boolean circle;
        private int rounded;
        private int blur;
        private boolean square;
        private int crossFade = 200;//渐显动画时长
        private Drawable placeHolderDrawable;//当正在加载显示的图片

        public Builder() {
            this.type = ImageLoaderUtil.PIC_SMALL;
            this.url = "";
            this.placeHolder = R.drawable.shape_loading;
            this.errorDrawable = R.drawable.shape_loading;
            this.imgView = null;
            this.wifiStrategy = ImageLoaderUtil.LOAD_STRATEGY_NORMAL;
            this.scale = 3 / 4;
            cacheStrategy = CACHE_NORMAL;
        }


        public Builder setWidth(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder setImgRes(int imgRes) {
            this.imgRes = imgRes;
            return this;
        }

        public Builder setCrossFade(int crossFade) {
            this.crossFade = crossFade;
            return this;
        }

        public Builder isCacheSize(boolean cacheSize) {
            isCacheSize = cacheSize;
            return this;
        }

        public Builder isCenterCrop(boolean centerCrop) {
            isCenterCrop = centerCrop;
            return this;
        }

        public Builder placeHolder(int placeHolder) {
            if (placeHolder != 0) {
                this.placeHolder = placeHolder;
            }
            return this;
        }
        public Builder placeHolder(Drawable placeHolderDrawable) {
            this.placeHolderDrawable = placeHolderDrawable;
            return this;
        }

        public Builder setErrorDrawable(int errorDrawable) {
            if (errorDrawable != 0) {
                this.errorDrawable = errorDrawable;
            }
            return this;
        }

        public Builder imgView(ImageView imgView) {
            this.imgView = imgView;
            return this;
        }

        public Builder strategy(int strategy) {
            this.wifiStrategy = strategy;
            return this;
        }

        public Builder scale(int scale) {
            this.scale = scale;
            return this;
        }
        public Builder setCacheStrategy(int cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            return this;
        }

        public Builder setRounded(int rounded) {
            this.rounded = rounded;
            return this;
        }

        public Builder setBlur(int blur) {
            if(blur > MAX_BLUR)
                blur = MAX_BLUR;
            this.blur = blur;
            return this;
        }

        public Builder setCircle(boolean circle) {
            this.circle = circle;
            return this;
        }

        public Builder setSquare(boolean square) {
            this.square = square;
            return this;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }

    }

    @Override
    public String toString() {
        return "ImageLoader{" +
                "type=" + type +
                ", \n url='" + url + '\'' +
                ", \n placeHolder=" + placeHolder +
                ", \n errorDrawable=" + errorDrawable +
                ", \n imgView=" + imgView +
                ", \n wifiStrategy=" + wifiStrategy +
                ", \n scale=" + scale +
                ", \n cacheStrategy=" + cacheStrategy +
                ", \n isCenterCrop=" + isCenterCrop +
                ", \n isCacheSize=" + isCacheSize +
                ", \n width=" + width +
                ", \n height=" + height +
                ", \n crossFade=" + crossFade +
                ", \n circle=" + circle +
                ", \n placeHolderDrawable=" + placeHolderDrawable +
                ", \n rounded=" + rounded +
                ", \n blur=" + blur +
                ", \n square=" + square +
                ", \n imgRes=" + imgRes +
                '}';
    }
}
