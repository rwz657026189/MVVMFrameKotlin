package com.rwz.lib_comm.ui.widget.tv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.rwz.lib_comm.utils.show.LogUtil;


/**
 * Created by rwz on 2017/3/30.
 * 带icon 水平垂直居中（注 ：　仅限制一个icon）
 *  1. 跟普通TextView 设置icon 方式一致，
 *  2. 不需要设置Gravity属性
 *  3. 宽高值不做限制
 */

public class SingleCenterTextView extends AppCompatTextView {

    public static final int NONE = -1; //无效，即普通TextView
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    private int mPosition;   //图片设置位置
    private Drawable mDrawable;

    public SingleCenterTextView(Context context) {
        this(context, null);
    }

    public SingleCenterTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    //******************* 方便支持DataBinding属性  *******************//
    public void setDrawableLeft(@DrawableRes int drawRes) {
        setCompoundDrawablesWithIntrinsicBounds(drawRes, 0, 0, 0);
    }

    public void setDrawableRight(@DrawableRes int drawRes) {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, drawRes, 0);
    }

    public void setDrawableTop(@DrawableRes int drawRes) {
        setCompoundDrawablesWithIntrinsicBounds(0, drawRes, 0, 0);
    }

    public void setDrawableBottom(@DrawableRes int drawRes) {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawRes);
    }


    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        init();
    }

    @Override
    public void setCompoundDrawablesRelative(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        super.setCompoundDrawablesRelative(start, top, end, bottom);
        init();
    }

    private void init() {
        mDrawable = null;
        Drawable[] drawables = getCompoundDrawables();
        for (int i = 0; i < drawables.length; i++) {
            mDrawable = drawables[i];
            if (mDrawable != null) {
                setPosition(i);
                break;
            }
        }
        if(mDrawable == null)
            setPosition(NONE);
    }

    public void setPosition(int position) {
        this.mPosition = position;
        switch (position) {
            case LEFT:
                setGravity(Gravity.CENTER_VERTICAL);
                break;
            case RIGHT:
                setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                break;
            case TOP:
                setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case BOTTOM:
                setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                break;
            default:
                setGravity(Gravity.CENTER);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPosition == LEFT || mPosition == RIGHT) {//水平icon 居中
            translateHorizontal(canvas);
        } else if (mPosition == TOP || mPosition == BOTTOM) {//竖直icon 居中
            translateVertical(canvas);
        }
        super.onDraw(canvas);
    }


    private void translateVertical(Canvas canvas) {
        int height = getHeight();
        int drawHeight = mDrawable == null ? 0 : mDrawable.getIntrinsicHeight() + getCompoundDrawablePadding();
        float textHeight = TextUtils.isEmpty(getText()) ? 0 :(getPaint().descent() - getPaint().ascent());
        float dy = (height - drawHeight - textHeight) / 2;
        canvas.translate(0, dy * (mPosition == TOP ? 1 : -1));
    }

    private void translateHorizontal(Canvas canvas) {
        int width = getWidth();
        int drawWidth = mDrawable == null ? 0 : mDrawable.getIntrinsicWidth() + getCompoundDrawablePadding();
        String text = getText() + "";
        float textWidth = TextUtils.isEmpty(text) ? 0 : getPaint().measureText(text);
        float dx = (width - drawWidth - textWidth) / 2;
        canvas.translate(dx * (mPosition == LEFT ? 1 : -1), 0);
    }

}
