package com.rwz.mvvm_kotlin_demo.ui.wdiget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rwz.lib_comm.utils.app.DensityUtils;


/**
 * date： 2019/11/14 10:51
 **/
public class ScrollFrameLayout extends FrameLayout {

    //最小高度
    public static final int MIN_HEIGHT = DensityUtils.dp2px(200);
    private int mTouchSlop ;
    private float mDownX, mDownY;
    private float mMoveX, mMoveY;
    private float mDownTopMargin;
    //支持滚动的控件
    private View mScrollerView;
    private int mHeight;
    //是否朝上滑动
    private boolean isScrollUp;
    //贝塞尔曲线图片
    private BesselImageView mBesselImageView;
    //需要上下跳动的控件
    private View mThrobView;
    private ValueAnimator mAnimator;
    private ValueAnimator mThrobAnimator;

    public ScrollFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ScrollFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void bindScrollerView(View scrollerView) {
        mScrollerView = scrollerView;
        //默认不可见
        if (mHeight > 0) {
            setScrollerViewTopMargin(mHeight);
        }
    }

    public void bindBesselImageView(BesselImageView imageView) {
        this.mBesselImageView = imageView;
    }

    public void bindThrobView(View throbView) {
        this.mThrobView = throbView;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        bindScrollerView(mScrollerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return handlerEvent(event);
    }

    private boolean handlerEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mDownX = event.getX();
            mDownY = event.getY();
            mDownTopMargin = getScrollerViewTopMargin();
        } else if (action == MotionEvent.ACTION_MOVE) {
            mMoveX = event.getX();
            mMoveY = event.getY();
            float dy = Math.abs(mMoveY - mDownY);
            if (dy > Math.abs(mMoveX - mDownX) && dy > mTouchSlop) {
                isScrollUp = mMoveY < mDownY;
                stopScroll();
                onScrolled(mMoveY - mDownY);
            } else {
                return false;
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            startScroll(isScrollUp);
        }
        return true;
    }

    private void onScrolled(float totalY) {
        int topMargin = (int) (mDownTopMargin + totalY);
        setScrollerViewTopMargin(topMargin);
        if (mBesselImageView != null) {
            mBesselImageView.onScrollChanged((int) totalY, topMargin);
        }
    }

    /**
     * 开始滑动
     * @param isScrollUp 是否向上滑动
     */
    public void startScroll(boolean isScrollUp) {
        startScrollAnim(isScrollUp);
        startThrobAnim();
    }

    public void startScrollAnim(boolean isScrollUp) {
        if (mScrollerView != null && mHeight > 0) {
            final int startY = getScrollerViewTopMargin();
            int endY = isScrollUp ? MIN_HEIGHT : mHeight;
            mAnimator = ValueAnimator.ofInt(startY, endY);
            mAnimator.setDuration(300 * Math.abs(endY - startY) / endY);
            mAnimator.addUpdateListener(animation -> {
                Integer currValue = (Integer) animation.getAnimatedValue();
                setScrollerViewTopMargin(currValue);
                if (mBesselImageView != null)
                    mBesselImageView.onScrollChanged(currValue - startY, currValue);
            });
            mAnimator.setInterpolator(isScrollUp ? new MyScrollInterpolator() : new LinearInterpolator());
            mAnimator.start();
        }
    }

    /**
     * 停止滑动
     */
    private void stopScroll() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
            mAnimator = null;
        }
        if (mThrobAnimator != null && mThrobAnimator.isRunning()) {
            mThrobAnimator.cancel();
            mThrobAnimator = null;
        }
    }
    private static class MyScrollInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float x) {
            float factor = 0.6f;
            return (float) (Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1);
        }

    }

    private static class MyThrobInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float x) {
            float cycles = 1.0f;
            return (float) Math.sin(2 * cycles * Math.PI * x);
        }

    }

    private void startThrobAnim() {
        if(mThrobView == null)
            return;
        MarginLayoutParams params = (MarginLayoutParams) mThrobView.getLayoutParams();
        int topMargin = params.topMargin;
        int endY = topMargin + MIN_HEIGHT / 4;
        mThrobAnimator = ValueAnimator.ofFloat(topMargin, endY);
        mThrobAnimator.setDuration(300);
        mThrobAnimator.addUpdateListener(animation -> {
            float currValue = (Float) animation.getAnimatedValue();
            MarginLayoutParams params1 = (MarginLayoutParams) mThrobView.getLayoutParams();
            params1.topMargin = (int) currValue;
            mThrobView.setLayoutParams(params1);
        });
        mThrobAnimator.setInterpolator(new MyThrobInterpolator());
        mThrobAnimator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        stopScroll();
    }

    private void setScrollerViewTopMargin(int value) {
        if(mScrollerView == null)
            return;
        MarginLayoutParams params = (MarginLayoutParams) mScrollerView.getLayoutParams();
        params.topMargin = Math.max(0, Math.min(getHeight(), value));
        mScrollerView.setLayoutParams(params);
    }

    private int getScrollerViewTopMargin() {
        if(mScrollerView == null)
            return 0;
        MarginLayoutParams params = (MarginLayoutParams) mScrollerView.getLayoutParams();
        return params.topMargin;
    }

}
