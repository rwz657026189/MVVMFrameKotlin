package com.rwz.lib_comm.utils.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.rwz.lib_comm.utils.show.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SizeCompress {

    /**
     * 压缩图片
     */
    public Bitmap compress(CompressConfig config) {
        BufferedInputStream bis = null;
        try {
            final File file = new File(config.getFilePath());
            bis = new BufferedInputStream(new FileInputStream(file));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bis, null, options);
            int outWidth = options.outWidth;
            int outHeight = options.outHeight;
            if (outHeight == 0 || outWidth == 0) {
                return null;
            }
            bis.close();
            bis = new BufferedInputStream(new FileInputStream(file));
            int destWidth = config.getDestWidth();
            int destHeight = config.getDestHeight();
            if (destWidth == 0) {
                destWidth = outWidth * destHeight / outHeight;
            }
            if (destHeight == 0) {
                destHeight = outHeight * destWidth / outWidth;
            }
            options.inSampleSize = calcInSampleSize(outWidth, outHeight, destWidth, destHeight, config.getMinWidth(),
                    config.getMinHeight(), config.getStrategy() != CompressConfig.Strategy.MIN);
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeStream(bis, null, options);
            if (bitmap != null) {
                LogUtil.INSTANCE.d("SizeCompress" + " compress(): " + "[" + outWidth + "," + outHeight + "]/[" + destWidth + "," +
                        destHeight + "] => [" + bitmap.getWidth() + "," + bitmap.getHeight() + "], inSampleSize = " + options.inSampleSize);
            }
            if (config.getStrategy() == CompressConfig.Strategy.CONSTANT && destWidth > 0 && destHeight > 0) {
                return ThumbnailUtils.extractThumbnail(bitmap, destWidth, destHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            } else {
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 计算inSampleSize
     * @param width： 原始宽度
     * @param height： 原始高度
     * @param destWidth： 目标宽度
     * @param destHeight： 目标高度
     * @param minWidth: 最小宽度
     * @param minHeight：最小高度
     * @param isEnough : 根据缩放比例，如5，true：取较小值4， false:较大值8
     * @return 根据原始宽高，计算符合目标宽高的inSampleSize,且不会低于最小宽，高
     */
    private int calcInSampleSize(int width, int height, int destWidth, int destHeight, int minWidth, int minHeight, boolean isEnough) {
        if (width < destWidth || height < destHeight || height == 0) {
            return 1;
        }
        if (destHeight == 0 && destWidth == 0) {
            return 1;
        }
        //避免crash，取最小值可忽略
        if (destHeight == 0) {
            destHeight = 1;
        }
        if (destWidth == 0) {
            destWidth = 1;
        }
        int inSampleSizeForHeight = getValidInSampleSize((double)height / destHeight, isEnough);
        if (height / inSampleSizeForHeight < minHeight) {
            inSampleSizeForHeight = Math.min(getValidInSampleSize((double)height / minHeight, isEnough), isEnough ? inSampleSizeForHeight : (inSampleSizeForHeight / 2));
        }
        int inSampleSizeForWidth = getValidInSampleSize((double) width / destWidth, isEnough);
        if (width / inSampleSizeForWidth < minWidth) {
            inSampleSizeForWidth = Math.min(getValidInSampleSize((double)width / minWidth, isEnough), isEnough ? inSampleSizeForWidth : (inSampleSizeForWidth / 2));
        }
        return Math.min(inSampleSizeForHeight, inSampleSizeForWidth);
    }

    /**
     * @param isEnough true: 5 -> 4, false : 5 -> 8
     *                 true: 4 -> 4, false : 4 -> 4
     */
    private int getValidInSampleSize(double rate, boolean isEnough) {
        if (rate <= 1) {
            return 1;
        }
        double result = Math.log(rate) / Math.log(2d);
        int value = isEnough ? (int) Math.floor(result) : (int) Math.ceil(result);
        return (int) Math.pow(2, value);
    }

}
