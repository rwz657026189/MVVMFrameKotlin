package com.rwz.lib_comm.ui.widget.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rwz.lib_comm.R;
import com.rwz.lib_comm.utils.app.ResourceUtil;

import java.util.List;

/**
 * Created by rwz on 2017/3/21.
 * 特殊位置分割线(仅支持水平方向的分割线)
 *  参考： https://blog.csdn.net/johnWcheung/article/details/54953568
 */

public class GridItemSpDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int mSpanCount;
    private List<Integer> mList;//需要设置分割线的位置,若为null,全部设置
    private int skipCount; //顶部偏移item数量(对其边距不处理)

    public GridItemSpDecoration() {
        this(ResourceUtil.getDimen(R.dimen.h_10));
    }

    public GridItemSpDecoration(int space) {
        this(space, null, 0);
    }

    public GridItemSpDecoration(int space , List<Integer> mList, int skipCount) {
        this.space = space;
        this.mList = mList;
        this.skipCount = skipCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        int currPos = parent.getChildLayoutPosition(view);

        int currDecorationPos = indexOf(currPos);
        if (currDecorationPos == -1 || currDecorationPos < skipCount) {
            return;
        }
        currDecorationPos -= skipCount;
        int spanCount = getSpanCount(parent); //2
        int column = currDecorationPos % spanCount; // item column
        int itemCount = parent.getAdapter().getItemCount();
        /**
         * 一个item的宽度 = outRect.left + outRect.right + contentView.width
         * 不能简单的, 否则3个item及以上分配不均匀
         * outRect.left = space
         * outRect.right = space / 2
         * ...
         */
        if (spanCount == 1) { //适合横向滚动的列表
            outRect.left = space;
            outRect.right = currPos == itemCount - 1 ? space : 0;
        } else {
            outRect.left = space - column * space / spanCount; // space - column * ((1f / spanCount) * space)
            outRect.right = (column + 1) * space / spanCount; // (column + 1) * ((1f / spanCount) * space)
        }
    }

    private int indexOf(int pos) {
        if (mList != null && mList.size() != 0) {
            return mList.indexOf(Integer.valueOf(pos));
        }else{
            return pos;
        }
    }

    /**
     * 获取列数
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        if (mSpanCount != 0) {
            return mSpanCount;
        }
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            mSpanCount = ((GridLayoutManager)manager).getSpanCount();
        }else if (manager instanceof StaggeredGridLayoutManager) {
            mSpanCount = ((StaggeredGridLayoutManager)manager).getSpanCount();
        }else{
            mSpanCount = 1;
        }
        return mSpanCount;
    }

    /**
     * 是否是最后一行
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount)
    {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
            childCount = childCount - childCount % spanCount;
            return pos >= childCount;
        } else if (layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL)
            {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                return pos >= childCount;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                return (pos + 1) % spanCount == 0;
            }
        }
        return false;
    }

}