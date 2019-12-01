package com.rwz.lib_comm.ui.widget.tv;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rwz.lib_comm.R;
import com.rwz.lib_comm.config.Orientation;
import com.rwz.lib_comm.utils.app.CommUtils;


/**
 * Created by rwz on 2018/7/20.
 *  带按下、不可点击状态的TextView、居中、不可点击仍然响应点击事件
 */

public class StateButton extends SingleCenterTextView {

    private static final String TAG = "StateButton";
    private Paint mPaint;

    private int mPressColor;        //按下去背景颜色
    private int mDisableColor;      //不可点击背景颜色
    private int mEnableColor;       //可点击背景颜色
    private int mPressTextColor;    //按下去字体颜色
    private int mDisableTextColor;  //不可点击字体颜色
    private int mEnableTextColor;   //可点击字体颜色
    private int mRoundOrientation;  //圆角位置
    private boolean isEmpty;        //是否空心
    private float mRound;           //圆角
    private float mLineSize;        // 线宽
    private boolean isInterceptDisable;   //是否拦截不可点击时的点击事件,默认false(不可点击时仍会收到点击事件)
    private boolean isEnablePressState;   //是否改变不可点击状态
    private boolean isEnableDisableState; //是否改变不可点击状态

    private RectF mViewRectF;
    private float[] mRoundArr;

    private WrapOnClickListener mClickListener;

    public StateButton(Context context) {
        super(context);
        init();
    }

