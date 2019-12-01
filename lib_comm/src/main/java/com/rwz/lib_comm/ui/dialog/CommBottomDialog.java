package com.rwz.lib_comm.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.rwz.lib_comm.R;
import com.rwz.lib_comm.base.BaseDialog;
import com.rwz.lib_comm.databinding.DialogBottomLinearBinding;
import com.rwz.lib_comm.entity.params.CommBottomEntity;
import com.rwz.lib_comm.utils.app.DensityUtils;
import com.rwz.lib_comm.utils.app.ResourceUtil;
import com.rwz.lib_comm.utils.show.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 通话的底部弹出dialog
 * Created by rwz on 2016/8/31.
 */
public class CommBottomDialog extends BaseDialog<DialogBottomLinearBinding> implements View.OnClickListener {

    public static final String KEY_ITEMS_ARRAY = "KEY_ITEMS_ARRAY";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_ARGS = "KEY_ARGS";
    public static final String KEY_COLOR = "KEY_COLOR";
    public static final String KEY_CANCELABLE = "KEY_CANCELABLE";
    public static final String KEY_TRANS_STATUS = "KEY_TRANS_STATUS";
    private static final int CANCEL_TOP_MARGIN = 15;//取消按钮顶部margin
    public static final String KEY_CHILD_LAYOUT = "KEY_CHILD_LAYOUT";

    private View mTemp;
    private List<CommBottomEntity> mData;
    private Bundle mArgs;
    private TextView mCancel;

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_bottom_linear;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        LinearLayout mItemContainer = mBind.mItemContainer;
        mTemp = mBind.temp;
        TextView mTitle = mBind.title;
        mData = arguments.getParcelableArrayList(KEY_ITEMS_ARRAY);
        String title = arguments.getString(KEY_TITLE);
        mArgs = arguments.getParcelable(KEY_ARGS);
        boolean cancelable  = arguments.getBoolean(KEY_CANCELABLE);
        boolean isTransStatus = arguments.getBoolean(KEY_TRANS_STATUS);
        int color = arguments.getInt(KEY_COLOR, ResourceUtil.getColor(R.color.text_dark_color));
        if (mData == null) {
            LogUtil.INSTANCE.d("tag","———————————————【 mCancel 不能为空 】———————————————");
            return;
        }
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        WindowManager.LayoutParams p = window.getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if(isTransStatus)
            p.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN; //包含状态栏
        //            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setAttributes(p);
        window.setWindowAnimations(R.style.dialogAnim);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        setCancelable(cancelable);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int childLayout = arguments.getInt(KEY_CHILD_LAYOUT, R.layout.item_simple_menu);
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
        mCancel = (TextView) inflater.inflate(R.layout.item_simple_menu, null, false);
        mCancel.setText(ResourceUtil.getString(R.string.cancel));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = ResourceUtil.getDimen(R.dimen.tab_height);
        params.topMargin = DensityUtils.dp2px(CANCEL_TOP_MARGIN);
        mItemContainer.addView(mCancel, params);
        mCancel.setOnClickListener(this);
        mTemp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != mCancel && v != mTemp && onClickItemListener != null && mData != null) {
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
        private int childLayout;    //子布局（最外层）

        public Build setTitle(String title) {
            this.title = TextUtils.isEmpty(title) ? "" : title;
            return this;
        }

        /** 是否可以取消 **/
        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        /** 传递参数 **/
        public Build setArgs(Bundle args) {
            this.args = args;
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

        /**
         *  设置item布局， 必须包含id 为 text 的TextView控件
         */
        public Build setChildLayout(@LayoutRes int childLayout) {
            this.childLayout = childLayout;
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

        public CommBottomDialog create() {
            return create(data);
        }

        public CommBottomDialog create(List<String> data) {
            ArrayList<CommBottomEntity> list = new ArrayList();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    CommBottomEntity entity = new CommBottomEntity(data.get(i),0, true);
                    list.add(entity);
                }
            }
            return create(list);
        }

        public CommBottomDialog create(String[] data) {
            ArrayList<CommBottomEntity> list = new ArrayList();
            if (data != null && data.length > 0) {
                for (String str : data) {
                    CommBottomEntity entity = new CommBottomEntity(str,0, true);
                    list.add(entity);
                }
            }
            return create(list);
        }

        public CommBottomDialog create(ArrayList<CommBottomEntity> data) {
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
            CommBottomDialog dialog = new CommBottomDialog();
            dialog.setArguments(bundle);
            dialog.setOnClickItemListener(onClickItemListener);
            return dialog;
        }
    }


}
