package com.rwz.lib_comm.ui.widget.count_down_btn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.rwz.lib_comm.R;
import com.rwz.lib_comm.utils.app.ResourceUtil;


/**
 * 带进度的跳过按钮
 * Created by lyy on 2016/6/1.
 */
public class JumpBt extends RoundProgressBarWidthNumber {

    String mJumpText;
    final int mJumpColor;
    int mMaxSecond;
    private final int mMax = 100;
    private OnFinishListener mFinishListener;
    private CountDownTimer mTimer;

    public interface OnFinishListener{
        void onFinish();
    }

    public JumpBt(Context context) {
        this(context, null);
    }

    public JumpBt(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.JumpBt);
        mJumpText = a.getString(R.styleable.JumpBt_jump_text);
        mMaxSecond = a.getInteger(R.styleable.JumpBt_max_second, 3000);
        mJumpColor = a.getColor(R.styleable.JumpBt_jump_text_color, Color.BLACK);
        a.recycle();
        if (TextUtils.isEmpty(mJumpText)) {
            mJumpText = ResourceUtil.getString(R.string.go_to);
        }
        setMax(mMax);
    }
    /**
     * 停止
     */
    public void stop(){
        if (mTimer != null){
            mTimer.cancel();
        }
    }

    /**
     * 启动
     */
    public void start() {
        mTimer = new CountDownTimer(mMaxSecond, 50) {
            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                setProgress(mMax * i / mMaxSecond);
                i += 50;
            }

            @Override
            public void onFinish() {
                setProgress(mMax);
                if (mFinishListener != null){
                    mFinishListener.onFinish();
                }
            }
        };
        mTimer.start();
    }

    public void setOnFinishListener(@NonNull OnFinishListener listener){
        mFinishListener = listener;
    }

    public void setMax_second(int maxSecond) {
        mMaxSecond = maxSecond;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        float textWidth = mPaint.measureText(mJumpText);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        // draw unreaded bar
        mPaint.setColor(mUnReachedBarColor);
        mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        // draw reached bar
        mPaint.setColor(mReachedBarColor);
//        mPaint.setStrokeWidth(mReachedProgressBarHeight);
        mPaint.setStrokeWidth(mReachedProgressBarHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0, sweepAngle, false, mPaint);
        // draw text
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mJumpColor);
        canvas.drawText(mJumpText, mRadius - textWidth / 2, mRadius - textHeight, mPaint);

        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }
}
