package com.rwz.lib_comm.ui.widget.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.rwz.lib_comm.R;


/**
 * Created by rwz on 2017/3/30.
 * 左右icon居中
 */

public class CenterTextView extends AppCompatTextView {

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;

    private static final int DEF_RES_ID = -1;
    private  int mPosition;
    private Bitmap mIcon;

    private final Paint mPaint;

    public CenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ctv, 0, 0);
        int icon = typedArray.getResourceId(R.styleable.ctv_icon, DEF_RES_ID);
        if (icon != DEF_RES_ID) {
            mIcon = BitmapFactory.decodeResource(getResources(), icon);
        }
        mPosition = typedArray.getInt(R.styleable.ctv_iconPos, LEFT);
        typedArray.recycle();
        mPaint = new Paint();
    }

    public void setIcon(Bitmap icon) {
        this.mIcon = icon;
    }

    public void setIcon(int icon) {
        this.mIcon = BitmapFactory.decodeResource(getResources(), icon);
    }

    public void setIconPos(int pos) {
        mPosition = pos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int offset = translateCanvas(canvas);
        super.onDraw(canvas);
        if (mIcon == null) {
            return;
        }
        int width = getWidth();
        int height = getHeight();

        int iconHeight = mIcon.getHeight();
        float textLength = getPaint().measureText(getText() + "");

        switch (mPosition) {
            case RIGHT:
                float left = width / 2 + textLength / 2 - offset * 2;
                float top = (height - iconHeight)/2;
                canvas.drawBitmap(mIcon, left, top ,mPaint);
                break;
            case LEFT:
                left = width / 2 - textLength / 2 - offset * 2;
                top = (height - iconHeight)/2;
                canvas.drawBitmap(mIcon,left, top ,mPaint);
                break;
        }
        canvas.translate(-offset, 0);
    }

    private int translateCanvas(Canvas canvas) {
        if(mIcon == null) return 0;
        int offX = (mIcon.getWidth() + getCompoundDrawablePadding())/2;
        switch (mPosition) {
            case LEFT:
                canvas.translate(offX, 0);
                return offX;
            case RIGHT:
                canvas.translate(-offX, 0);
                return -offX;
        }
        return 0;
    }
}
