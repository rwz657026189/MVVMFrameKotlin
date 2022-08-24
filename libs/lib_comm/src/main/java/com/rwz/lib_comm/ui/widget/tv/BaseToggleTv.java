package com.rwz.lib_comm.ui.widget.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.rwz.lib_comm.R;


/**
 * Created by rwz on 2017/1/5.
 *  支持自动水平垂直居中对齐， 只需要设置属性 center = true (默认false), Gravity不需要设置, 且设置无效
 */

public class BaseToggleTv extends AppCompatTextView {

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    private final static int DEF_RES_ID = 0;//默认资源id
    private final static int DEF_COLOR = 1;//默认颜色(资源颜色为负值)
    private final boolean isEnableIfChecked; //选中后是否允许取消
    private final int mCheckedBg;  //选中的背景
    private final int mUncheckedBg;//未选中的背景
    private final int mCheckedSrc;//选中的icon
    private final int mUncheckedSrc; //未选中的icon
    private final boolean mCenter;//水平居中
    private String mUncheckedText; //选中的字符串
    private String mCheckedText;   //未选中的字符串
    private final int mUncheckedTextColor; //选中的字体颜色
    private final int mCheckedTextColor;   //未选中的字体颜色
    private final int mPosition;   //图片设置位置
    protected final boolean showAnim;//是否显示动画效果
    private boolean checkedMe; //单独设置属性，不用xml 的checked属性，是为了列表滑动过程反复设置该属性，导致onCheckedChanged()的触发

    public BaseToggleTv(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.scb, 0, 0);
        isEnableIfChecked = typedArray.getBoolean(R.styleable.scb_isEnableIfChecked, true);
        mCheckedBg = typedArray.getResourceId(R.styleable.scb_checkedBg, DEF_RES_ID);
        mUncheckedBg = typedArray.getResourceId(R.styleable.scb_uncheckedBg, DEF_RES_ID);
        mCheckedSrc = typedArray.getResourceId(R.styleable.scb_checkedSrc, DEF_RES_ID);
        mUncheckedSrc = typedArray.getResourceId(R.styleable.scb_uncheckedSrc, DEF_RES_ID);
        mCheckedText = typedArray.getString(R.styleable.scb_checkedText);
        mUncheckedText = typedArray.getString(R.styleable.scb_uncheckedText);
        mCheckedTextColor = typedArray.getColor(R.styleable.scb_checkedTextColor, DEF_COLOR);
        mUncheckedTextColor = typedArray.getColor(R.styleable.scb_uncheckedTextColor, DEF_COLOR);
        showAnim = typedArray.getBoolean(R.styleable.scb_showAnim, true);
        mPosition = typedArray.getInt(R.styleable.scb_src_position, LEFT);
        mCenter = typedArray.getBoolean(R.styleable.scb_center, false);
        typedArray.recycle();
    }

    public boolean isCheckedMe() {
        return checkedMe;
    }

    //databinding 会触发该方法
    public void setCheckedMe(boolean checkedMe) {
//        LogUtil.d("setCheckedMe",checkedMe);
        this.checkedMe = checkedMe;
        setState();
    }

    public void setUncheckedText(String uncheckedText) {
        this.mUncheckedText = uncheckedText;
        if (!checkedMe) {
            setText(uncheckedText);
        }
    }

    public void setCheckedText(String checkedText) {
        this.mCheckedText = checkedText;
        if (checkedMe) {
            setText(checkedText);
        }
    }

    /**
     * 反转状态
     */
    protected boolean toggleCheck() {
        checkedMe = !checkedMe;
        setState();
        return checkedMe;
    }

    protected void setState() {
        //设置水平方向icon时,默认垂直居中对齐; 设置竖直方向icon时,默认水平居中对齐, 故设置文字居中,并偏向icon一方, 再通过平移画布来实现居中对齐
        if (mCenter) {
            switch (mPosition) {
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
            }
        }
        //设置背景
        if (mCheckedBg != DEF_RES_ID && mUncheckedBg!= DEF_RES_ID)
            setBackgroundResource(checkedMe ? mCheckedBg : mUncheckedBg);
        //设置icon
        int drawable = checkedMe ? mCheckedSrc : mUncheckedSrc;
        switch (mPosition) {
            case TOP:
                setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0);
                break;
            case RIGHT:
                setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
                break;
            case BOTTOM:
                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable);
                break;
            case LEFT:
                setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
                break;
        }
        //设置选中和非选中的text
        if (!TextUtils.isEmpty(mCheckedText) && TextUtils.isEmpty(mUncheckedText))
            mUncheckedText = mCheckedText;
        setText(checkedMe ? mCheckedText : mUncheckedText);
        //设置选中和非选中的textColor
        if (mCheckedTextColor != DEF_COLOR && mUncheckedTextColor!= DEF_COLOR)
            setTextColor(checkedMe ? mCheckedTextColor : mUncheckedTextColor);
        //设置选中后是否取消选中
        if (!isEnableIfChecked && checkedMe)
            setEnabled(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence text = getText();
        //如果文字为空，会进行居中对其操作
        if (TextUtils.isEmpty(text)) {
            int width = getWidth();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), mCheckedSrc, opts);
            int bitmapWidth = opts.outWidth;
            if (bitmapWidth > 0) {
                int dx = (width - bitmapWidth)/2;
                canvas.translate(dx, 0);
            }
        } else if(mCenter){
            if (mPosition == LEFT || mPosition == RIGHT) {//水平icon 居中
                translateHorizontal(canvas);
            } else if (mPosition == TOP || mPosition == BOTTOM) {//竖直icon 居中
                translateVertical(canvas);
            }
        }
        super.onDraw(canvas);
    }


    private void translateVertical(Canvas canvas) {
        int height = getHeight();
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[mPosition];
        int drawHeight = drawable == null ? 0 : drawable.getIntrinsicHeight() + getCompoundDrawablePadding();
        float textHeight = TextUtils.isEmpty(getText()) ? 0 :(getPaint().descent() - getPaint().ascent());
        float dy = (height - drawHeight - textHeight) / 2;
        canvas.translate(0, dy * (mPosition == TOP ? 1 : -1));
    }

    private void translateHorizontal(Canvas canvas) {
        int width = getWidth();
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[mPosition];
        int drawWidth = drawable == null ? 0 : drawable.getIntrinsicWidth() + getCompoundDrawablePadding();
        String text = getText() + "";
        float textWidth = TextUtils.isEmpty(text) ? 0 : getPaint().measureText(text);
        float dx = (width - drawWidth - textWidth) / 2;
        canvas.translate(dx * (mPosition == LEFT ? 1 : -1), 0);
    }

}
