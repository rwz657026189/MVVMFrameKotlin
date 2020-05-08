package com.rwz.lib_comm.utils.compress;

import android.graphics.Bitmap;

public class CompressConfig {

    private String filePath;    //目标文件路径
    private String savePath;    //是否保存到文件
    private int destWidth;      //目标宽，支持单边，即仅宽/高(根据原图比例缩放)
    private int destHeight;     //目标高，支持单边，即仅宽/高(根据原图比例缩放)
    private int minWidth;
    private int minHeight;
    private int size;//kb，决定是否质量压缩
    private Strategy strategy;
    private Bitmap.CompressFormat format;

    public enum Strategy {
        CONSTANT,   //指定大小，需要设置destWidth、destHeight(可能裁剪，缩放)
        MIN         //压缩到最接近destWidth、destHeight 但不小于minWidth、minHeight的尺寸(不会裁剪)
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSavePath() {
        return savePath;
    }

    public int getDestWidth() {
        return destWidth;
    }

    public int getDestHeight() {
        return destHeight;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public Bitmap.CompressFormat getFormat() {
        return format;
    }

    public int getSize() {
        return size;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    private CompressConfig() {
    }

    public static final class Build{

        private CompressConfig config;

        public Build() {
            config = new CompressConfig();
        }

        public Build setFilePath(String filePath) {
            config.filePath = filePath;
            return this;
        }

        public Build setSavePath(String savePath) {
            config.savePath = savePath;
            return this;
        }

        public Build setDestWidth(int destWidth) {
            config.destWidth = destWidth;
            return this;
        }

        public Build setDestHeight(int destHeight) {
            config.destHeight = destHeight;
            return this;
        }

        public Build setMinWidth(int minWidth) {
            config.minWidth = minWidth;
            return this;
        }

        public Build setMinHeight(int minHeight) {
            config.minHeight = minHeight;
            return this;
        }

        public Build setFormat(Bitmap.CompressFormat format) {
            config.format = format;
            return this;
        }

        public Build setSize(int size) {
            config.size = size;
            return this;
        }

        public Build setStrategy(Strategy strategy) {
            config.strategy = strategy;
            return this;
        }

        public CompressConfig create(String filePath) {
            config.filePath = filePath;
            return config;
        }

    }

}
