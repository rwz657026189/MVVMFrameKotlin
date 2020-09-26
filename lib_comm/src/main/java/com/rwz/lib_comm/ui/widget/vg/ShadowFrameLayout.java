package com.rwz.lib_comm.ui.widget.vg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rwz.lib_comm.R;

/**
 * date： 2020/9/26 13:24
 * author： rwz
 * description：阴影容器
 **/
public class ShadowFrameLayout extends FrameLayout {
    private Paint mPaint;
    private RectF mRectF;
    private float mBgRound;

    public ShadowFrameLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public ShadowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRectF = new RectF();
        if (attrs != null) {
            TypedArray type = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShadowFrameLayout,
                    0,0);
            mBgRound = type.getDimension(R.styleable.ShadowFrameLayout_bgRound, 0);
            int shadowColor = type.getColor(R.styleable.ShadowFrameLayout_shadowColor, Color.GRAY);
            float shadowX = type.getDimension(R.styleable.ShadowFrameLayout_shadowX, 0);
            float shadowY = type.getDimension(R.styleable.ShadowFrameLayout_shadowY, 0);
            float shadowRound = type.getDimension(R.styleable.ShadowFrameLayout_shadowRound, 0);
            mPaint.setShadowLayer(shadowRound, shadowX, shadowY, shadowColor);
            type.recycle();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (getChildCount() == 1) {
            View child = getChildAt(0);
            float x = child.getX();
            float y = child.getY();
            mRectF.set(x, y, child.getWidth() + x,child.getHeight() + y);
            if (Float.compare(mBgRound, 0f) == 0) {
                canvas.drawRect(mRectF, mPaint);
            } else {
                canvas.drawRoundRect(mRectF, mBgRound, mBgRound, mPaint);
            }
        }
        super.dispatchDraw(canvas);
    }
}
