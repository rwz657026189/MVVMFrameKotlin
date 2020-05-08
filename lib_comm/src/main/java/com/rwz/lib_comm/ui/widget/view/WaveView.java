package com.rwz.lib_comm.ui.widget.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 多层波浪效果控件
 */
public class WaveView extends View {

    private static final float DEFAULT_AMPLITUDE_RATIO = 0.1f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 1.0f;
    private Paint mViewPaint;
    private List<WaveEntity> mWaveData = new ArrayList<>();
    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
        mViewPaint.setStyle(Paint.Style.FILL);
    }

    public void update() {
        ValueAnimator waveShiftRatioAnim = createAnim(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (WaveEntity entity : mWaveData) {
                    entity.waveShiftRatio = (float) animation.getAnimatedValue();
                }
                postInvalidate();
            }
        }, 2000);
        ValueAnimator amplitudeRatioAnim = createAnim(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (WaveEntity entity : mWaveData) {
                    entity.amplitudeRatio = Math.abs((float)animation.getAnimatedValue() - 0.5f) * 0.08f + 0.01f;
                }
            }
        }, 5000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(waveShiftRatioAnim, amplitudeRatioAnim);
        animatorSet.start();
    }

    private ValueAnimator createAnim(ValueAnimator.AnimatorUpdateListener listener, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1f);
        animator.addUpdateListener(listener);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        for (WaveEntity entity : mWaveData) {
            mViewPaint.setColor(entity.color & 0x00FFFFFF);
            mViewPaint.setAlpha(Color.alpha(entity.color));
            float length = width * entity.waveLengthRatio;
            float shift = length * entity.waveShiftRatio;
            float level = height * entity.waterLevelRatio;
            float amplitude = (height - level) * entity.amplitudeRatio;
            Path path = entity.path;
            path.reset();
            for (int i = 0; i < width; i++) {
                float y = (float) (amplitude * Math.sin(i / length * 2 * Math.PI + shift / length * 2 * Math.PI));
                if (i == 0) {
                    path.moveTo(i, height - level - y);
                } else {
                    path.lineTo(i, height - level - y);
                }
            }
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.close();
            canvas.drawPath(path, mViewPaint);
        }
    }

    public WaveView addWaveData(WaveEntity entity) {
        this.mWaveData.add(entity);
        return this;
    }

    public static final class WaveEntity{
        //振幅
        private float amplitudeRatio;
        //波长
        private float waveLengthRatio;
        //水位(波纹高度)
        private float waterLevelRatio;
        //波偏移
        private float waveShiftRatio;
        //波纹颜色
        private int color;
        private Path path;

        public WaveEntity(int color) {
            this(DEFAULT_AMPLITUDE_RATIO, DEFAULT_WAVE_LENGTH_RATIO, DEFAULT_WATER_LEVEL_RATIO, DEFAULT_WAVE_SHIFT_RATIO, color);
        }

        public WaveEntity(float amplitudeRatio, float waveLengthRatio, float waterLevelRatio, float waveShiftRatio, int color) {
            this.amplitudeRatio = amplitudeRatio;
            this.waveLengthRatio = waveLengthRatio;
            this.waterLevelRatio = waterLevelRatio;
            this.waveShiftRatio = waveShiftRatio;
            this.color = color;
            this.path = new Path();
        }
    }
}
