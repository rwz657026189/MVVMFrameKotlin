package com.rwz.lib_comm.ui.widget.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.rwz.lib_comm.R;


/**
 * Created by rwz on 2020/4/20.
 * 设置不同状态(普通、加载中、失败)的icon
 *  适用于点击某个按钮后，发起网络请求
 *  1. 默认居中展示
 *  2. 设置drawableLeft、drawableRight... 后会覆盖position属性
 *  3. 宽高值不做限制
 */

public class IconStateTextView extends SingleCenterTextView{

    //正常状态
    private Drawable mNormalDrawable;
    //加载中
    private Drawable mLoadingDrawable;
    //加载失败，重试
    private Drawable mErrorDrawable;
    //加载完成
    private Drawable mCompletedDrawable;
    //状态
    private State mState = State.NORMAL;
    //icon位置(父类该属性会根据drawable擦出，该值赋值后不再改变)
    private int mPosition;
    private Animatable mCurrAnim;
    //加载的时候是否运行可点击
    private boolean isEnableIfLoading;

    public IconStateTextView(Context context) {
        this(context, null);
    }

    public IconStateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconStateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int position = NONE;
        if (attrs != null) {
            TypedArray type = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconStateTextView,0,0);
            mNormalDrawable = type.getDrawable(R.styleable.IconStateTextView_icon_normal);
            mLoadingDrawable = type.getDrawable(R.styleable.IconStateTextView_icon_loading);
            mErrorDrawable = type.getDrawable(R.styleable.IconStateTextView_icon_error);
            mCompletedDrawable = type.getDrawable(R.styleable.IconStateTextView_icon_completed);
            isEnableIfLoading = type.getBoolean(R.styleable.IconStateTextView_enableIfLoading, false);
            position = type.getInt(R.styleable.IconStateTextView_position, NONE);
            type.recycle();
        }
        if (position != NONE && getPosition() == NONE) {
            mPosition = position;
            setPosition(position);
        } else {
            mPosition = getPosition();
        }
        setState(mState);
    }

    public enum State{
        NORMAL, LOADING, ERROR, COMPLETED
    }

    private Drawable getCurrDrawable() {
        if (mState == State.NORMAL) {
            return mNormalDrawable;
        } else if (mState == State.LOADING) {
            return mLoadingDrawable;
        } else if (mState == State.ERROR) {
            return mErrorDrawable;
        } else if (mState == State.COMPLETED) {
            return mCompletedDrawable;
        }
        return null;
    }

    private void startAnim() {
        if (getVisibility() != VISIBLE || getWindowVisibility() != VISIBLE) {
            return;
        }
        stopAnim();
        Drawable currDrawable = getCurrDrawable();
        if (!(currDrawable instanceof Animatable)) {
            return;
        }
        mCurrAnim = (Animatable) currDrawable;
        mCurrAnim.start();
    }

    private void stopAnim() {
        if (mCurrAnim != null) {
            mCurrAnim.stop();
            mCurrAnim = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    /**
     * 设置状态值
     */
    public void setState(State mState) {
        this.mState = mState;
        setEnabled(mState != State.LOADING || isEnableIfLoading);
        setDrawable(mPosition, getCurrDrawable());
        startAnim();
        postInvalidate();
    }

    public State getState() {
        return mState;
    }

    private void setDrawable(int position, Drawable drawable) {
        switch (position) {
            case LEFT:
                setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                break;
            case TOP:
                setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                break;
            case RIGHT:
                setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                break;
            case BOTTOM:
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                break;
        }
    }

}
