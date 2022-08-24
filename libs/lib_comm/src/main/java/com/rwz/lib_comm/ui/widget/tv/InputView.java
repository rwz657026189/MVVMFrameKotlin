package com.rwz.lib_comm.ui.widget.tv;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by rwz on 2020/4/20.
 * 1.只能输入纯数字
 * 2.支持手机号分段(3-4-4可扩展)显示
 * 3.手机号首位强制为1校验
 * 4.支持输入框点击确认事件的监听
 * 5.支持删除按钮，只需要设置drawableRight属性
 */
public class InputView extends AppCompatEditText {

    private Type mType;
    private String mRegexFilter;
    private int mMaxLength = -1;
    public static final int CODE_MAX_LENGTH = 6;
    public static final int PHONE_MAX_LENGTH = 11;
    public static final String SEPARATOR = " ";
    private final List<Integer> mSeparatorArr = Arrays.asList(3, 7);
    private Drawable mRightDrawable;
    public enum Type{
        FORMAT_PHONE, //手机号格式化成3-4-4
        CODE, //验证码
        PHONE //手机号
    }

    public InputView(Context context) {
        super(context);
        init();
    }

    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setType(Type.FORMAT_PHONE);
    }

    public void setType(@NonNull Type mType) {
        final boolean isInit = this.mType == null;
        this.mType = mType;
        if (mType == Type.CODE) {
            mMaxLength = CODE_MAX_LENGTH;
            mRegexFilter = String.format(Locale.getDefault(), "\\d{1,%d}", mMaxLength);
            setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (mType == Type.FORMAT_PHONE) {
            mMaxLength = PHONE_MAX_LENGTH + SEPARATOR.length() * mSeparatorArr.size();
            mRegexFilter = String.format(Locale.getDefault(), "1[\\d"+  SEPARATOR + "]{0,%d}", (mMaxLength - 1));
            setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (mType == Type.PHONE) {
            mMaxLength = PHONE_MAX_LENGTH;
            mRegexFilter = String.format(Locale.getDefault(), "1[\\d]{0,%d}", (mMaxLength - 1));
            setInputType(InputType.TYPE_CLASS_PHONE);
        }
        if (isInit) {
            InputFilter[] filters = getFilters();
            InputFilter[] newInputFilter;
            InputFilter lengthFilter = new InputFilter.LengthFilter(mMaxLength);
            if (filters == null) {
                newInputFilter = new InputFilter[]{mInputFilter, lengthFilter};
            } else {
                int length = filters.length;
                newInputFilter = new InputFilter[length + 2];
                newInputFilter[length] = mInputFilter;
                newInputFilter[length + 1] = lengthFilter;
            }
            setFilters(newInputFilter);
            addTextChangedListener(mTextWatcher);
        }
    }

    private final InputFilter mInputFilter = (source, start, end, dest, dstart, dend) -> {
        if (!TextUtils.isEmpty(source)) {
            if (mMaxLength != -1 && mMaxLength > dest.length() && source.length() > mMaxLength - dest.length()) {
                source = source.subSequence(0, mMaxLength - dest.length());
            }
            if (!isValid(dest.toString() + source)) {
                return "";
            }
        }
        return source;
    };

    private boolean isValid(CharSequence source) {
        return TextUtils.isEmpty(mRegexFilter) || Pattern.matches(mRegexFilter, source);
    }

    private CharSequence formatPhone(String phoneNum) {
        int length = phoneNum.length();
        if (length == 0) {
            return phoneNum;
        }
        String formatStr = phoneNum;
        for (int i = 0; i < mSeparatorArr.size(); i++) {
            int extra = i * SEPARATOR.length();
            int index = extra + mSeparatorArr.get(i);
            if (formatStr.length() > index) {
                formatStr = formatStr.substring(0, index).concat(SEPARATOR).concat(formatStr.substring(index));
            }
        }
        return formatStr;
    }

    private String getPhoneNum() {
        Editable text = getText();
        return text == null ? "" : text.toString().replaceAll(SEPARATOR, "");
    }

    private boolean isInSeparatorIndex(int index) {
        for (int i = 0; i < mSeparatorArr.size(); i++) {
            Integer curr = mSeparatorArr.get(i);
            if (curr + i * SEPARATOR.length() == index) {
                return true;
            }
        }
        return false;
    }

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setRightDrawableVisible(!TextUtils.isEmpty(s));
            if (mType == Type.FORMAT_PHONE) {
                String origin;
                //为了计算指针偏移量
                int delCount = 0;
                //表示正在删除分隔符
                if (count == 0 && before == 1 && isInSeparatorIndex(start)) {
                    origin = (s.subSequence(0, start - SEPARATOR.length()).toString() + s.subSequence(start, s.length())).replaceAll(SEPARATOR, "");
                    delCount = before;
                } else {
                    origin = getPhoneNum();
                }
                CharSequence formatPhone = formatPhone(origin);
                if (!TextUtils.equals(formatPhone, s)) {
                    int selectionStart = getSelectionStart();
                    //展示格式化后的手机号
                    setText(formatPhone);
                    selectionStart += formatPhone.length() - s.length() - delCount;
                    //重置指针位置
                    int max = formatPhone.length();
                    if (mMaxLength != -1) {
                        max = Math.min(mMaxLength, max);
                    }
                    setSelection(Math.max(Math.min(selectionStart, max), 0));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mEnterClickListener != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            mEnterClickListener.onClick(this);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private OnClickListener mEnterClickListener;

    /**
     * 输入框点击确认事件
     */
    public void setEnterClickListener(OnClickListener mEnterClickListener) {
        this.mEnterClickListener = mEnterClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Drawable rightDrawable = getRightDrawable();
        if (rightDrawable == null || !isRightDrawableVisible) {
            return super.onTouchEvent(event);
        }
        int width = rightDrawable.getIntrinsicWidth();
        float x = event.getX();
        float left = getWidth() - getPaddingRight() - width - getCompoundDrawablePadding();
        if (x > left && x < getWidth()) {
            //点击了删除按钮
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setText("");
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Drawable rightDrawable = getRightDrawable();
        if (rightDrawable != null) {
            isRightDrawableVisible = !TextUtils.isEmpty(getText());
            if (!isRightDrawableVisible) {
                setCompoundDrawables(null, null, null, null);
            }
        }
    }

    private boolean isRightDrawableVisible;
    private void setRightDrawableVisible(boolean isVisible) {
        if (this.isRightDrawableVisible == isVisible) {
            return;
        }
        this.isRightDrawableVisible = isVisible;
        if (isVisible) {
            Drawable rightDrawable = getRightDrawable();
            setCompoundDrawables(null, null, rightDrawable, null);
        } else {
            setCompoundDrawables(null, null, null, null);
        }
    }

    private Drawable getRightDrawable() {
        if (mRightDrawable == null) {
            Drawable[] drawables = getCompoundDrawables();
            if (drawables[2] != null) {
                mRightDrawable = drawables[2];
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Drawable[] drawablesRelative = getCompoundDrawablesRelative();
                if (drawablesRelative[2] != null) {
                    mRightDrawable = drawablesRelative[2];
                }
            }
        }
        return mRightDrawable;
    }

}
