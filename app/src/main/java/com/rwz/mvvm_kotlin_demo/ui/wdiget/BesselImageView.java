package com.rwz.mvvm_kotlin_demo.ui.wdiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;


import java.lang.ref.WeakReference;

/**
 * date： 2019/11/14 9:31
 **/
@SuppressLint("AppCompatCustomView")
public class BesselImageView extends ImageView {
    private static final String TAG = "BesselImageView";
    //圆弧高度
    public static final int MAX_RANGE = 200;
    //求交集
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    //混合画笔
    private Paint mPaint;
    //遮罩画笔
    private Paint mMaskPaint;
    //矩形图片高度(定值)(扣除弧度的部分)
    private int mRectangleHeight = -1;
    //最小遮罩的bitmap
    private WeakReference<Bitmap> mMinMaskBitmap;

    public BesselImageView(Context context) {
        this(context, null);
    }

    public BesselImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BesselImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//获取画笔对象并抗锯齿效果
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setStyle(Paint.Style.FILL);
        mMaskPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        if(mRectangleHeight == -1)
            mRectangleHeight = height - MAX_RANGE;
        Drawable drawable = getDrawable();
        if (drawable != null) {
            //获取drawable的宽和高
            int dWidth = drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight();
            float scale = Math.max(getWidth() * 1.0f / dWidth, height * 1.0f / dHeight);
            int showHeight = (int) (scale * dHeight);
            int startY = Math.min(Math.max(0, showHeight - topMargin), showHeight);
            //根据缩放比例，设置bounds，相当于缩放图片了
            drawable.setBounds(0, 0, (int) (scale * dWidth), showHeight);
            Bitmap maskBitmap = getBitmap();
            if (maskBitmap != null && !maskBitmap.isRecycled()) {
                //创建bitmap
                Bitmap bitmap = Bitmap.createBitmap(getWidth(), height, Bitmap.Config.ARGB_8888);
                //创建画布
                Canvas drawCanvas = new Canvas(bitmap);
                drawCanvas.save();
                drawCanvas.translate(0,  -startY);
                drawable.draw(drawCanvas);
                drawCanvas.restore();
                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(mXfermode);
                //绘制形状
                drawCanvas.drawBitmap(maskBitmap, 0, 0, mPaint);
                mPaint.setXfermode(null);
                //将准备好的bitmap绘制出来
                canvas.drawBitmap(bitmap, 0, 0, null);
            } else {
                Log.d(TAG, "onDraw: bitmap is null");
                drawable.draw(canvas);
            }
        }
    }
    /**
     * 绘制形状
     */
    public Bitmap getBitmap() {
        int maxRange = getHeight() - mRectangleHeight;
        int rangeHeight = Math.min(maxRange, maxRange + dy);
        Log.d(TAG, "getBitmap: rangeHeight = " + rangeHeight);
        if (rangeHeight < 0) {
            Bitmap bitmap = mMinMaskBitmap == null ? null : mMinMaskBitmap.get();
            if (bitmap == null || bitmap.isRecycled()) {
                bitmap = getMinMaskBitmap();
                mMinMaskBitmap = new WeakReference<>(bitmap);
            }
            return bitmap;
        }
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth(), mRectangleHeight);
        path.quadTo(getWidth() / 2, mRectangleHeight + rangeHeight, 0, mRectangleHeight);
        path.close();
        canvas.drawPath(path, mMaskPaint);
        return bitmap;
    }

    private Bitmap getMinMaskBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth(), mRectangleHeight);
        path.lineTo(0, mRectangleHeight);
        path.close();
        canvas.drawPath(path, mMaskPaint);
        return bitmap;
    }

    private int dy;
    private int topMargin = Integer.MAX_VALUE;
    public void onScrollChanged(int dy, int topMargin){
        this.dy = dy / 3;
        this.topMargin = topMargin;
        invalidate();
    }

}
