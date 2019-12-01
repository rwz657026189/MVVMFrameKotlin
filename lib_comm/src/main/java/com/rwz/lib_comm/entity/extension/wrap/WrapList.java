package com.rwz.lib_comm.entity.extension.wrap;

import android.graphics.drawable.Drawable;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rwz.lib_comm.entity.response.BaseListEntity;

import java.util.List;

/**
 * Created by rwz on 2017/7/20.
 */

public class WrapList<T> extends BaseListEntity {

    @LayoutRes
    private int mItemLayoutId;//作为recyclerview item的布局id,必要
    @LayoutRes
    private int mChildItemLayoutId;//作为List item的布局id,非必要
    private Drawable mBg;//背景图片
    private List<T> list;
    private int orientation = LinearLayoutManager.VERTICAL;        //方向
    private int spanCount = 2;  //列数
    private String params;//携带参数
    private boolean hasInitData;//定义一个变量,避免重复设置数据
    //分隔条
    private RecyclerView.ItemDecoration itemDecoration;

    /**
     * @param mItemLayoutId  item布局id
     * @param mChildItemLayoutId 子类item布局id 非必要,默认值为0
     * @param list 集合
     */
    private WrapList(@LayoutRes int mItemLayoutId, @LayoutRes int mChildItemLayoutId, List<T> list , Drawable mBg ,
                     int orientation, int spanCount, String params, RecyclerView.ItemDecoration itemDecoration) {
        this.mItemLayoutId = mItemLayoutId;
        this.mChildItemLayoutId = mChildItemLayoutId;
        this.list = list;
        this.mBg = mBg;
        this.orientation = orientation;
        this.spanCount = spanCount;
        this.params = params;
        this.itemDecoration = itemDecoration;
        hasInitData = false;
    }

    public String getParams() {
        return params;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean isHasInitData() {
        return hasInitData;
    }

    public List<T> getList() {
        return list;
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return itemDecoration;
    }

    public int getChildItemLayoutId() {
        return mChildItemLayoutId;
    }

    public Drawable getBg() {
        return mBg;
    }

    @Override
    public int itemLayoutId() {
        return mItemLayoutId;
    }

    public static class Build<T>{
        @LayoutRes
        private int mChildItemLayoutId;//作为List item的布局id,非必要
        private Drawable mBg;   //背景图片
        private List<T> list;   //封装数据
        private int orientation = LinearLayoutManager.VERTICAL;        //方向
        private int spanCount = 2;  //列数
        private String params;//携带参数
        //分隔条
        private RecyclerView.ItemDecoration itemDecoration;

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

        public Build setParams(String params) {
            this.params = params;
            return this;
        }

        public Build setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        public Build setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
            this.itemDecoration = itemDecoration;
            return this;
        }

        public WrapList create(@LayoutRes int mItemLayoutId, List<T> list) {
            if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
                orientation = LinearLayoutManager.VERTICAL;
            }
            return new WrapList(mItemLayoutId, mChildItemLayoutId, list, mBg, orientation, spanCount,
                    params, itemDecoration);
        }
    }



}