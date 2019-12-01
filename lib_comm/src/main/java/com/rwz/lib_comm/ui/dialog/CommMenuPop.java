package com.rwz.lib_comm.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.rwz.lib_comm.R;
import com.rwz.lib_comm.databinding.PopTopMenuBinding;
import com.rwz.lib_comm.entity.params.CommBottomEntity;
import com.rwz.lib_comm.utils.app.ResourceUtil;
import com.rwz.lib_comm.base.BasePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rwz on 2018/7/26.
 */

public class CommMenuPop extends BasePopupWindow<PopTopMenuBinding> implements View.OnClickListener {

    public static final String KEY_ITEMS_ARRAY = "KEY_ITEMS_ARRAY";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_ARGS = "KEY_ARGS";
    public static final String KEY_COLOR = "KEY_COLOR";
    public static final String KEY_CANCELABLE = "KEY_CANCELABLE";
    public static final String KEY_TRANS_STATUS = "KEY_TRANS_STATUS";
    public static final String KEY_CHILD_LAYOUT = "KEY_CHILD_LAYOUT";

    private List<CommBottomEntity> mData;
    private Bundle mArgs;

    public CommMenuPop(Context context, Bundle arguments) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (arguments == null) {
            return;
        }
        LinearLayout mItemContainer = mBind.container;
        mData = arguments.getParcelableArrayList(KEY_ITEMS_ARRAY);
        mArgs = arguments.getParcelable(KEY_ARGS);
        boolean cancelable  = arguments.getBoolean(KEY_CANCELABLE);
        setOutsideTouchable(cancelable);
        int color = arguments.getInt(KEY_COLOR, ResourceUtil.getColor(R.color.text_dark_color));
        if (mData == null) {
            return;
        }
        int childLayout = arguments.getInt(KEY_CHILD_LAYOUT, R.layout.item_simple_menu);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < mData.size(); i++) {
            CommBottomEntity entity = mData.get(i);
            if(entity == null)
                continue;
            View item = inflater.inflate(childLayout, mItemContainer, false);
            TextView textView = item.findViewById(R.id.text);
            if(textView == null)
                continue;
            textView.setText(entity.getContent());
            mItemContainer.addView(item);
            item.setTag(item.getId(), i);
            int itemColor = entity.getColor();
            textView.setTextColor(itemColor == 0 ? color : itemColor);
            if(entity.isClickEnable())
                item.setOnClickListener(this);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.pop_top_menu;
    }

    @Override
    public void onClick(View v) {
        if (onClickItemListener != null && mData != null) {
            Integer position = (Integer) v.getTag(v.getId());
            onClickItemListener.onClickItem(v.getContext(), position, mData, mArgs);
        }
        dismiss();
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    private OnClickItemListener onClickItemListener;

    public interface OnClickItemListener{
        /**
         * @param context  某些dialog跳转dialog，再次跳转需要context
         * @param position 点击位置
         * @param data items
         * @param args  传递的参数
         */
        void onClickItem(Context context, int position, List<CommBottomEntity> data, Bundle args);
    }

    /**
     * 建造者
     * 1.默认标题不可点击
     * 2.不可点击仅针对item
     * */
    public static class Build {
        private String title = "";
        private Bundle args = null;//回调参数
        private int color = ResourceUtil.getColor(R.color.text_dark_color);//回调参数
        private boolean cancelable = true;  //点击外部是否可以取消dialog
        private boolean isTransStatus = false; //是否透明化状态栏
        private ArrayList<CommBottomEntity> data;
        private OnClickItemListener onClickItemListener;
        private int childLayout;

        /** 是否可以取消 **/
        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        /** 传递参数 **/
        public Build setArgs(Bundle args) {
            this.args = args;
            return this;
        }

        /**
         *  设置item布局， 必须包含id 为 text 的TextView控件
         */
        public Build setChildLayout(@LayoutRes int childLayout) {
            this.childLayout = childLayout;
            return this;
        }

        /** 统一颜色值 **/
        public Build setItemColor(int color) {
            this.color = color;
            return this;
        }

        public Build setOnClickItemListener(OnClickItemListener onClickItemListener) {
            this.onClickItemListener = onClickItemListener;
            return this;
        }

        /** 设置是否透明状态栏 默认： false **/
        public Build setTransStatus(boolean transStatus) {
            isTransStatus = transStatus;
            return this;
        }

        /** 添加item **/
        public Build addItem(String content) {
            if(!TextUtils.isEmpty(content))
                addItem(new CommBottomEntity(content, 0, true));
            return this;
        }

        public Build addItem(CommBottomEntity entity) {
            if(data == null)
                data = new ArrayList<>();
            if(entity != null)
                data.add(entity);
            return this;
        }

        public CommMenuPop create(Context context) {
            return create(context, data);
        }

        public CommMenuPop create(Context context, List<String> data) {
            ArrayList<CommBottomEntity> list = new ArrayList();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    CommBottomEntity entity = new CommBottomEntity(data.get(i), 0, true);
                    list.add(entity);
                }
            }
            return create(context, list);
        }

        public CommMenuPop create(Context context, String[] data) {
            ArrayList<CommBottomEntity> list = new ArrayList();
            if (data != null && data.length > 0) {
                for (String aData : data) {
                    CommBottomEntity entity = new CommBottomEntity(aData,0, true);
                    list.add(entity);
                }
            }
            return create(context, list);
        }

        public CommMenuPop create(Context context, ArrayList<CommBottomEntity> data) {
            Bundle bundle = new Bundle();
            if(data != null)
                bundle.putParcelableArrayList(KEY_ITEMS_ARRAY, data);
            bundle.putString(KEY_TITLE, title);
            if(args != null)
                bundle.putParcelable(KEY_ARGS, args);
            bundle.putInt(KEY_COLOR, color);
            bundle.putBoolean(KEY_CANCELABLE, cancelable);
            bundle.putBoolean(KEY_TRANS_STATUS, isTransStatus);
            if(childLayout != 0)
                bundle.putInt(KEY_CHILD_LAYOUT, childLayout);
            CommMenuPop dialog = new CommMenuPop(context, bundle);
            dialog.setOnClickItemListener(onClickItemListener);
            return dialog;
        }
    }


}