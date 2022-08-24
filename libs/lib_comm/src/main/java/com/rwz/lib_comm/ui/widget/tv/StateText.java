package com.rwz.lib_comm.ui.widget.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.rwz.lib_comm.R;


/**
 * Created by rwz on 2016/12/9.
 */

public class StateText extends AppCompatTextView implements View.OnClickListener {

    private final static int DEF_STATE = 0;//默认状态
    private final static int DEF_RES_ID = 0;//默认资源id

    private int mState;//当前状态
    private int mTextResId;//text数组资源id
    private int mColorResId;//color数组资源id
    private int mBgResId;//背景数组资源id
    private String[] mTextArr;//定义在array text数组
    private int[] mBgArr;//定义在array 背景数组
    private int[] mColorArr;//定义在array color数组

    public StateText(Context context) {
        this(context, null);
    }

    public StateText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.style.TextAppearance);
    }

    public StateText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray type = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateText,0,0);
        mTextResId = type.getResourceId(R.styleable.StateText_textArr, DEF_RES_ID);
        mColorResId = type.getResourceId(R.styleable.StateText_colorArr, DEF_RES_ID);
        mBgResId = type.getResourceId(R.styleable.StateText_bgArr, DEF_RES_ID);
        type.recycle();
    }

    /**dababinding传值 必须要有set方法**/
    public void setState(int state) {
        this.mState = state;
        init();
    }

    private void init() {
        if(mTextResId != DEF_RES_ID)
            mTextArr = getResources().getStringArray(mTextResId);
        if(mColorResId != DEF_RES_ID)
            mColorArr = getResources().getIntArray(mColorResId);
        if(mBgResId != DEF_RES_ID)
            mBgArr = getResources().getIntArray(mBgResId);
        setStateText();
        setStateColor();
        setStateBg();
        setOnClickListener(this);
    }
    /**根据状态设置text**/
    private void setStateText() {
        if (mTextArr == null || mTextArr.length == 0) {
            return;
        }
        int value = (mState < 0 || mState  >= mTextArr.length) ? DEF_STATE : mState;
        setText(mTextArr[value]);
    }
    /**根据状态设置color**/
    private void setStateColor() {
        if (mColorArr == null || mColorArr.length == 0) {
            return;
        }
        int value = (mState < 0 || mState  >= mColorArr.length) ? DEF_STATE : mState;
        setTextColor(mColorArr[value]);

    }
    /**根据状态设置背景**/
    private void setStateBg() {
        if (mBgArr == null || mBgArr.length == 0) {
            return;
        }
        int value = (mState < 0 || mState  >= mBgArr.length) ? DEF_STATE : mState;
        setBackgroundColor(mBgArr[value]);
    }

    @Override
    public void onClick(View view) {
        if (onClickStateListener != null) {
            onClickStateListener.onClickStae(mState,mTextArr);
        }
    }

    private OnClickStateListener onClickStateListener;

    public void setOnClickStateListener(OnClickStateListener onClickStateListener) {
        this.onClickStateListener = onClickStateListener;
    }
    public interface OnClickStateListener{
        void onClickStae(int state, String[] textArr);
    }
}
