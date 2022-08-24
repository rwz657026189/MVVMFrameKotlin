package com.rwz.lib_comm.ui.widget.vg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.rwz.lib_comm.utils.app.KeyboardHelper;


/**
 * 监听输入法键盘的弹起与隐藏
 * Created by Ren Wenzhang on 2016/8/28.
 * {@link KeyboardHelper}替代
 */
@Deprecated
public class KeyboardLayout extends FrameLayout {

    private static final String TAG = "KeyboardLayout";

    private KeyboardLayoutListener mListener;
    private boolean mIsKeyboardActive = false; //　输入法是否激活
    private int mKeyboardHeight = 400; // 输入法高度

    public KeyboardLayout(Context context) {
        this(context, null, 0);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 监听布局变化
        getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardOnGlobalChangeListener());
    }

    private class KeyboardOnGlobalChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {

        int mScreenHeight = 0;

        private int getScreenHeight() {
            if (mScreenHeight > 0) {
                return mScreenHeight;
            }
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            if(wm == null)
                return 0;
            mScreenHeight = wm
                    .getDefaultDisplay().getHeight();
            return mScreenHeight;
        }

        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            // 获取当前页面窗口的显示范围
            ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int screenHeight = getScreenHeight();
            int keyboardHeight = screenHeight - rect.bottom; // 输入法的高度
            boolean isActive = false;
            if (Math.abs(keyboardHeight) > screenHeight / 5) {
                isActive = true; // 超过屏幕五分之一则表示弹出了输入法
                mKeyboardHeight = keyboardHeight;
                if (mListener != null) {
                    mListener.onKeyboardChanged(true, keyboardHeight);
                }
            } else if(Math.abs(keyboardHeight) < 5){
                if (mListener != null) {
                    mListener.onKeyboardChanged(false, keyboardHeight);
                }
            }
            mIsKeyboardActive = isActive;
        }
    }

    public void setKeyboardListener(KeyboardLayoutListener listener) {
        mListener = listener;
    }

    public KeyboardLayoutListener getKeyboardListener() {
        return mListener;
    }

    public boolean isKeyboardActive() {
        return mIsKeyboardActive;
    }

    /**
     * 获取输入法高度
     * @return
     */
    public int getKeyboardHeight() {
        return mKeyboardHeight;
    }

   /* public interface KeyboardLayoutListener {
        *//**
         * @param isActive       输入法是否激活
         * @param keyboardHeight 输入法面板高度
         *//*
        void onKeyboardStateChanged(boolean isActive, int keyboardHeight);
    }*/

    /**
     * 与上面
     */
    public static abstract class KeyboardLayoutListener {

        boolean isCloseKeyboard;//现状态是否关闭软键盘
        private int keyboardHeight;

        public KeyboardLayoutListener(boolean isCloseKeyboard) {
            this.isCloseKeyboard = isCloseKeyboard;
        }

        /**
         * @param isActive       输入法是否激活
         * @param keyboardHeight 输入法面板高度
         */
        public abstract void onKeyboardStateChanged(boolean isActive, int keyboardHeight);

        public boolean isCloseKeyboard() {
            return isCloseKeyboard;
        }

        /**
         * 该方式是为了每次关闭/打开软键盘只调用一次onKeyboardStateChanged()
         * @param isActive
         * @param keyboardHeight
         */
        public void  onKeyboardChanged(boolean isActive, int keyboardHeight) {
            //关闭软键盘(keyboardHeight逐渐变小)
            if (this.keyboardHeight - keyboardHeight > 0) {
                this.keyboardHeight = keyboardHeight;
                if (isCloseKeyboard) {
                    return;
                } else {
                    isCloseKeyboard = true;
                    onKeyboardStateChanged(false, keyboardHeight);
                }
            }

            //打开软键盘(keyboardHeight逐渐变大)
            if (this.keyboardHeight - keyboardHeight < 0) {
                this.keyboardHeight = keyboardHeight;
                if (isCloseKeyboard) {
                    isCloseKeyboard = false;
                    onKeyboardStateChanged(true, keyboardHeight);
                }
            }
        }

    }

}
