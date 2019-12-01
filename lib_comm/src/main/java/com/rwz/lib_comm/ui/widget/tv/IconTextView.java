package com.rwz.lib_comm.ui.widget.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.rwz.lib_comm.R;
import com.rwz.lib_comm.utils.app.ResourceUtil;


/**
 * Created by rwz on 2017/3/23.
 *  设置界面左右带icon 且右侧为文字
 *  请把android:layout_width设置为具体数值 或 match_parents
 *
 */

public class IconTextView extends AppCompatTextView {

    private static final int ALIGN_VIEW = 0;        //线条同控件宽度
    private static final int ALIGN_LEFT_ICON = 1;   //线条与左侧icon对其，右侧同控件宽度
    private static final int ALIGN_TEXT = 2;        //线条与左侧text对其，右侧同控件宽度
    private static final int ALIGN_LEFT_RIGHT_ICON = 3;//线条与左侧icon对其，右侧同icon对其
    private static final int ALIGN_TEXT_RIGHT_ICON = 4;//线条与左侧text对其，右侧同icon对其

    private static final int DEF_RES_ID = 0;            //默认应用值
    private int LINE_WIDTH = 1;            //线条宽度
    private int LINE_COLOR;   //线条颜色
    //线条的左右间距是否包含左右padding的宽度(true : 同控件宽度)
//    private final boolean lineContainPadding = false;

    Paint mPaint;
    private String mRightText;      //右侧文本
    private int mRightTextColor;    //右侧文本颜色
    private int mRightTextSize;     //右侧文本字体大小
    private boolean isShowBottomLine;//是否绘制底部线条
    private final int mTextAlignType =  ALIGN_TEXT;     //线条对其类型

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray type = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconTextView,0,0);
            int rightTextId = type.getResourceId(R.styleable.IconTextView_right_text, DEF_RES_ID);
            if (rightTextId != DEF_RES_ID) {
                mRightText = getResources().getString(rightTextId);
            }
            mRightTextColor = type.getColor(R.styleable.IconTextView_right_text_color, getCurrentTextColor());
            mRightTextSize = type.getResourceId(R.styleable.IconTextView_right_text_size, DEF_RES_ID);
            isShowBottomLine = type.getBoolean(R.styleable.IconTextView_show_bottom_line,true);
            type.recycle();
        }
        LINE_WIDTH = (int) getResources().getDimension(R.dimen.line_size_thin);
        LINE_COLOR = ResourceUtil.getColor(R.color.line_color);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(LINE_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Drawable[] drawables = getCompoundDrawables();
        Drawable leftDraw = drawables[0];
        Drawable rightDraw = drawables[2];
        int height = getHeight();
        int width = getWidth(); //空间宽度
        int leftDrawWidth = 0;
        int rightDrawWidth = 0;
        int x = 0;
        int y = height - LINE_WIDTH / 2;
        mPaint.setColor(LINE_COLOR);
        //绘制底部线条
        if (isShowBottomLine) {
            if (leftDraw != null) { //左侧icon
                leftDrawWidth = leftDraw.getIntrinsicWidth();
            }
            int lineStartX = 0, lineEndX = width;
            switch (mTextAlignType) {
                case ALIGN_VIEW:
                    lineStartX = 0;
                    lineEndX = width;
                    break;
                case ALIGN_LEFT_ICON:
                    lineStartX = getPaddingLeft() + leftDrawWidth;
                    break;
                case ALIGN_TEXT:
                    lineStartX = getPaddingLeft() + leftDrawWidth + getCompoundDrawablePadding();
                    break;
                case ALIGN_LEFT_RIGHT_ICON:
                    lineStartX = getPaddingLeft() + leftDrawWidth;
                    lineEndX = width - getPaddingRight();
                    break;
                case ALIGN_TEXT_RIGHT_ICON:
                    lineStartX = getPaddingLeft() + leftDrawWidth + getCompoundDrawablePadding();
                    lineEndX = width - getPaddingRight();
                    break;
            }
            /*canvas.drawLine(lineContainPadding ? x : getPaddingLeft(), y,
                    lineContainPadding ? width : (width - getPaddingRight()),y,mPaint);*/
            canvas.drawLine(lineStartX, y, lineEndX, y, mPaint);
        }


        //绘制右侧文本
        if (!TextUtils.isEmpty(mRightText)) {

            if (mRightTextColor != 0) {
                mPaint.setColor(mRightTextColor);
            }
//                LogUtil.d("onDraw",mRightText,mRightTextSize,mRightTextColor);
            if (mRightTextSize != DEF_RES_ID) {
                mPaint.setTextSize(getResources().getDimension(mRightTextSize));
            }

            TextPaint paint = getPaint();
            float textWidth = paint.measureText(getText() + "");
            int drawablePadding = getCompoundDrawablePadding();
            //多加了一个drawablePadding 是因为与右侧文本有间距
            float leftTextEnd = textWidth + getPaddingLeft() + leftDrawWidth + drawablePadding + drawablePadding;
            //防止右侧文本覆盖左侧文本
            float rightTextWidth = mPaint.measureText(mRightText);

            if (rightDraw != null) {//右侧icon
                rightDrawWidth = rightDraw.getIntrinsicWidth();
            }

            //不能与上面交换,先设置mPaint在测量,才能获取准确的值
            x = (int) (width - getPaddingRight() - rightDrawWidth - drawablePadding / 2 - rightTextWidth);
            if (x < leftTextEnd) {
                x = (int) leftTextEnd;
                float dx = leftTextEnd - x;
                int words = (int) (mRightText.length() * ((rightTextWidth - dx)) / rightTextWidth);
                if (words > 3) {
                    mRightText = mRightText.substring(0, words - 3) + "...";
                }
            }
            int baseline = getBaseline();
            canvas.drawText(mRightText, x, baseline,mPaint);
        }
    }

    String right_text;

    public void setRight_text(String right_text) {
        this.mRightText = right_text;
        invalidate();
    }

    public void setRight_text_color(int mRightTextColor) {
        this.mRightTextColor = mRightTextColor;
    }
}