    public StateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateButton, 0, 0);
        int defTextColor = getCurrentTextColor();
        mEnableColor = typedArray.getColor(R.styleable.StateButton_enableColor, Color.TRANSPARENT);
        mEnableTextColor = typedArray.getColor(R.styleable.StateButton_enableTextColor, defTextColor);
        mDisableColor = typedArray.getColor(R.styleable.StateButton_disableColor, mEnableColor);
        mDisableTextColor = typedArray.getColor(R.styleable.StateButton_disableTextColor, mEnableTextColor);
        mPressColor = typedArray.getColor(R.styleable.StateButton_pressColor, mEnableColor);
        mPressTextColor = typedArray.getColor(R.styleable.StateButton_pressTextColor, mEnableTextColor);
        isEmpty = typedArray.getBoolean(R.styleable.StateButton_empty, false);
        isInterceptDisable = typedArray.getBoolean(R.styleable.StateButton_isInterceptDisable, false);
        isEnablePressState = typedArray.getBoolean(R.styleable.StateButton_enablePressState, true);
        isEnableDisableState = typedArray.getBoolean(R.styleable.StateButton_enableDisableState, true);
        mLineSize = typedArray.getDimension(R.styleable.StateButton_lineSize, isEmpty ? 1 : 0);
        mRound = typedArray.getDimension(R.styleable.StateButton_round, 0);
        mRoundOrientation = typedArray.getInt(R.styleable.StateButton_roundOrientation, mRound == 0 ? Orientation.NONE : Orientation.ALL);
        typedArray.recycle();
        init();
    }

    private void init() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStrokeJoin(Paint.Join.ROUND); //结合处为圆弧
        }
        mPaint.setStrokeWidth(mLineSize);
        mPaint.setStyle(isEmpty ? Paint.Style.STROKE : Paint.Style.FILL);
        setRoundArr();
        onNormal();
    }

    private void setRoundArr() {
        float leftTop = 0f;
        float rightTop = 0f;
        float leftBottom = 0f;
        float rightBottom = 0f;
        if (mRoundOrientation == Orientation.ALL || mRoundOrientation == Orientation.BOTTOM) {
            leftBottom = mRound;
            rightBottom = mRound;
        }
        if (mRoundOrientation == Orientation.ALL || mRoundOrientation == Orientation.TOP) {
            leftTop = mRound;
            rightTop = mRound;
        }
        if (mRoundOrientation == Orientation.ALL || mRoundOrientation == Orientation.LEFT) {
            leftTop = mRound;
            leftBottom = mRound;
        }
        if (mRoundOrientation == Orientation.ALL || mRoundOrientation == Orientation.RIGHT) {
            rightTop = mRound;
            rightBottom = mRound;
        }
        mRoundArr = new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //方便xml窗口预览
        isInEditMode();
    }

    /** 按下的状态 **/
    private void onPressed() {
        if (isEnablePressState && isEnabled()) {
            setPaintColor(mPressColor);
            setTextColor(mPressTextColor);
            //避免字体颜色没变时不刷新
            postInvalidate();
        }
    }

    ValueAnimator mColorAnim;
    private void startPressAnim() {
        if (mColorAnim == null) {
            mColorAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
            mColorAnim.setDuration(80);
            mColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float progress = (float) animation.getAnimatedValue();
                    //字体背景
                    int paintColor = CommUtils.getTransitionColor(mEnableColor, mPressColor, progress);
                    mPaint.setColor(paintColor);
                    //字体颜色
                    int textColor = CommUtils.getTransitionColor(mEnableTextColor, mPressTextColor, progress);
                    setTextColor(textColor);
                }
            });
        } else {
            mColorAnim.cancel();
        }
        mColorAnim.start();
    }

    /** 正常的状态 **/
    private void onNormal() {
        setPaintColor(isEnabled() || !isEnableDisableState ? mEnableColor : mDisableColor);
        setTextColor(isEnabled() || !isEnableDisableState ? mEnableTextColor : mDisableTextColor);
        //避免字体颜色没变时不刷新
        postInvalidate();
    }

    @Override
    public void setEnabled(boolean enabled) {
        boolean isChanged = isEnabled() != enabled;
        super.setEnabled(enabled);
        if (isChanged)
            onNormal();
    }

    /** 设置画笔颜色 **/
    private void setPaintColor(int color) {
        if(mPaint == null || color == 0)
            return;
        int alpha = Color.alpha(color);
        int rgb = Color.argb(0xFF, Color.red(color), Color.green(color), Color.blue(color));
//        LogUtil.d(TAG, "setPaintColor", "color = " + color, "alpha = " + alpha, "rgb = " + rgb);
        mPaint.setAlpha(alpha);
        mPaint.setColor(rgb);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        int width = getWidth();
        int height = getHeight();
        float space = mLineSize / 2;
        mViewRectF = new RectF(space, space, width - space, height - space);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //设置背景
        if (mViewRectF != null && mPaint != null && mRoundArr != null && mEnableColor != Color.TRANSPARENT) {
            Path path = new Path();
            //Path.Direction.CW : 顺时针方向的矩形路径
            path.addRoundRect(mViewRectF, mRoundArr, Path.Direction.CW);
            canvas.drawPath(path, mPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                onPressed();
                break;
            case MotionEvent.ACTION_UP:
                //即使不可点击仍然响应事件
                if(!isEnabled() && mClickListener != null && !isInterceptDisable)
                    mClickListener.onClick(this);
            case MotionEvent.ACTION_CANCEL: //取消事件
                onNormal();
                break;
        }
//        LogUtil.d(TAG, "dispatchTouchEvent", "action = " + action);
        return super.dispatchTouchEvent(event);
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                onPressed();
                break;
            case MotionEvent.ACTION_UP:
                //即使不可点击仍然响应事件
                if(!isEnabled() && mClickListener != null && !isInterceptDisable)
                    mClickListener.onClick(this);
            case MotionEvent.ACTION_CANCEL: //取消事件
                onNormal();
                break;
        }
        LogUtil.d(TAG, "onTouchEvent", "action = " + action);
        return super.onTouchEvent(event);
    }*/

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        if (mClickListener == null)
            mClickListener = new WrapOnClickListener(l);
        else
            mClickListener.setListener(l);
        super.setOnClickListener(l);
    }

    private static class WrapOnClickListener implements View.OnClickListener {

        View.OnClickListener listener;

        public WrapOnClickListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onClick(v);
        }
    }

    public void setPressColor(int mPressColor) {
        this.mPressColor = mPressColor;
        init();
    }

    public void setDisableColor(int mDisableColor) {
        this.mDisableColor = mDisableColor;
        init();
    }

    public void setEnableColor(int mEnableColor) {
        this.mEnableColor = mEnableColor;
        init();
    }

    public void setPressTextColor(int mPressTextColor) {
        this.mPressTextColor = mPressTextColor;
        init();
    }

    public void setDisableTextColor(int mDisableTextColor) {
        this.mDisableTextColor = mDisableTextColor;
        init();
    }

    public void setEnableTextColor(int mEnableTextColor) {
        this.mEnableTextColor = mEnableTextColor;
        init();
    }

    public void setRoundOrientation(int mRoundOrientation) {
        this.mRoundOrientation = mRoundOrientation;
        init();
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
        init();
    }

    public void setRound(float mRound) {
        this.mRound = mRound;
        init();
    }

    public void setLineSize(float mLineSize) {
        this.mLineSize = mLineSize;
        init();
    }

    public void setInterceptDisable(boolean interceptDisable) {
        isInterceptDisable = interceptDisable;
    }

    public void setEnablePressState(boolean enablePressState) {
        isEnablePressState = enablePressState;
    }

    public void setEnableDisableState(boolean enableDisableState) {
        isEnableDisableState = enableDisableState;
    }
}
