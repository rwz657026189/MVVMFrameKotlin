package com.rwz.lib_comm.entity.extension.wrap;

import android.graphics.drawable.Drawable;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rwz.lib_comm.entity.response.BaseListEntity;

/**
 * Created by rwz on 2017/3/14.
 *
 * 封装一个对象,使其可以作为一个recyclerview的item
 *
 */

public class WrapData<E> extends BaseListEntity {

    @LayoutRes
    int mItemLayoutId;//作为recyclerview item的布局id,必要
    @LayoutRes
    int mChildItemLayoutId;//作为List item的布局id,非必要
    Drawable mBg;//背景图片
    E data;

    boolean hasInitData;//定义一个变量,避免重复设置数据
    /**
     * @param mItemLayoutId  item布局id
     * @param mChildItemLayoutId 子类item布局id 非必要,默认值为0
     * @param data
     */
    private WrapData(@LayoutRes int mItemLayoutId, @LayoutRes int mChildItemLayoutId, E data , Drawable mBg) {
        this.mItemLayoutId = mItemLayoutId;
        this.mChildItemLayoutId = mChildItemLayoutId;
        this.data = data;
        this.mBg = mBg;
        hasInitData = false;
    }

    /**
     * 以include的形式引入布局采用
     * @param data
     */
    public WrapData(E data) {
        this.data = data;
    }

    public void setHasInitData(boolean hasInitData) {
        this.hasInitData = hasInitData;
    }

    public boolean isHasInitData() {
        return hasInitData;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public int getmChildItemLayoutId() {
        return mChildItemLayoutId;
    }

    public Drawable getBg() {
        return mBg;
    }

    @Override
    public int itemLayoutId() {
        return mItemLayoutId;
    }

    public static class Build<E>{
        @LayoutRes
        int mItemLayoutId;//作为recyclerview item的布局id,必要
        @LayoutRes
        int mChildItemLayoutId;//作为List item的布局id,非必要
        Drawable mBg;   //背景图片
        E data;   //封装数据
        int orientation = LinearLayoutManager.VERTICAL;        //方向
        int spanCount = 2;  //列数

        public Build setChildItemLayoutId(int mChildItemLayoutId) {
            this.mChildItemLayoutId = mChildItemLayoutId;
            return this;
        }

        public Build setBg(Drawable mBg) {
            this.mBg = mBg;
            return this;
        }

        public Build setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Build setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }
        public WrapData create(@LayoutRes int mItemLayoutId, E data) {
            if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
                orientation = LinearLayoutManager.VERTICAL;
            }
            return new WrapData(mItemLayoutId, mChildItemLayoutId, data, mBg);
        }
    }

}
